package fr.univ_amu.iut.server;

import java.io.*;
import java.net.Socket;

/**
 * Supports the communication with the client
 */
public class ClientCommunication {

    private Socket socketClient;
    private BufferedWriter out;
    private BufferedReader in;
    private String message;

    public ClientCommunication(Socket socketClient) throws IOException {
        this.socketClient = socketClient;
        out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
    }

    /**
     * Send a String to the client
     * @param message to send to the client
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendMessageToClient(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    /**
     * Send the message received from the client
     * @return the string sent by the client
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public String receiveMessageFromClient() throws IOException {
        if((message = in.readLine()) != null) {
            return message;
        }
        close();
        return null;
    }

    /**
     * Return true if the client sent a message to the server
     * @return true - The client sent a message | else, false
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean isReceiveMessageFromClient() throws IOException {
        return in.ready();
    }

    /**
     * This method close the socket
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socketClient.close();
    }
}
