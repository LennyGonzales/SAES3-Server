package fr.univ_amu.iut.communication;

import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Supports the communication with the client
 * @author LennyGonzales
 */
public class Communication {

    private final SSLSocket socketClient;
    private BufferedReader in = null;
    private ObjectInputStream inObject = null;
    private ObjectOutputStream outObject = null;
    private Object object;


    public Communication(SSLSocket socketClient) {
        this.socketClient = socketClient;
        try {
            in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            outObject = new ObjectOutputStream(socketClient.getOutputStream());
            inObject = new ObjectInputStream(socketClient.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to the client
     * @param communicationFormat the message to send
     * @throws IOException if the communication with the client didn't go well
     */
    public void sendMessage(CommunicationFormat communicationFormat) throws IOException {
        outObject.writeObject(communicationFormat);
        outObject.flush();
    }

    /**
     * Return the message received from the client
     * @return the string sent by the client
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public CommunicationFormat receiveMessage() throws IOException {
        try {
            object = inObject.readObject();
            if(object instanceof CommunicationFormat) {
                return (CommunicationFormat) object;
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
