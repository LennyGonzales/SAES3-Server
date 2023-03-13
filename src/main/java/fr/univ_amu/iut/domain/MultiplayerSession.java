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
import java.util.List;

/**
 * Supports a multiplayer session
 * @author LennyGonzales
 */
public class MultiplayerSession {
    private final List<MultipleChoiceQuestion> multipleChoiceQuestionList;
    private final List<WrittenResponseQuestion> writtenResponseQuestionList;

    private final List<Communication> users;

    private final Communication hostCommunication;

    public MultiplayerSession(String module, int nbQuestions, Communication hostCommunication) throws SQLException {
        this.hostCommunication = hostCommunication;
        users = new ArrayList<>();

        // Generate the questions lists
        DAOMultipleChoiceQuestionsJDBC daoMultipleChoiceResponsesJDBC = new DAOMultipleChoiceQuestionsJDBC();
        multipleChoiceQuestionList = daoMultipleChoiceResponsesJDBC.getACertainNumberOfQCM(nbQuestions/2, module);
        DAOWrittenResponseQuestionsJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionsJDBC();
        writtenResponseQuestionList = daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(nbQuestions/2, module);
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
     * Run the session for all other users
     * @throws IOException if the communication with a user is closed or didn't go well
     */
    public void start() throws IOException {
        CommunicationFormat message = new CommunicationFormat(Flags.BEGIN);
        for (Communication clientMultiplayerCommunication : users) {
            clientMultiplayerCommunication.sendMessage(message);
        }
        users.add(hostCommunication);
    }
}