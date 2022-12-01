package fr.univ_amu.iut.server.questions;

import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.*;
import java.util.List;

public class GiveQuestions implements Runnable{
    private List<Qcm> quiz;
    private ClientCommunication clientCommunication;

    public GiveQuestions(ClientCommunication clientCommunication, List<Qcm> quiz) {
        this.clientCommunication = clientCommunication;
        this.quiz = quiz;
    }

    /**
     * Send the questions and answers to the client and send a end game flag when the game is finished
     * @throws IOException
     */
    public void giveQuestions() throws IOException {
        for(Qcm q : quiz) {
            clientCommunication.sendMessageToClient(q.getQuestion());
            clientCommunication.sendMessageToClient(q.getAnswer1());
            clientCommunication.sendMessageToClient(q.getAnswer2());
            clientCommunication.sendMessageToClient(q.getAnswer3());

            if(q.getTrueAnswer() == Integer.parseInt(clientCommunication.receiveMessageFromClient())) {
                clientCommunication.sendMessageToClient("CORRECT_ANSWER_FLAG");
            } else {
                clientCommunication.sendMessageToClient("WRONG_ANSWER_FLAG");
            }
        }
        clientCommunication.sendMessageToClient("END_GAME_FLAG");
    }

    @Override
    public void run() {
        try {
            giveQuestions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
