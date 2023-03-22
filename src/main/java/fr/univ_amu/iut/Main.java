package fr.univ_amu.iut;

import fr.univ_amu.iut.database.Database;

/**
 * Class to run to launch the server
 * @author LennyGonzales
 */
public class Main {
    public static Database database;
    public static void main(String[] args) throws Exception {
        database = new Database();
        database.initConnections(); // Initialize the connections with the databases

        Server server = new Server();   // Initialize the server
        server.acceptClients();

        database.closeConnections(); //Close the connection with the database
    }
}
