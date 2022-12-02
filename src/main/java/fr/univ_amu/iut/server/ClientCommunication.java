package fr.univ_amu.iut.server;

import java.io.*;
import java.net.Socket;

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
     * @param message
     * @throws IOException
     */
    public void sendMessageToClient(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    /**
     * Send the message received from the client
     * @return
     * @throws IOException
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
     * @return
     * @throws IOException
     */
    public boolean isReceiveMessageFromClient() throws IOException {
        return in.ready();
    }

    /**
     * This method close the socket
     * @throws IOException
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socketClient.close();
    }
}
