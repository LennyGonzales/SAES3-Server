package fr.univ_amu.iut.server.module;

import fr.univ_amu.iut.database.dao.DAOModuleJDBC;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Supports the modules' actions
 * @author LennyGonzales
 */
public class Modules {
    private final DAOModuleJDBC daoStoriesJDBC;
    private final ClientCommunication clientCommunication;

    public Modules(ClientCommunication clientCommunication) throws SQLException {
        daoStoriesJDBC = new DAOModuleJDBC();
        this.clientCommunication = clientCommunication;
    }

    /**
     * Send modules to the user
     * @throws SQLException if the SQL request to get all the modules didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendModules() throws SQLException, IOException {
        clientCommunication.sendObjectToClient(daoStoriesJDBC.getAllModules());
    }

    /**
     * Get the module chosen by the user
     * @return the module chosen
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public String getModuleChoice() throws IOException {
        return clientCommunication.receiveMessageFromClient();
    }
}
