package fr.univ_amu.iut.control;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.StoryChecking;
import fr.univ_amu.iut.service.UsersChecking;
import fr.univ_amu.iut.service.dao.DAOMultipleChoiceQuestions;
import fr.univ_amu.iut.service.dao.DAOQuestions;
import fr.univ_amu.iut.service.dao.DAOUsers;
import fr.univ_amu.iut.service.dao.DAOWrittenResponseQuestions;
import fr.univ_amu.iut.domain.MultiplayerSession;
import fr.univ_amu.iut.service.multiplayer.MultiplayerSessionsManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controls the user's actions
 * @author LennyGonzales
 */
public class Controllers {
    private Communication communication;

    public Controllers(Communication communication) {
        this.communication = communication;
    }

    /**
     * Control the login
     * @param credentials a list containing email and password
     * @param usersChecking an instance of UsersChecking
     * @param daoUsers interface (Reversing dependencies)
     * @return if the login was successful
     * @throws IOException  if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public boolean loginAction(List<String> credentials, UsersChecking usersChecking, DAOUsers daoUsers) throws IOException, SQLException {
        if(usersChecking.authenticate(credentials.get(0), credentials.get(1), daoUsers)) {
            communication.sendMessage(new CommunicationFormat(Flags.LOGIN_SUCCESSFULLY));
            return true;
        }
        communication.sendMessage(new CommunicationFormat(Flags.LOGIN_NOT_SUCCESSFULLY));
        return false;
    }

    /**
     * Control the story
     * @param module story's module
     * @param numberOfQuestions number of questions for the story
     * @param storyChecking an instance of StoryChecking
     * @param daoMultipleChoiceQuestions interface (Reversing dependencies) to access database
     * @param daoWrittenResponseQuestions interface (Reversing dependencies) to access database
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     * @throws CloneNotSupportedException if the clone in StoryChecking.getStory isn't supported
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void storyAction(String module, int numberOfQuestions, StoryChecking storyChecking, DAOMultipleChoiceQuestions daoMultipleChoiceQuestions, DAOWrittenResponseQuestions daoWrittenResponseQuestions) throws SQLException, CloneNotSupportedException, IOException {
        communication.sendMessage(new CommunicationFormat(Flags.STORY, storyChecking.getStory(module, numberOfQuestions, daoMultipleChoiceQuestions, daoWrittenResponseQuestions)));
    }


    /**
     * Control the summary
     * @param questions questions received
     * @param storyChecking an instance of StoryChecking
     * @param usersChecking an instance of UsersChecking
     * @param daoUsers interface (Reversing dependencies)
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws UserIsNotInTheDatabaseException if the user isn't in the database
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public void summaryAction(Object questions, StoryChecking storyChecking, UsersChecking usersChecking, DAOUsers daoUsers) throws IOException, UserIsNotInTheDatabaseException, SQLException {
        communication.sendMessage(new CommunicationFormat(Flags.SUMMARY, storyChecking.getSummary(questions, usersChecking, daoUsers)));
        communication.sendMessage(new CommunicationFormat(Flags.USER_POINTS, storyChecking.getUserPoints(usersChecking)));
    }

    /**
     * Control modules
     * @param storyChecking an instance of StoryChecking
     * @param daoQuestions interface (Reversing dependencies) to access database
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void modulesAction(StoryChecking storyChecking ,DAOQuestions daoQuestions) throws SQLException, IOException {
        communication.sendMessage(new CommunicationFormat(Flags.MODULES, storyChecking.getModules(daoQuestions)));
    }

    /**
     * Control the creation of a session
     * @param module story's module
     * @param numberOfQuestions number of questions for the story
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public void createSessionAction(String module, int numberOfQuestions) throws IOException, SQLException {
        String sessionCode = UUID.randomUUID().toString().substring(0,8);
        MultiplayerSessionsManager.addSession(sessionCode,new MultiplayerSession(module, numberOfQuestions, communication));
        communication.sendMessage(new CommunicationFormat(Flags.CODE, sessionCode));
    }


    public void removeSessionAction(String sessionCode) {
        if(MultiplayerSessionsManager.getSessionWithSessionCode(sessionCode).getHostCommunication().equals(communication)) {    // Verify if the user is the host (owner) of the session
            MultiplayerSessionsManager.removeSession(sessionCode);
        }
    }

    public void beginSessionAction(String sessionCode, StoryChecking storyChecking) throws IOException, CloneNotSupportedException {
        MultiplayerSession multiplayerSession = MultiplayerSessionsManager.getSessionWithSessionCode(sessionCode);
        if (multiplayerSession.getHostCommunication().equals(communication)) {    // Verify if the user is the host (owner) of the session
            multiplayerSession.start();
            MultiplayerSessionsManager.removeSession(sessionCode);

            List<Question> story = storyChecking.prepareStory(multiplayerSession.getMultipleChoiceResponseList(), multiplayerSession.getWrittenResponseQuestionList());
            communication.sendMessage(new CommunicationFormat(Flags.STORY, story));
        }
    }

    // Join
    // multiplayerSession.getQCMList() => StoryChecking.setCurrentQCMList()
    // ...
}
