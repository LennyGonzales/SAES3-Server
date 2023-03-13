package fr.univ_amu.iut.communication;


import javax.net.ssl.SSLSocket;
import java.io.*;

/**
 * Supports the communication with the client
 * @author LennyGonzales
 */
public class Communication {

    private final SSLSocket socketClient;
    private final BufferedReader in;
    private final ObjectInputStream inObject;
    private final ObjectOutputStream outObject;
    private Object object;


    public Communication(SSLSocket socketClient) throws IOException {
        this.socketClient = socketClient;
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        outObject = new ObjectOutputStream(socketClient.getOutputStream());
        inObject = new ObjectInputStream(socketClient.getInputStream());

    }



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
