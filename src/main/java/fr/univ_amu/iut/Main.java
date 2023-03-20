package fr.univ_amu.iut;

import fr.univ_amu.iut.database.Database;
import fr.univ_amu.iut.database.dao.DAOQuestionsJDBC;

import java.util.Arrays;

/**
 * Class to run to launch the server
 * @author LennyGonzales
 */
public class Main {
    public static Database database;
    public static void main(String[] args) throws Exception {
        database = new Database();  // Initialize the connection with the database

        Server server = new Server();   // Initialize the server
        server.acceptClients();

        database.closeConnections(); //Close the connection with the database
    }
}
