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
    private ClientCommunication clientCommunication;
    private Modules modules;
    private GiveQuestions giveQuestions;

    public TaskThread(Socket sockClient) throws IOException, SQLException {
        clientCommunication = new ClientCommunication(sockClient);
        modules = new Modules(clientCommunication);
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

    /**
     * Return 5 qcm
     * @param module chose by the user
     * @return 5 qcm
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public List<Qcm> getQCM(String module) throws SQLException {
        DAOQcmJDBC daoQcmJDBC = new DAOQcmJDBC();
        return daoQcmJDBC.getACertainNumberOfQCM(5, module);
    }

    /**
     * Return 5 written response questions
     * @param module chose by the user
     * @return 5 written response questions
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public List<WrittenResponseQuestion> getWrittenResponseQuestions(String module) throws SQLException {
        DAOWrittenResponseQuestionJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionJDBC();
        return daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(5, module);
    }

    /**
     * Send questions and answer to the client and verify if the answer is correct
     * @throws SQLException if the getACertainNumberOfQCM() or getACertainNumberOfWrittenResponseQuestion() method didn't go well
     * @throws EmptyQuestionsListException if qcmList and writtenResponseQuestionList are empty
     */
    public void giveQuestionsWithSpecificModule(String module) throws SQLException, EmptyQuestionsListException {
        giveQuestions = new GiveQuestions(clientCommunication, getQCM(module), getWrittenResponseQuestions(module));
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

    /**
     * Send modules and get the module chose
     *
     * @return the module chose
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException  if a SQL request in the Multiplayer class method didn't go well
     */
    public String serviceModules() throws IOException, SQLException {
        modules.sendModulesToTheHost();
        return modules.getModuleChoice();
    }

    /**
     * Supports the creation of a training game
     * @throws SQLException if a SQL request in the Multiplayer class method didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws EmptyQuestionsListException call when the list of questions is empty
     */
    public void serviceTraining() throws SQLException, IOException, EmptyQuestionsListException {
        giveQuestionsWithSpecificModule(serviceModules());
    }

    /**
     * A function which find the service type and call the function associated
     *
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request didn't go well
     * @throws EmptyQuestionsListException call when the list of questions is empty
     */
    public void serviceType() throws SQLException, IOException, EmptyQuestionsListException {  // Find the service between {Login, solo, multiplayer, training}
        String message;
        while ((message = clientCommunication.receiveMessageFromClient()) != null) { // As long as the server receives no requests, it waits
            switch (message) {
                case "LOGIN_FLAG" -> serviceLogin();
                case "SOLO_FLAG" -> giveQuestionsWithSpecificModule("ALL");
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
