package fr.univ_amu.iut.control;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.database.dao.DAOUsers;
import fr.univ_amu.iut.database.dao.DAOUsersJDBC;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Controllers {
    private Communication communication;
    private DAOUsers daoUsers;  // interface (Reversing dependencies)

    public Controllers(Communication communication, DAOUsers daoUsers) {
        this.communication = communication;
        this.daoUsers = daoUsers;
    }

    /**
     * Control the login
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public boolean loginAction(List<String> credentials) throws IOException, SQLException {
        if(daoUsers.authentication(credentials.get(0), credentials.get(1))) {
            communication.sendMessage(new CommunicationFormat(Flags.LOGIN_SUCCESSFULLY));
            return true;
        }
        communication.sendMessage(new CommunicationFormat(Flags.LOGIN_NOT_SUCCESSFULLY));
        return false;
    }
}
