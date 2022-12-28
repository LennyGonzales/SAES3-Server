package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.dao.DAOHistoryJDBC;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.*;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Supports the multiplayer's actions (creation and join)
 */
public class Multiplayer {
    private final ClientCommunication clientCommunication;

    public Multiplayer(ClientCommunication clientCommunication) {
        this.clientCommunication = clientCommunication;
    }

    /**
     * Send modules to the session's host
     * @throws SQLException if the SQL request to get all the modules didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendModulesToTheHost() throws SQLException, IOException {
        DAOHistoryJDBC daoHistoryJDBC = new DAOHistoryJDBC();
        clientCommunication.sendObjectToClient(daoHistoryJDBC.getAllModules());
    }

    /**
     * Get the module chosen by the host
     * @return the module chosen
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public String getModuleChoice() throws IOException {
        return clientCommunication.receiveMessageFromClient();
    }

    /**
     * Create the session's code
     * @return the session's code
     */
    public String createCode() {
        return UUID.randomUUID().toString().substring(0,8);
    }

    /**
     * Send the code to the host
     * @param code the session's code
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendCode(String code) throws IOException {
        clientCommunication.sendMessageToClient("CODE_FLAG");
        clientCommunication.sendMessageToClient(code);
    }

    /**
     * Supports the creation of a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void createMultiplayerSession() throws IOException, SQLException {
        sendModulesToTheHost();
        String module = getModuleChoice();

        String code = createCode();
        sendCode(code);

        createSession(code, module);
    }

    /**
     * Create the multiplayer session
     * @param module the module chosen
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request didn't go well
     */
    public void createSession(String code, String module) throws IOException, SQLException {
        ServerMultiplayer serverMultiplayer = new ServerMultiplayer(code, module, clientCommunication);
        serverMultiplayer.run();
    }

    /**
     * Join a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void joinMultiplayerSession() throws IOException, SQLException {
        clientCommunication.sendMessageToClient("JOIN_SESSION");
        DAOConfigSessionsJDBC configSessionsJDBC = new DAOConfigSessionsJDBC();
        String message = clientCommunication.receiveMessageFromClient();
        if(configSessionsJDBC.isIn(message)) {  // Get the input code and ask if the code is in the database
            clientCommunication.sendMessageToClient("CODE_EXISTS_FLAG");
            clientCommunication.sendMessageToClient(Integer.toString(configSessionsJDBC.findPort(message)));    // Give the port
        } else {
            clientCommunication.sendMessageToClient("CODE_NOT_EXISTS_FLAG");
        }
    }
}
