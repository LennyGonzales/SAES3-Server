package fr.univ_amu.iut.domain;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.database.dao.DAOMultipleChoiceQuestionsJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionsJDBC;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.communication.Communication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supports a multiplayer session
 * @author LennyGonzales
 */
public class MultiplayerSession {
    private final List<MultipleChoiceQuestion> multipleChoiceQuestionList;
    private final List<WrittenResponseQuestion> writtenResponseQuestionList;

    private final List<Communication> users;
    private HashMap<String, Integer> leaderboard;

    private final Communication hostCommunication;
    private boolean isRunning;

    public MultiplayerSession(String module, int nbQuestions, Communication hostCommunication) throws SQLException {
        this.hostCommunication = hostCommunication;
        users = new ArrayList<>();
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

    public boolean isRunning() {
        return isRunning;
    }

    public Communication getHostCommunication() {
        return hostCommunication;
    }

    /**
     * Get the qcm list
     * @return the qcm list
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
     * Add an user on the leaderboard
     * @param email user email
     * @param numberOfGoodAnswers the number of good answers
     */
    public void addUserOnLeaderboard(String email, int numberOfGoodAnswers, Communication communication) {
        users.add(communication);
        leaderboard.put(email, numberOfGoodAnswers);
    }

    /**
     * Send the leaderboard to all the users
     * @throws IOException if the communication with a user is closed or didn't go well
     */
    public void sendLeaderboard() throws IOException {
        HashMap<String, Integer> leaderboardToSend = new HashMap<>();
        for(Communication communicationUser : users) {
            /*                                          TO READ
             * We have to create a new object (new reference) so as not to send the same object (same reference)
             * because of the sockets that "remember the object
             * and when it re-receives the same object (same reference), it returns to the client the old value
             */
            leaderboardToSend = new HashMap<>();
            for(Map.Entry<String,Integer> entry : leaderboard.entrySet()) {
                leaderboardToSend.put(entry.getKey(), entry.getValue());
            }
            communicationUser.sendMessage(new CommunicationFormat(Flags.LEADERBOARD, leaderboardToSend));
        }
    }

    /**
     * Run the session for all other users
     * @throws IOException if the communication with a user is closed or didn't go well
     */
    public void start() throws IOException {
        isRunning = true;
        CommunicationFormat message = new CommunicationFormat(Flags.BEGIN);
        for (Communication clientMultiplayerCommunication : users) {
            clientMultiplayerCommunication.sendMessage(message);
        }
        //users.add(hostCommunication);
        users.clear();  // This list will become the list of users who has finish the game
    }
}