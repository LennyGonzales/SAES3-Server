package fr.univ_amu.iut.server;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socketClient;
    private BufferedWriter out;
    private BufferedReader in;
    private String message;

    public Client() throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
    }

    /**
     * Send a String to the server
     * @param message
     * @throws IOException
     */
    public void sendMessageToServer(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    /**
     * Send the message received from the server
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String receiveMessageFromServer() throws IOException {
        if((message = in.readLine()) != null) {
            return message;
        }
        close();
        return null;
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
