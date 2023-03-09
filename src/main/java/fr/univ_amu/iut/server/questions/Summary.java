package fr.univ_amu.iut.server.questions;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.database.dao.DAOUsersJDBC;
import fr.univ_amu.iut.database.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Supports the summary (give the questions and whether the user's answers were correct or not)
 * @author LennyGonzales
 */
public class Summary {
    private final Communication communication;
    private List<Question> storyReceived;
    private int correctAnswers;
    private HashMap<Integer, String> correctionWrittenResponse;
    private HashMap<Integer, Optional<Integer>> correctionMultipleChoiceResponse;

    public Summary(Communication communication, HashMap<Integer, String> correctionWrittenResponse, HashMap<Integer, Optional<Integer>> correctionMultipleChoiceResponse) {
        this.communication = communication;
        this.correctionWrittenResponse = correctionWrittenResponse;
        this.correctionMultipleChoiceResponse = correctionMultipleChoiceResponse;
        correctAnswers = 0;
    }


    /**
     * Do the correction
     * @return the correction (Question => true or false)
     */
    public HashMap<Question, Boolean> doCorrection() {
        HashMap<Question, Boolean> storyToSend = new HashMap<>();
        boolean answerStatus;

        for(Question question : storyReceived) {
            if(question instanceof MultipleChoiceQuestion) {
                answerStatus = correctionMultipleChoiceResponse.get(question.getId()).equals(((MultipleChoiceQuestion) question).getTrueAnswer());
                storyToSend.put(question, answerStatus);

                if(answerStatus) { ++correctAnswers; }
            }
            else if(question instanceof WrittenResponseQuestion) {
                answerStatus = correctionWrittenResponse.get(question.getId()).equals(((WrittenResponseQuestion) question).getTrueAnswer());
                storyToSend.put(question, answerStatus);

                if(answerStatus) { ++correctAnswers; }
            }
        }
        return storyToSend;
    }

    public int doUserPoints(String email) throws UserIsNotInTheDatabaseException, SQLException {
        DAOUsersJDBC daoUserJDBC = new DAOUsersJDBC();
        int newPoints = daoUserJDBC.getPointsByEmail(email);

        newPoints += 10 * (correctAnswers - (storyReceived.size()/2.0)) * (1 - (newPoints / 2000.0));   // function to calculate the new user points

        daoUserJDBC.setPointsByEmail(email, newPoints);
        return newPoints;
    }

    public void initialize() throws UserIsNotInTheDatabaseException, SQLException, IOException {
        CommunicationFormat message = communication.receiveMessage();
        if(message.getFlag().equals(Flags.SUMMARY)) {
            storyReceived = (List<Question>) message.getContent();
            communication.sendMessage(new CommunicationFormat(Flags.SUMMARY, doCorrection()));

            message = communication.receiveMessage();
            if(message.getFlag().equals(Flags.EMAIL)) {
                String email = message.getContent().toString();
                communication.sendMessage(new CommunicationFormat(Flags.USER_POINTS, doUserPoints(email)));
            }
        }
    }
}
