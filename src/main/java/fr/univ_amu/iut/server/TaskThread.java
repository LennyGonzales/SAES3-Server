package fr.univ_amu.iut.server;


import fr.univ_amu.iut.database.dao.DAOQuizJDBC;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.server.login.Login;
import fr.univ_amu.iut.server.multiplayer.Multiplayer;
import fr.univ_amu.iut.server.questions.GiveQuestions;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class TaskThread implements Runnable {
    private static Socket sockClient;
    private static BufferedReader in;
    private static BufferedWriter out;
    private String str;
    private ClientCommunication clientCommunication;

    public TaskThread(Socket sockClient) throws IOException {
        this.sockClient = sockClient;
        this.in = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(sockClient.getOutputStream()));

        clientCommunication = new ClientCommunication(sockClient);
    }

    /**
     * This function supports client login
     *
     * @throws IOException
     * @throws SQLException
     */
    public void serviceLogin() throws IOException, SQLException {
        Login login = new Login(clientCommunication);   // Get the username and the password
        login.serviceLogin();
    }

    /**
     * Send questions and answer to the client and verify if the answer is correct
     * @throws SQLException
     * @throws IOException
     */
    public void serviceSolo() throws SQLException, IOException {
        DAOQuizJDBC daoQuiz = new DAOQuizJDBC();
        List<Qcm> qcmList = daoQuiz.findAllQCM();

        GiveQuestions giveQuestions = new GiveQuestions(sockClient, qcmList);
        giveQuestions.run();
    }

    /**
     * Supports the creation of a multiplayer game
     * @throws IOException
     */
    public void serviceCreationMultiplayer() throws IOException, SQLException {
        Multiplayer multiplayer = new Multiplayer(clientCommunication);
        multiplayer.createMultiplayerSession();
    }

    /**
     * Join a multiplayer session
     * @throws IOException
     */
    public void serviceJoinMultiplayer() throws IOException, SQLException {
        Multiplayer multiplayer = new Multiplayer(clientCommunication);
        multiplayer.joinMultiplayerSession();
    }

    /**
     * A function which find the service type and call the function associated
     *
     * @throws IOException
     */
    public void serviceType() throws SQLException, IOException {  // Find the service between {Login, Solo, Multijoueur, Entraînement}
        while ((str = clientCommunication.receiveMessageFromClient()) != null) { // As long as the server receives no requests, it waits
            switch (str) {
                case "LOGIN_FLAG" -> serviceLogin();
                case "SOLO_FLAG" -> serviceSolo();
                case "MULTIPLAYER_CREATION_FLAG" -> serviceCreationMultiplayer();
                case "MULTIPLAYER_JOIN_FLAG" -> serviceJoinMultiplayer();
            }
        }
        System.out.println("END");
        stopRunning();
    }


    /**
     * Stop the connection with the client
     *
     * @throws IOException
     */
    public static void stopRunning() throws IOException {
        in.close();
        out.close();
        sockClient.close();
    }

    @Override
    public void run() {
        try {
            serviceType();
        } catch (IOException | SQLException e){
            throw new RuntimeException(e);
        }
    }
}
