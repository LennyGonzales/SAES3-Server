package fr.univ_amu.iut.server.questions;

import fr.univ_amu.iut.database.dao.DAOUserJDBC;
import fr.univ_amu.iut.database.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Summary {
    private HashMap<String, Boolean> summaryHashMap;
    private ClientCommunication clientCommunication;

    public Summary(ClientCommunication clientCommunication, HashMap<String, Boolean> summaryHashMap) {
        this.clientCommunication = clientCommunication;
        this.summaryHashMap = summaryHashMap;
    }

    public int getNumberOfCorrectAnswers() {
        return (int) summaryHashMap.values().stream().filter(Boolean::booleanValue).count();
    }

    /**
     * Change the points of the user and send it to him
     * @throws SQLException if the SQL request didn't go well
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws UserIsNotInTheDatabaseException If the user isn't in the database
     */
    public void changeUserPoints() throws SQLException, IOException, UserIsNotInTheDatabaseException, ClassNotFoundException {
        DAOUserJDBC daoUserJDBC = new DAOUserJDBC();
        String email = clientCommunication.receiveMessageFromClient();
        int userPoints = daoUserJDBC.getPointsByEmail(email);
        userPoints += 10 * (getNumberOfCorrectAnswers() - (summaryHashMap.size()/2.0)) * (1 - (userPoints / 2000.0));   // function to calculate the new user points
        daoUserJDBC.setPointsByEmail(email, userPoints);

        clientCommunication.sendMessageToClient(Integer.toString(userPoints));
    }

    public void sendSummary() throws IOException {
        clientCommunication.sendObjectToClient(summaryHashMap);
    }

    public void initialize() throws UserIsNotInTheDatabaseException, SQLException, IOException, ClassNotFoundException {
        changeUserPoints();
        sendSummary();
    }
}
