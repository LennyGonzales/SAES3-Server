package fr.univ_amu.iut.server.questions;

import fr.univ_amu.iut.database.dao.DAOUserJDBC;
import fr.univ_amu.iut.database.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;
import fr.univ_amu.iut.server.ClientCommunication;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Supports the quiz/history (give questions and verify the answer)
 */
public class GiveQuestions implements Runnable{
    private Random randValue;
    private Iterator<Qcm> iteratorQcm;
    private Iterator<WrittenResponseQuestion> iteratorWrittenResponseQuestion;
    private ClientCommunication clientCommunication;
    private int numberOfCorrectAnswers;
    private int numberOfQuestions;

    public GiveQuestions(ClientCommunication clientCommunication, List<Qcm> qcmList, List<WrittenResponseQuestion> writtenResponseQuestionList) throws EmptyQuestionsListException{
        this.clientCommunication = clientCommunication;
        if ((qcmList.size() < 1) && (writtenResponseQuestionList.size() < 1)) { // Verify if there are questions in the database
            throw new EmptyQuestionsListException();    // If not, throw exception
        }
        iteratorQcm = qcmList.iterator();
        iteratorWrittenResponseQuestion = writtenResponseQuestionList.iterator();
        randValue = new Random();

        numberOfQuestions = qcmList.size() + writtenResponseQuestionList.size();
    }

    /**
     * Send the question (QCM) to the client and verify if the answer is correct
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void giveQcm() throws IOException {
        Qcm qcm = iteratorQcm.next();
        clientCommunication.sendMessageToClient(qcm.getQuestion());
        clientCommunication.sendMessageToClient(qcm.getDescription());
        clientCommunication.sendMessageToClient(qcm.getAnswer1());
        clientCommunication.sendMessageToClient(qcm.getAnswer2());
        clientCommunication.sendMessageToClient(qcm.getAnswer3());
        if(qcm.getTrueAnswer() == Integer.parseInt(clientCommunication.receiveMessageFromClient())) {
            clientCommunication.sendMessageToClient("CORRECT_ANSWER_FLAG");
            ++numberOfCorrectAnswers;
        } else {
            clientCommunication.sendMessageToClient("WRONG_ANSWER_FLAG");
        }
    }

    /**
     * Send the question (Written response question) to the client and verify if the answer is correct
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void giveWrittenResponseQuestion() throws IOException {
        WrittenResponseQuestion writtenResponseQuestion = iteratorWrittenResponseQuestion.next();
        clientCommunication.sendMessageToClient(writtenResponseQuestion.getQuestion());
        clientCommunication.sendMessageToClient(writtenResponseQuestion.getDescription());
        if((writtenResponseQuestion.getTrueAnswer()).equalsIgnoreCase(clientCommunication.receiveMessageFromClient())) {
            clientCommunication.sendMessageToClient("CORRECT_ANSWER_FLAG");
            ++numberOfCorrectAnswers;
        } else {
            clientCommunication.sendMessageToClient("WRONG_ANSWER_FLAG");
        }
    }

    /**
     * Send a QCM or a written response question with a random value
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void checkingQuestionType() throws IOException {
        while ((iteratorQcm.hasNext()) || (iteratorWrittenResponseQuestion.hasNext())) {
            if((randValue.nextInt(2) == 0) && (iteratorQcm.hasNext())) {
                clientCommunication.sendMessageToClient("QCM_FLAG");
                giveQcm();
            } else if (iteratorWrittenResponseQuestion.hasNext()){
                clientCommunication.sendMessageToClient("WRITTEN_RESPONSE_QUESTION_FLAG");
                giveWrittenResponseQuestion();
            }
        }
    }

    /**
     * Notify the client of the end game by sending a flag
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void endGame() throws IOException {
        clientCommunication.sendMessageToClient("END_GAME_FLAG");
    }

    /**
     * Change the points of the user and send it to him
     * @throws SQLException if the SQL request didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws UserIsNotInTheDatabaseException If the user isn't in the database
     */
    public void changeUserPoints() throws SQLException, IOException, UserIsNotInTheDatabaseException {
        DAOUserJDBC daoUserJDBC = new DAOUserJDBC();
        String email = clientCommunication.receiveMessageFromClient();
        int userPoints = daoUserJDBC.getPointsByEmail(email);
        userPoints += 10 * (numberOfCorrectAnswers - (numberOfQuestions/2.0)) * (1 - (userPoints / 2000.0));
        daoUserJDBC.setPointsByEmail(email, userPoints);

        clientCommunication.sendMessageToClient(Integer.toString(userPoints));
    }

    @Override
    public void run() {
        try {
            checkingQuestionType();
            endGame();
            changeUserPoints();
        } catch (IOException | UserIsNotInTheDatabaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
