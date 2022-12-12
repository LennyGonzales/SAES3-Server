package fr.univ_amu.iut.server;


import fr.univ_amu.iut.database.dao.DAOQcmJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionJDBC;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;
import fr.univ_amu.iut.server.login.Login;
import fr.univ_amu.iut.server.multiplayer.Multiplayer;
import fr.univ_amu.iut.server.questions.GiveQuestions;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

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
     */
    public void serviceSolo() throws SQLException, EmptyQuestionsListException {
        DAOQcmJDBC daoQcmJDBC = new DAOQcmJDBC();
        List<Qcm> qcmList = daoQcmJDBC.getACertainNumberOfQCM(5, "ALL");

        DAOWrittenResponseQuestionJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionJDBC();
        List<WrittenResponseQuestion> writtenResponseQuestionList = daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(5, "ALL");

        GiveQuestions giveQuestions = new GiveQuestions(clientCommunication, qcmList, writtenResponseQuestionList);
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
    public void serviceType() throws SQLException, IOException, EmptyQuestionsListException {  // Find the service between {Login, Solo, Multijoueur, Entraînement}
        while ((str = clientCommunication.receiveMessageFromClient()) != null) { // As long as the server receives no requests, it waits
            switch (str) {
                case "LOGIN_FLAG" -> serviceLogin();
                case "SOLO_FLAG" -> serviceSolo();
                case "MULTIPLAYER_CREATION_FLAG" -> serviceCreationMultiplayer();
                case "MULTIPLAYER_JOIN_FLAG" -> serviceJoinMultiplayer();
            }
        }
        clientCommunication.close();
    }

    @Override
    public void run() {
        try {
            serviceType();
        } catch (IOException | SQLException | EmptyQuestionsListException e){
            throw new RuntimeException(e);
        }
    }
}
