package fr.univ_amu.iut.server;

import java.io.*;
import java.net.Socket;

/**
 * Supports the communication with the client
 */
public class ClientCommunication {

    private Socket socketClient;
    private BufferedWriter out;
    private ObjectOutputStream outObject;
    private BufferedReader in;
    private String message;

    public ClientCommunication(Socket socketClient) throws IOException {
        this.socketClient = socketClient;
        out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
    }

    /**
     * Send an object to the client
     * @param obj the object to send
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendObjectToClient(Object obj) throws IOException {
        outObject = new ObjectOutputStream(socketClient.getOutputStream()); // We can't instantiate in the constructor because it obfuscates the traffic (by adding characters in messages)
        outObject.writeObject(obj);
        outObject.flush();
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
        if(outObject != null) { outObject.close(); }
        socketClient.close();
    }
}
