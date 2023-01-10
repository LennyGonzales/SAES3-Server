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
 * Supports the multiplayer session's server
 */
public class MultiplayerSession {
    private final List<Qcm> qcmList;
    private final List<WrittenResponseQuestion> writtenResponseQuestionList;

    private final List<ClientCommunication> clients;

    // Main server
    private final ClientCommunication clientCommunication;
    private String sessionCode;

    public MultiplayerSession(String sessionCode, String module, ClientCommunication clientCommunication) throws SQLException {
        this.clientCommunication = clientCommunication;
        this.sessionCode = sessionCode;
        clients = new ArrayList<>();

        DAOQcmJDBC daoQcmJDBC = new DAOQcmJDBC();
        qcmList = daoQcmJDBC.getACertainNumberOfQCM(5, module);
        DAOWrittenResponseQuestionJDBC daoWrittenResponseQuestionJDBC = new DAOWrittenResponseQuestionJDBC();
        writtenResponseQuestionList = daoWrittenResponseQuestionJDBC.getACertainNumberOfWrittenResponseQuestion(5, module);
    }

    public List<Qcm> getQcmList() {
        return qcmList;
    }

    public List<WrittenResponseQuestion> getWrittenResponseQuestionList() {
        return writtenResponseQuestionList;
    }

    /**
     * Add a client
     * @param clientMultiplayerCommunication a multiplayer
     */
    public void addClient(ClientCommunication clientMultiplayerCommunication, String email) throws IOException {
        clients.add(clientMultiplayerCommunication);
        clientCommunication.sendMessageToClient(email);
    }

    /**
     * Verify if the user return to the menu
     * Store users who join the session in a list and notify them that their request has been received, but if the user
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean verifyBackToMenu() throws IOException {
        return (clientCommunication.receiveMessageFromClient()).equals("BACK_TO_MENU_FLAG");    // If the user return to the menu
    }

    /**
     * Store users who join the session in a list and notify them that their request has been received
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void getUsersUntilSessionStartOrBackToMenu() throws IOException {
        clients.add(clientCommunication);
        do{

        } while(!(clientCommunication.isReceiveMessageFromClient())); // While the multiplayer session's host doesn't click on the button 'Lancer'
    }

    /**
     * Run the session for all other users
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void executeUsers() throws IOException {
        // Send to all other users that the game begins
        for (ClientCommunication clientMultiplayerCommunication : clients) {
            clientMultiplayerCommunication.sendMessageToClient("BEGIN_FLAG");   // Notify the client that the session begin (he can read the quiz's data)
        }
        MultiplayerSessions.removeSession(sessionCode);
        System.out.println(MultiplayerSessions.getMultiplayerSessions().size());
    }

    public boolean isGameWillPlayed() throws IOException{
        getUsersUntilSessionStartOrBackToMenu();
        if(verifyBackToMenu()) {
            return false;
        }
        executeUsers();     // Run the session for all other users
        return true;
    }
}