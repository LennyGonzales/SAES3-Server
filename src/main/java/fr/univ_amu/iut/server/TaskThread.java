package fr.univ_amu.iut.server;


import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.database.dao.DAOMultipleChoiceQuestionsJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionsJDBC;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.server.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.server.login.Login;
import fr.univ_amu.iut.server.module.Modules;
import fr.univ_amu.iut.server.multiplayer.Multiplayer;
import fr.univ_amu.iut.server.questions.GiveQuestions;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Supports the main communication with the client
 * @author LennyGonzales
 */
public class TaskThread implements Runnable {
    private final Communication communication;

    public TaskThread(SSLSocket sockClient) throws IOException, SQLException {
        communication = new Communication(sockClient);
    }

    /**
     * This function supports client login
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public void serviceLogin(Object credentials) throws IOException, SQLException {
        Login login = new Login(communication, (List<String>) credentials);   // Get the username and the password
        login.serviceLogin();
    }

    /**
     * Return 5 qcm
     * @param module chose by the user
     * @return 5 qcm
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public List<MultipleChoiceQuestion> getQCM(String module) throws SQLException {
        DAOMultipleChoiceQuestionsJDBC daoMultipleChoiceResponsesJDBC = new DAOMultipleChoiceQuestionsJDBC();
        return daoMultipleChoiceResponsesJDBC.getACertainNumberOfQCM(5, module);
    }

    /**
     * Return 5 written response questions
     * @param module chose by the user
     * @return 5 written response questions
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public List<WrittenResponseQuestion> getWrittenResponseQuestions(String module) throws SQLException {
        DAOWrittenResponseQuestionsJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionsJDBC();
        return daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(5, module);
    }

    /**
     * Give the qcm and written response questions to the user and verify the answers
     * @throws SQLException if the getACertainNumberOfQCM() or getACertainNumberOfWrittenResponseQuestion() method didn't go well
     * @throws EmptyQuestionsListException if qcmList and writtenResponseQuestionList are empty
     */
    public void giveQuestionsWithSpecificModule(String module) throws SQLException, EmptyQuestionsListException, IOException {
        GiveQuestions giveQuestions = new GiveQuestions(communication, getQCM(module), getWrittenResponseQuestions(module));
        giveQuestions.run();
    }

    /**
     * Supports the creation of a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     * @throws EmptyQuestionsListException  if qcmList and writtenResponseQuestionList are empty
     */
    public void serviceCreationMultiplayer() throws IOException, SQLException, EmptyQuestionsListException {
        Multiplayer multiplayer = new Multiplayer(communication);
        multiplayer.createMultiplayerSession();
    }

    /**
     * Join a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     * @throws EmptyQuestionsListException  if qcmList and writtenResponseQuestionList are empty
     */
    public void serviceJoinMultiplayer() throws IOException, SQLException, EmptyQuestionsListException {
        Multiplayer multiplayer = new Multiplayer(communication);
        multiplayer.joinMultiplayerSession();
    }

    /**
     * Send the modules and get the module chosen by the user
     * @return the module chose
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException  if a SQL request in the Multiplayer class method didn't go well
     */
    public String serviceModules() throws IOException, SQLException {
        Modules modules = new Modules(communication);
        modules.sendModules();
        return modules.getModuleChoice();
    }

    /**
     * Supports the creation of a training session
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws EmptyQuestionsListException call when the list of questions is empty
     */
    public void serviceTraining() throws SQLException, IOException, EmptyQuestionsListException {
        String choice = serviceModules();
        if((choice != null) && (!(choice.equals(Flags.BACK_TO_MENU)))) {   // (choice != null) to not throwing the NullPointerException (String.equals(null))
            giveQuestionsWithSpecificModule(choice);
        }
    }

    /**
     * A function which find the service type (login, create multiplayer session, ...) and call the function associated
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request didn't go well
     * @throws EmptyQuestionsListException call when the list of questions is empty
     */
    public void serviceType() throws SQLException, IOException, EmptyQuestionsListException, NotTheExpectedFlagException, ClassNotFoundException {
        CommunicationFormat message;
        while ((message = communication.receiveMessage()) != null) { // As long as the server receives no requests, it waits
            // Use element
            switch(message.getFlag()) {
                case LOGIN -> serviceLogin(message.getContent());
                //case "SOLO_FLAG" -> giveQuestionsWithSpecificModule("Tous les modules");
                //case "MULTIPLAYER_CREATION_FLAG" -> serviceCreationMultiplayer();
                //case "MULTIPLAYER_JOIN_FLAG" -> serviceJoinMultiplayer();
                case TRAINING -> serviceTraining();
                default -> throw new NotTheExpectedFlagException("LOGIN or SOLO_FLAG or MULTIPLAYER_CREATION_FLAG or MULTIPLAYER_JOIN_FLAG or TRAINING_FLAG");
            }
        }
        communication.close();    // Close the communication when the client leave
    }

    @Override
    public void run() {
        try {
            serviceType();
        } catch (IOException | SQLException | EmptyQuestionsListException | NotTheExpectedFlagException |
                 ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
}
