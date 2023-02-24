package fr.univ_amu.iut.communication;


import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.HashMap;

/**
 * Supports the communication with the client
 * @author LennyGonzales
 */
public class ClientCommunication {

    private final SSLSocket socketClient;
    private final BufferedReader in;
    private final ObjectInputStream inObject;
    private final ObjectOutputStream outObject;
    private Object object;
    private HashMap<String, Object> hashMapMessage; // private HashMap<Flag, String> hashMapMessage;


    public ClientCommunication(SSLSocket socketClient) throws IOException {
        this.socketClient = socketClient;
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        outObject = new ObjectOutputStream(socketClient.getOutputStream());
        inObject = new ObjectInputStream(socketClient.getInputStream());

        hashMapMessage = new HashMap<>();
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



    public void sendMessage(Flags flag, Object content) throws IOException {
        HashMap<Flags, Object> hashmap = new HashMap<>();
        hashmap.put(flag,content);

        outObject.writeObject(hashmap);
        outObject.flush();
    }

    public void sendMessage(Flags flag) throws IOException {
        HashMap<Flags, Object> hashmap = new HashMap<>();
        hashmap.put(flag,null);

        outObject.writeObject(hashmap);
        outObject.flush();
    }

    /**
     * Return the message received from the client
     * @return the string sent by the client
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public HashMap<Flags, Object> receiveMessage() throws IOException {
        try {
            object = inObject.readObject();
            if(object instanceof HashMap) {
                return (HashMap<Flags, Object>) object;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
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
            if(object instanceof String) {
                return object.toString();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
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
