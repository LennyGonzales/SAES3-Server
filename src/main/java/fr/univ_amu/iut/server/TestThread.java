package fr.univ_amu.iut.server;

import java.io.IOException;

public class TestThread extends Thread{

    private ClientCommunication clientCommunication;
    private String message;

    public TestThread(ClientCommunication clientCommunication) {
        this.clientCommunication = clientCommunication;
    }

    public void run() {
        try {
            message = clientCommunication.receiveMessageFromClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessage() {
        return message;
    }

}
