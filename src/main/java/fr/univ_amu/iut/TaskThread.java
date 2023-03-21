package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.control.Controllers;
import fr.univ_amu.iut.database.dao.DAOMultipleChoiceQuestionsJDBC;
import fr.univ_amu.iut.database.dao.DAOQuestionsJDBC;
import fr.univ_amu.iut.database.dao.DAOUsersJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionsJDBC;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.multiplayer.MultiplayerChecking;
import fr.univ_amu.iut.service.story.StoryChecking;
import fr.univ_amu.iut.service.users.UsersChecking;

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
    private Controllers controller;

    // Checking
    private UsersChecking usersChecking;
    private StoryChecking storyChecking;
    private MultiplayerChecking multiplayerChecking;

    // DAO
    private DAOUsersJDBC daoUsers;
    private DAOMultipleChoiceQuestionsJDBC daoMultipleChoiceQuestions;
    private DAOWrittenResponseQuestionsJDBC daoWrittenResponseQuestions;
    private DAOQuestionsJDBC daoQuestions;

    /**
     * Constructor
     * Initialize the class for the database access
     * @param sockClient the socket client
     * @throws SQLException if the prepareStatement in the class for the database access aren't well define
     */
    public TaskThread(SSLSocket sockClient) throws SQLException {
        communication = new Communication(sockClient);

        usersChecking = new UsersChecking();
        storyChecking = new StoryChecking();
        multiplayerChecking = new MultiplayerChecking();

        daoUsers = new DAOUsersJDBC();
        daoMultipleChoiceQuestions = new DAOMultipleChoiceQuestionsJDBC();
        daoWrittenResponseQuestions = new DAOWrittenResponseQuestionsJDBC();
        daoQuestions = new DAOQuestionsJDBC();

        controller = new Controllers(communication);
    }

    /**
     * A function which find the service type (login, create multiplayer session, ...) and call the function associated
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request didn't go well
     * @throws NotTheExpectedFlagException
     * @throws CloneNotSupportedException
     * @throws UserIsNotInTheDatabaseException
     */
    public void serviceType() throws SQLException, IOException, NotTheExpectedFlagException, CloneNotSupportedException, UserIsNotInTheDatabaseException {
        CommunicationFormat message;
        while ((message = communication.receiveMessage()) != null) { // As long as the server receives no requests, it waits
            // Use element
            switch(message.getFlag()) {
                // Login
                case LOGIN -> controller.loginAction((List<String>) message.getContent(), usersChecking, daoUsers);

                // Story
                case MODULES -> controller.modulesAction(storyChecking, daoQuestions);
                case STORY -> controller.storyAction(((List<Object>)message.getContent()).get(0).toString(), (int)((List<Object>)message.getContent()).get(1), storyChecking, daoMultipleChoiceQuestions, daoWrittenResponseQuestions);
                case SUMMARY -> controller.summaryAction(message.getContent(), storyChecking, usersChecking, daoUsers, multiplayerChecking, daoQuestions);

                // Multiplayer session
                case CREATE_SESSION -> controller.createSessionAction(((List<Object>)message.getContent()).get(0).toString(), (int)((List<Object>)message.getContent()).get(1), multiplayerChecking);
                case CANCEL_SESSION -> controller.removeSessionAction(multiplayerChecking);
                case MULTIPLAYER_JOIN -> controller.joinSessionAction(message.getContent().toString(), usersChecking, storyChecking, multiplayerChecking);
                case BEGIN -> controller.beginSessionAction(storyChecking, multiplayerChecking);
                case LEAVE_SESSION -> controller.leaveSessionAction(multiplayerChecking);   // Leave the session on the summary page to not receive the summary and create interference

                default -> throw new NotTheExpectedFlagException("LOGIN or MODULES or STORY or SUMMARY or CREATE_SESSION or CANCEL_CREATE_SESSION or BEGIN or MULTIPLAYER_JOIN or LEAVE_SESSION : Flag received => " + message.getFlag() );
            }
        }
        communication.close();    // Close the communication when the client leave
    }

    @Override
    public void run() {
        try {
            serviceType();
        } catch (IOException | SQLException | NotTheExpectedFlagException | CloneNotSupportedException | UserIsNotInTheDatabaseException e){
            throw new RuntimeException(e);
        }
    }
}
