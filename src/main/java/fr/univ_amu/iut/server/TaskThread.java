package fr.univ_amu.iut.server;


import fr.univ_amu.iut.database.dao.DAOQcmJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionJDBC;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;
import fr.univ_amu.iut.server.login.Login;
import fr.univ_amu.iut.server.module.Modules;
import fr.univ_amu.iut.server.multiplayer.Multiplayer;
import fr.univ_amu.iut.server.questions.GiveQuestions;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

/**
 * Supports the main communication with the client
 */
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
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public void serviceLogin() throws IOException, SQLException {
        Login login = new Login(clientCommunication);   // Get the username and the password
        login.serviceLogin();
    }

    public List<Qcm> getQCM(String module) throws SQLException {
        DAOQcmJDBC daoQcmJDBC = new DAOQcmJDBC();
        return daoQcmJDBC.getACertainNumberOfQCM(5, module);
    }

    public List<WrittenResponseQuestion> getWrittenResponseQuestions(String module) throws SQLException {
        DAOWrittenResponseQuestionJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionJDBC();
        return daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(5, module);
    }

    /**
     * Send questions and answer to the client and verify if the answer is correct
     * @throws SQLException if the getACertainNumberOfQCM() or getACertainNumberOfWrittenResponseQuestion() method didn't go well
     * @throws EmptyQuestionsListException if qcmList and writtenResponseQuestionList are empty
     */
    public void serviceSolo() throws SQLException, EmptyQuestionsListException {
        GiveQuestions giveQuestions = new GiveQuestions(clientCommunication, getQCM("ALL"), getWrittenResponseQuestions("ALL"));
        giveQuestions.run();
    }

    /**
     * Supports the creation of a multiplayer game
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     */
    public void serviceCreationMultiplayer() throws IOException, SQLException {
        Multiplayer multiplayer = new Multiplayer(clientCommunication);
        multiplayer.createMultiplayerSession();
    }

    /**
     * Join a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     */
    public void serviceJoinMultiplayer() throws IOException, SQLException {
        Multiplayer multiplayer = new Multiplayer(clientCommunication);
        multiplayer.joinMultiplayerSession();
    }


    public void serviceTraining() throws SQLException, IOException, EmptyQuestionsListException {
        Modules modules = new Modules(clientCommunication);
        modules.sendModulesToTheHost();
        String moduleChose = modules.getModuleChoice();

        GiveQuestions giveQuestions =  new GiveQuestions(clientCommunication, getQCM(moduleChose), getWrittenResponseQuestions(moduleChose));
        giveQuestions.run();
    }

    /**
     * A function which find the service type and call the function associated
     *
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request didn't go well
     * @throws EmptyQuestionsListException
     */
    public void serviceType() throws SQLException, IOException, EmptyQuestionsListException {  // Find the service between {Login, Solo, Multijoueur, Entraînement}
        while ((str = clientCommunication.receiveMessageFromClient()) != null) { // As long as the server receives no requests, it waits
            switch (str) {
                case "LOGIN_FLAG" -> serviceLogin();
                case "SOLO_FLAG" -> serviceSolo();
                case "MULTIPLAYER_CREATION_FLAG" -> serviceCreationMultiplayer();
                case "MULTIPLAYER_JOIN_FLAG" -> serviceJoinMultiplayer();
                case "TRAINING_FLAG" -> serviceTraining();
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
