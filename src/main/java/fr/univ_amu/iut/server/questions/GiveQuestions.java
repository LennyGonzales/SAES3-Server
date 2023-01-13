package fr.univ_amu.iut.server.questions;

import fr.univ_amu.iut.database.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.database.table.MultipleChoiceQuestion;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;
import fr.univ_amu.iut.server.ClientCommunication;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Supports the quiz/story (give questions and verify the answer)
 * @author LennyGonzales
 */
public class GiveQuestions implements Runnable{
    private final Random randValue;
    private final Iterator<MultipleChoiceQuestion> iteratorQcm;
    private final Iterator<WrittenResponseQuestion> iteratorWrittenResponseQuestion;
    private final ClientCommunication clientCommunication;
    private final HashMap<String, Boolean> summaryHashMap;
    private final String module;


    public GiveQuestions(ClientCommunication clientCommunication, List<MultipleChoiceQuestion> multipleChoiceQuestionList, List<WrittenResponseQuestion> writtenResponseQuestionList) throws EmptyQuestionsListException{
        this.clientCommunication = clientCommunication;
        if ((multipleChoiceQuestionList.size() < 1) && (writtenResponseQuestionList.size() < 1)) { // Verify if there are questions in the database
            throw new EmptyQuestionsListException();    // If not, throw exception
        }
        iteratorQcm = multipleChoiceQuestionList.iterator();
        iteratorWrittenResponseQuestion = writtenResponseQuestionList.iterator();
        randValue = new Random();

        summaryHashMap = new HashMap<>();

        // Get the module of the story
        if(writtenResponseQuestionList.size() > 0) {
            module = writtenResponseQuestionList.get(0).getModule();
        } else {
            module = multipleChoiceQuestionList.get(0).getModule();
        }
    }

    /**
     * Send a qcm to the user
     * @param multipleChoiceQuestion the qcm to send
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendQcm(MultipleChoiceQuestion multipleChoiceQuestion) throws IOException {
        clientCommunication.sendMessageToClient(multipleChoiceQuestion.getDescription());
        clientCommunication.sendMessageToClient(multipleChoiceQuestion.getQuestion());
        clientCommunication.sendMessageToClient(multipleChoiceQuestion.getAnswer1());
        clientCommunication.sendMessageToClient(multipleChoiceQuestion.getAnswer2());
        clientCommunication.sendMessageToClient(multipleChoiceQuestion.getAnswer3());
    }

    /**
     * Send the question (QCM) to the user and verify if the answer is correct
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void giveQcm() throws IOException {
        MultipleChoiceQuestion multipleChoiceQuestion = iteratorQcm.next();
        sendQcm(multipleChoiceQuestion);

        // Put the question and check if the answer is correct or not
        String answer = clientCommunication.receiveMessageFromClient();
        if(answer == null) {
            summaryHashMap.put(multipleChoiceQuestion.getQuestion(), false);   // Not throwing the NumberFormatException : Cannot parse null string
        } else {
            summaryHashMap.put(multipleChoiceQuestion.getQuestion(), (multipleChoiceQuestion.getTrueAnswer() == Integer.parseInt(answer)));
        }
    }

    /**
     * Send the written response question
     * @param writtenResponseQuestion the written response question instance
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendWrittenResponseQuestion(WrittenResponseQuestion writtenResponseQuestion) throws IOException {
        clientCommunication.sendMessageToClient(writtenResponseQuestion.getDescription());
        clientCommunication.sendMessageToClient(writtenResponseQuestion.getQuestion());
    }

    /**
     * Send the question (Written response question) to the client and verify if the answer is correct
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void giveWrittenResponseQuestion() throws IOException {
        WrittenResponseQuestion writtenResponseQuestion = iteratorWrittenResponseQuestion.next();
        sendWrittenResponseQuestion(writtenResponseQuestion);
        summaryHashMap.put(writtenResponseQuestion.getQuestion(), (writtenResponseQuestion.getTrueAnswer()).equalsIgnoreCase(clientCommunication.receiveMessageFromClient())); // Put the question and check if the answer is correct or not
    }

    /**
     * Send a QCM or a written response question with a random value
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void checkingQuestionType() throws IOException, ClassNotFoundException {
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
     * Supports the end game
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void endGame() throws IOException, UserIsNotInTheDatabaseException, SQLException, ClassNotFoundException {
        clientCommunication.sendMessageToClient("END_GAME_FLAG"); // Notify the client of the end game by sending a flag

        Summary summary = new Summary(clientCommunication, summaryHashMap);
        summary.initialize();
    }

    /**
     * Send the module to the user
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendModule() throws IOException {
        clientCommunication.sendMessageToClient(module);
    }

    @Override
    public void run() {
        try {
            sendModule();
            checkingQuestionType();
            endGame();
        } catch (IOException | ClassNotFoundException | UserIsNotInTheDatabaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
