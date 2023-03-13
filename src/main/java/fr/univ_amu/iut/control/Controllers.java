package fr.univ_amu.iut.control;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.service.UsersChecking;
import fr.univ_amu.iut.service.dao.DAOUsers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Controllers {
    private Communication communication;

    public Controllers(Communication communication) {
        this.communication = communication;
    }

    /**
     * Control the login
     * @param credentials a list containing email and password
     * @param usersChecking a instance of UsersChecking
     * @param daoUsers interface (Reversing dependencies)
     * @return if the login was successful
     * @throws IOException  if the communication with the client is closed or didn't go well
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public boolean loginAction(List<String> credentials, UsersChecking usersChecking, DAOUsers daoUsers) throws IOException, SQLException {
        if(usersChecking.authenticate(credentials.get(0), credentials.get(1), daoUsers)) {
            communication.sendMessage(new CommunicationFormat(Flags.LOGIN_SUCCESSFULLY));
            return true;
        }
        communication.sendMessage(new CommunicationFormat(Flags.LOGIN_NOT_SUCCESSFULLY));
        return false;
    }

}
