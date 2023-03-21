package fr.univ_amu.iut.domain;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.database.dao.DAOMultipleChoiceQuestionsJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionsJDBC;
import fr.univ_amu.iut.communication.Communication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Supports a multiplayer session
 * @author LennyGonzales
 */
public class MultiplayerSession {
    private final List<MultipleChoiceQuestion> multipleChoiceQuestionList;
    private final List<WrittenResponseQuestion> writtenResponseQuestionList;

    private final List<Communication> users;
    private final List<Communication> usersWhoFinished;
    private HashMap<String, Integer> leaderboard;

    private final Communication hostCommunication;
    private boolean isRunning;

    /**
     * Prepare the questions (multiple choice questions and written response questions)
     * @param module the module chosen
     * @param nbQuestions the number of questions
     * @param hostCommunication the instance of Communication for the user who created this session (the host)
     * @throws SQLException if one of the sql queries (get questions) didn't go well
     */
    public MultiplayerSession(String module, int nbQuestions, Communication hostCommunication) throws SQLException {
        this.hostCommunication = hostCommunication;
        users = new ArrayList<>();
        usersWhoFinished = new ArrayList<>();
        isRunning = false;
        leaderboard = new HashMap<>();

        // Generate the questions lists
        DAOMultipleChoiceQuestionsJDBC daoMultipleChoiceResponsesJDBC = new DAOMultipleChoiceQuestionsJDBC();
        multipleChoiceQuestionList = daoMultipleChoiceResponsesJDBC.getACertainNumberOfQCM(nbQuestions/2, module);
        DAOWrittenResponseQuestionsJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionsJDBC();
        writtenResponseQuestionList = daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(nbQuestions/2, module);
    }

    public List<Communication> getUsers() {
        return users;
    }

    public Communication getHostCommunication() {
        return hostCommunication;
    }

    /**
     * Get the qcm list
     * @return the qcm list
     * @throws CloneNotSupportedException if the class doesn't implement Cloneable interface
     */
    public List<MultipleChoiceQuestion> getMultipleChoiceResponseList() throws CloneNotSupportedException {
        List<MultipleChoiceQuestion> out = new ArrayList<>();
        for(MultipleChoiceQuestion question : multipleChoiceQuestionList) {
            out.add(question.clone());
        }
        return out;
    }

    /**
     * Get the written response question list
     * @return the written response question list
     * @throws CloneNotSupportedException if the class doesn't implement Cloneable interface
     */
    public List<WrittenResponseQuestion> getWrittenResponseQuestionList() throws CloneNotSupportedException {
        List<WrittenResponseQuestion> out = new ArrayList<>();
        for(WrittenResponseQuestion question : writtenResponseQuestionList) {
            out.add(question.clone());
        }
        return out;
    }

    /**
     * Add a user to the session
     * @param clientMultiplayerCommunication the communication with this user
     * @param email the email of this user
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void addUser(Communication clientMultiplayerCommunication, String email) throws IOException {
        users.add(clientMultiplayerCommunication);
        hostCommunication.sendMessage(new CommunicationFormat(Flags.NEW_PLAYER, email));
    }

    /**
     * Add a user on the leaderboard
     * @param email user email
     * @param numberOfGoodAnswers the number of good answers
     */
    public void addUserOnLeaderboard(String email, int numberOfGoodAnswers, Communication communication) {
        usersWhoFinished.add(communication);
        leaderboard.put(email, numberOfGoodAnswers);
    }

    /**
     * Send the leaderboard to all the users
     * @throws IOException if the communication with a user is closed or didn't go well
     */
    public void sendLeaderboard() throws IOException {
        HashMap<String, Integer> leaderboardToSend;
        for(Communication communicationUser : usersWhoFinished) {
            /*                                          TO READ
             * We have to create a new object (new reference) so as not to send the same object (same reference)
             * because of the sockets that "remember the object
             * and when it re-receives the same object (same reference), it returns to the client the old value
             */
            leaderboardToSend = new HashMap<>();
            leaderboardToSend.putAll(leaderboard);
            communicationUser.sendMessage(new CommunicationFormat(Flags.LEADERBOARD, leaderboardToSend));
        }
    }

    /**
     * Run the session for all other users
     * @throws IOException if the communication with a user is closed or didn't go well
     */
    public void start(List<Question> story) throws IOException {
        isRunning = true;
        CommunicationFormat message = new CommunicationFormat(Flags.STORY, story);
        for (Communication clientMultiplayerCommunication : users) {
            clientMultiplayerCommunication.sendMessage(message);
        }
    }
}