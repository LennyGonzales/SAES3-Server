package fr.univ_amu.iut.server.questions;

import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;
import fr.univ_amu.iut.server.ClientCommunication;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.*;
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

    public GiveQuestions(ClientCommunication clientCommunication, List<Qcm> qcmList, List<WrittenResponseQuestion> writtenResponseQuestionList) throws EmptyQuestionsListException{
        this.clientCommunication = clientCommunication;
        if ((qcmList.size() > 0) || (writtenResponseQuestionList.size() > 0)) { // Verify if there are questions in the database
            iteratorQcm = qcmList.iterator();
            iteratorWrittenResponseQuestion = writtenResponseQuestionList.iterator();
        } else  {
            throw new EmptyQuestionsListException();
        }
        randValue = new Random();
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

        if((writtenResponseQuestion.getTrueAnswer().toLowerCase()).equals(clientCommunication.receiveMessageFromClient().toLowerCase())) {
            clientCommunication.sendMessageToClient("CORRECT_ANSWER_FLAG");
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
        clientCommunication.sendMessageToClient("END_GAME_FLAG");
    }

    @Override
    public void run() {
        try {
            checkingQuestionType();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
