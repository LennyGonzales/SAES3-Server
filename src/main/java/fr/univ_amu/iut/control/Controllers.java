package fr.univ_amu.iut.control;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.multiplayer.MultiplayerChecking;
import fr.univ_amu.iut.service.story.StoryChecking;
import fr.univ_amu.iut.service.users.UsersChecking;
import fr.univ_amu.iut.service.dao.DAOMultipleChoiceQuestions;
import fr.univ_amu.iut.service.dao.DAOQuestions;
import fr.univ_amu.iut.service.dao.DAOUsers;
import fr.univ_amu.iut.service.dao.DAOWrittenResponseQuestions;
import fr.univ_amu.iut.domain.MultiplayerSession;
import fr.univ_amu.iut.service.multiplayer.MultiplayerSessionsManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Controls the user's actions
 * @author LennyGonzales
 */
public class Controllers {
    private final Communication communication;

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
        communication.sendMessage(new CommunicationFormat(Flags.STORY, storyChecking.createStory(module, numberOfQuestions, daoMultipleChoiceQuestions, daoWrittenResponseQuestions)));
    }

    /**
     * Control the summary
     * @param questions questions received
     * @param storyChecking an instance of StoryChecking
     * @param usersChecking an instance of UsersChecking
     * @param daoUsers interface (Reversing dependencies)
     * @param multiplayerChecking an instance of MultiplayerChecking
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws UserIsNotInTheDatabaseException if the user isn't in the database
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public void summaryAction(Object questions, StoryChecking storyChecking, UsersChecking usersChecking, DAOUsers daoUsers, MultiplayerChecking multiplayerChecking, DAOQuestions daoQuestions) throws IOException, UserIsNotInTheDatabaseException, SQLException {
        HashMap<Question, Boolean> summary = storyChecking.summary(questions, usersChecking, daoUsers, daoQuestions);
        communication.sendMessage(new CommunicationFormat(Flags.SUMMARY, summary));
        communication.sendMessage(new CommunicationFormat(Flags.USER_POINTS, usersChecking.getUser().getPoints()));

        // If it was a multiplayer session
        MultiplayerSession multiplayerSession = multiplayerChecking.getCurrentMultiplayerSession();
        if (multiplayerSession != null) {
            int numberOfCorrectAnswers = Collections.frequency(summary.values(), true); // Get the number of correct answers with the summary
            multiplayerSession.addUserOnLeaderboard(usersChecking.getUser().getEmail(), numberOfCorrectAnswers, communication);
            multiplayerSession.sendLeaderboard();

            multiplayerChecking.setCurrentMultiplayerSession(null); // We wouldn't have to use the multiplayer session anymore
        }
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
     * @param multiplayerChecking the instance of MultiplayerChecking
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public void createSessionAction(String module, int numberOfQuestions, MultiplayerChecking multiplayerChecking) throws IOException, SQLException {
        String sessionCode = UUID.randomUUID().toString().substring(0,8);

        MultiplayerSession multiplayerSession = new MultiplayerSession(module, numberOfQuestions, communication);
        MultiplayerSessionsManager.addSession(sessionCode, multiplayerSession);
        multiplayerChecking.setCurrentMultiplayerSession(multiplayerSession);

        communication.sendMessage(new CommunicationFormat(Flags.CODE, sessionCode));
    }


    /**
     * Control the deletion of a multiplayer session
     * @param multiplayerChecking the instance of MultiplayerChecking
     * @return true if the session is canceled
     */
    public boolean removeSessionAction(MultiplayerChecking multiplayerChecking) throws IOException {
        MultiplayerSession multiplayerSession = multiplayerChecking.getCurrentMultiplayerSession();

        if(multiplayerSession.getHostCommunication().equals(communication)) {    // Verify if the user is the host (owner) of the session
            MultiplayerSessionsManager.removeSession(multiplayerSession);

            for(Communication communicationUser : multiplayerSession.getUsers()) {  // Say to all users who joined the session that the session is canceled
                communicationUser.sendMessage(new CommunicationFormat(Flags.CANCEL_SESSION));
            }
            multiplayerChecking.setCurrentMultiplayerSession(null);

            return true;
        }

        if(multiplayerSession != null) {    // If the user joined the session
            multiplayerSession.getUsers().remove(communication);
            multiplayerChecking.setCurrentMultiplayerSession(null);
            return true;
        }

        return false;
    }

    /**
     * Control the beginning of a multiplayer session
     * @param storyChecking an instance of StoryChecking
     * @param multiplayerChecking the instance of MultiplayerChecking*
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws CloneNotSupportedException if the clone in StoryChecking.getStory isn't supported
     */
    public boolean beginSessionAction(StoryChecking storyChecking, MultiplayerChecking multiplayerChecking) throws IOException, CloneNotSupportedException {
        MultiplayerSession multiplayerSession = multiplayerChecking.getCurrentMultiplayerSession();

        if ((multiplayerSession != null) && (multiplayerSession.getHostCommunication() == communication)) {    // Verify if the user is the host (owner) of the session (use '==' because hostCommunication might be null if the user isn't the host)
            MultiplayerSessionsManager.removeSession(multiplayerSession);   // Nobody can join the multiplayer session anymore

            List<Question> story = storyChecking.prepareStory(multiplayerSession.getMultipleChoiceResponseList(), multiplayerSession.getWrittenResponseQuestionList());
            multiplayerSession.start(story); // Notify the users who joined the multiplayer session that the session begin

            communication.sendMessage(new CommunicationFormat(Flags.STORY, story));
            return true;
        }

        return false;
    }

    /**
     * Control the join multiplayer session action
     * @param sessionCode the session code
     * @param usersChecking an instance of UsersChecking
     * @param storyChecking an instance of StoryChecking
     * @param multiplayerChecking an instance of MultiplayerChecking
     * @return if the join action was successful
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws CloneNotSupportedException if the clone in StoryChecking.getStory isn't supported
     */
    public boolean joinSessionAction(String sessionCode, UsersChecking usersChecking, StoryChecking storyChecking, MultiplayerChecking multiplayerChecking) throws IOException, CloneNotSupportedException {
        MultiplayerSession multiplayerSession = MultiplayerSessionsManager.getSessionWithSessionCode(sessionCode);
        if(multiplayerSession != null) { // If the sessions exists
            multiplayerSession.addUser(communication, usersChecking.getUser().getEmail());
            multiplayerChecking.setCurrentMultiplayerSession(multiplayerSession);

            // Store the story in the instance of StoryChecking
            storyChecking.prepareStory(multiplayerSession.getMultipleChoiceResponseList(),multiplayerSession.getWrittenResponseQuestionList());

            communication.sendMessage(new CommunicationFormat(Flags.SESSION_EXISTS));
            return true;
        }
        communication.sendMessage(new CommunicationFormat(Flags.SESSION_NOT_EXISTS));
        return false;
    }

    /**
     * The player switch to the menu and doesn't want anymore an update of the leaderboard
     * @param multiplayerChecking an instance of MultiplayerChecking
     */
    public void leaveSessionAction(MultiplayerChecking multiplayerChecking) {
        MultiplayerSession multiplayerSession = multiplayerChecking.getCurrentMultiplayerSession();
        if(multiplayerSession != null) {
            multiplayerSession.getUsers().remove(communication);
            multiplayerChecking.setCurrentMultiplayerSession(null);
            if(multiplayerSession.getUsers().size() == 0) { MultiplayerSessionsManager.removeSession(multiplayerSession); }  // If everyone is gone
        }
    }
}
