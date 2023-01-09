package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.dao.DAOHistoryJDBC;
import fr.univ_amu.iut.server.ClientCommunication;
import fr.univ_amu.iut.server.module.Modules;

import java.io.*;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Supports the multiplayer's actions (creation and join)
 */
public class Multiplayer {
    private final ClientCommunication clientCommunication;
    private Modules modules;

    public Multiplayer(ClientCommunication clientCommunication) throws SQLException {
        this.clientCommunication = clientCommunication;
        modules = new Modules(clientCommunication);
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
    public void createMultiplayerSession() throws IOException, SQLException, ClassNotFoundException {
        modules.sendModulesToTheHost();
        String choice = modules.getModuleChoice();
        if(!(choice.equals("BACK_TO_MENU_FLAG"))) {
            String code = createCode();
            sendCode(code);

            createSession(code, choice);
        }
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
    public void joinMultiplayerSession() throws IOException, SQLException, ClassNotFoundException {
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
