package fr.univ_amu.iut;


import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.control.Controllers;
import fr.univ_amu.iut.database.dao.DAOMultipleChoiceQuestionsJDBC;
import fr.univ_amu.iut.database.dao.DAOQuestionsJDBC;
import fr.univ_amu.iut.database.dao.DAOUsersJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionsJDBC;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.StoryChecking;
import fr.univ_amu.iut.service.UsersChecking;
import fr.univ_amu.iut.service.dao.DAOQuestions;
import fr.univ_amu.iut.service.module.Modules;
import fr.univ_amu.iut.service.multiplayer.Multiplayer;
import fr.univ_amu.iut.service.questions.GiveQuestions;
import fr.univ_amu.iut.exceptions.EmptyQuestionsListException;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Supports the main communication with the client
 * @author LennyGonzales
 */
public class TaskThread implements Runnable {
    private static final String DEFAULT_MODULE = "Tous les modules";
    private final Communication communication;
    private Controllers controller;

    // Cheking
    private UsersChecking usersChecking;
    private StoryChecking storyChecking;

    // DAO
    private DAOUsersJDBC daoUsersJDBC;
    private DAOMultipleChoiceQuestionsJDBC daoMultipleChoiceQuestionsJDBC;
    private DAOWrittenResponseQuestionsJDBC daoWrittenResponseQuestionsJDBC;

    private DAOQuestionsJDBC daoQuestionsJDBC;
    public TaskThread(SSLSocket sockClient) throws IOException, SQLException {
        communication = new Communication(sockClient);
        usersChecking = new UsersChecking();
        storyChecking = new StoryChecking();
        daoUsersJDBC = new DAOUsersJDBC();
        daoMultipleChoiceQuestionsJDBC = new DAOMultipleChoiceQuestionsJDBC();
        daoWrittenResponseQuestionsJDBC = new DAOWrittenResponseQuestionsJDBC();
        daoQuestionsJDBC = new DAOQuestionsJDBC();
        controller = new Controllers(communication);
    }

    /**
     * Return 5 qcm
     * @param module chose by the user
     * @return 5 qcm
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public List<MultipleChoiceQuestion> getQCM(String module, int nbQuestions) throws SQLException {
        DAOMultipleChoiceQuestionsJDBC daoMultipleChoiceResponsesJDBC = new DAOMultipleChoiceQuestionsJDBC();
        return daoMultipleChoiceResponsesJDBC.getACertainNumberOfQCM(nbQuestions, module);
    }

    /**
     * Return 5 written response questions
     * @param module chose by the user
     * @return 5 written response questions
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public List<WrittenResponseQuestion> getWrittenResponseQuestions(String module, int nbQuestions) throws SQLException {
        DAOWrittenResponseQuestionsJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionsJDBC();
        return daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(nbQuestions, module);
    }

    /**
     * Give the qcm and written response questions to the user and verify the answers
     * @throws SQLException if the getACertainNumberOfQCM() or getACertainNumberOfWrittenResponseQuestion() method didn't go well
     * @throws EmptyQuestionsListException if qcmList and writtenResponseQuestionList are empty
     */
    public void giveQuestionsWithSpecificModule(String module, int nbQuestions) throws SQLException, EmptyQuestionsListException, IOException {
        GiveQuestions giveQuestions = new GiveQuestions(communication, getQCM(module, nbQuestions/2), getWrittenResponseQuestions(module, nbQuestions/2));
        giveQuestions.run();
    }

    /**
     * Supports the creation of a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     * @throws EmptyQuestionsListException  if qcmList and writtenResponseQuestionList are empty
     */
    public void serviceCreationMultiplayer(int nbQuestions) throws IOException, SQLException, EmptyQuestionsListException, CloneNotSupportedException {
        Multiplayer multiplayer = new Multiplayer(communication);
        multiplayer.createMultiplayerSession(nbQuestions);
    }

    /**
     * Join a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     * @throws EmptyQuestionsListException  if qcmList and writtenResponseQuestionList are empty
     */
    public void serviceJoinMultiplayer(String code) throws IOException, SQLException, EmptyQuestionsListException, CloneNotSupportedException {
        Multiplayer multiplayer = new Multiplayer(communication);
        multiplayer.joinMultiplayerSession(code);
    }

    /**
     * Supports the creation of a training session
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws EmptyQuestionsListException call when the list of questions is empty
     */
    public void serviceTraining(int nbQuestions) throws SQLException, IOException, EmptyQuestionsListException {
        // Send modules
        Modules modules = new Modules(communication);
        modules.sendModules();

        // Receive module choice
        CommunicationFormat message = communication.receiveMessage();
        if(!(message.getFlag().equals(Flags.BACK_TO_MENU))) {   // (choice != null) to not throwing the NullPointerException (String.equals(null))
            giveQuestionsWithSpecificModule(message.getContent().toString(), nbQuestions);
        }
    }

    /**
     * A function which find the service type (login, create multiplayer session, ...) and call the function associated
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request didn't go well
     * @throws EmptyQuestionsListException call when the list of questions is empty
     */
    public void serviceType() throws SQLException, IOException, EmptyQuestionsListException, NotTheExpectedFlagException, ClassNotFoundException, CloneNotSupportedException, UserIsNotInTheDatabaseException {
        CommunicationFormat message;
        while ((message = communication.receiveMessage()) != null) { // As long as the server receives no requests, it waits
            // Use element
            switch(message.getFlag()) {
                case LOGIN -> controller.loginAction((List<String>) message.getContent(), usersChecking, daoUsersJDBC);

                case MODULES -> controller.modulesAction(storyChecking, daoQuestionsJDBC);

                case STORY -> controller.storyAction(((List<Object>)message.getContent()).get(0).toString(), (int)((List<Object>)message.getContent()).get(1), storyChecking, daoMultipleChoiceQuestionsJDBC, daoWrittenResponseQuestionsJDBC);

                case SUMMARY -> controller.summaryAction(message.getContent(), storyChecking, usersChecking, daoUsersJDBC);

                case MULTIPLAYER_CREATION -> serviceCreationMultiplayer((Integer) message.getContent());

                case MULTIPLAYER_JOIN -> serviceJoinMultiplayer(message.getContent().toString());

                default -> throw new NotTheExpectedFlagException("LOGIN or MODULES or STORY or SUMMARY or MULTIPLAYER_CREATION or MULTIPLAYER_JOIN");
            }
        }
        communication.close();    // Close the communication when the client leave
    }

    @Override
    public void run() {
        try {
            serviceType();
        } catch (IOException | SQLException | EmptyQuestionsListException | NotTheExpectedFlagException |
                 ClassNotFoundException | CloneNotSupportedException | UserIsNotInTheDatabaseException e){
            throw new RuntimeException(e);
        }
    }
}
