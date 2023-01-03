package fr.univ_amu.iut.server.module;

import fr.univ_amu.iut.database.dao.DAOHistoryJDBC;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Supports the modules' actions (send and get)
 */
public class Modules {
    private DAOHistoryJDBC daoHistoryJDBC;
    private ClientCommunication clientCommunication;

    public Modules(ClientCommunication clientCommunication) throws SQLException {
        daoHistoryJDBC = new DAOHistoryJDBC();
        this.clientCommunication = clientCommunication;
    }

    /**
     * Send modules to the session's host
     * @throws SQLException if the SQL request to get all the modules didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendModulesToTheHost() throws SQLException, IOException {
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
}
