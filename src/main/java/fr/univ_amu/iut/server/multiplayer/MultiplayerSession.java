package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOQcmJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionJDBC;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Supports a multiplayer session
 */
public class MultiplayerSession {
    private final List<Qcm> qcmList;
    private final List<WrittenResponseQuestion> writtenResponseQuestionList;

    private final List<ClientCommunication> users;

    private final ClientCommunication hostCommunication;
    private String sessionCode;

    public MultiplayerSession(String sessionCode, String module, ClientCommunication hostCommunication) throws SQLException {
        this.hostCommunication = hostCommunication;
        this.sessionCode = sessionCode;
        users = new ArrayList<>();

        DAOQcmJDBC daoQcmJDBC = new DAOQcmJDBC();
        qcmList = daoQcmJDBC.getACertainNumberOfQCM(5, module);
        DAOWrittenResponseQuestionJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionJDBC();
        writtenResponseQuestionList = daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(5, module);
    }

    /**
     * Get the qcm list
     * @return the qcm list
     */
    public List<Qcm> getQcmList() {
        return qcmList;
    }

    /**
     * Get the written response question list
     * @return the written response question list
     */
    public List<WrittenResponseQuestion> getWrittenResponseQuestionList() {
        return writtenResponseQuestionList;
    }

    /**
     * Add a user
     * @param clientMultiplayerCommunication the communication with this user
     * @param email the email of this user
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void addUser(ClientCommunication clientMultiplayerCommunication, String email) throws IOException {
        users.add(clientMultiplayerCommunication);
        hostCommunication.sendMessageToClient(email);
    }

    /**
     * Verify if the user return to the menu
     * Store users who join the session in a list and notify them that their request has been received, but if the user
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean verifyBackToMenu() throws IOException {
        return (hostCommunication.receiveMessageFromClient()).equals("BACK_TO_MENU_FLAG");    // If the user return to the menu
    }

    /**
     * Store users who join the session in a list and notify them that their request has been received
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void getUsersUntilSessionStartOrBackToMenu() throws IOException {
        users.add(hostCommunication);
        while(!(hostCommunication.isReceiveMessageFromClient())) {
            // While the multiplayer session's host doesn't click on the button 'Lancer', we wait for new users
        }
    }

    /**
     * Run the session for all other users
     * @throws IOException if the communication with a user is closed or didn't go well
     */
    public void executeUsers() throws IOException {
        // Send to all users that the game begins
        for (ClientCommunication clientMultiplayerCommunication : users) {
            clientMultiplayerCommunication.sendMessageToClient("BEGIN_FLAG");   // Notify the client that the session begin (he can read the quiz's data)
        }
        MultiplayerSessions.removeSession(sessionCode);
    }

    /**
     * Return if the game will played
     * @return true - if the game will played | else, false
     * @throws IOException if the communication with a user is closed or didn't go well
     */
    public boolean isGameWillPlayed() throws IOException{
        getUsersUntilSessionStartOrBackToMenu();
        if(verifyBackToMenu()) {
            return false;
        }
        executeUsers();     // Run the session for all other users
        return true;
    }
}