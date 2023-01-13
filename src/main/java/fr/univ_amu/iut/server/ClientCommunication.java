package fr.univ_amu.iut.server;

import java.io.*;
import java.net.Socket;

/**
 * Supports the communication with the client
 * @author LennyGonzales
 */
public class ClientCommunication {

    private final Socket socketClient;
    private final BufferedReader in;
    private final ObjectInputStream inObject;
    private final ObjectOutputStream outObject;
    private Object object;

    public ClientCommunication(Socket socketClient) throws IOException {
        this.socketClient = socketClient;
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        outObject = new ObjectOutputStream(socketClient.getOutputStream());
        inObject = new ObjectInputStream(socketClient.getInputStream());
    }

    /**
     * Send an object to the client
     * @param obj the object to send
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendObjectToClient(Object obj) throws IOException {
        outObject.writeObject(obj);
        outObject.flush();
    }


    /**
     * Send a String to the client
     * @param message to send to the client
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendMessageToClient(String message) throws IOException {
        outObject.writeObject(message);
        outObject.flush();
    }

    /**
     * Return the message received from the client
     * @return the string sent by the client
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public String receiveMessageFromClient() throws IOException {
        try {
            object = inObject.readObject();
            return object.toString();
        } catch (EOFException | ClassNotFoundException e) {
            close();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return true if the client sent a message to the server
     * @return true - if the client sent a message | else, false
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean isReceiveMessageFromClient() throws IOException {
        return in.ready();
    }

    /**
     * Close the streams and the socket
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void close() throws IOException {
        in.close();
        inObject.close();
        outObject.close();
        socketClient.close();
    }
}
