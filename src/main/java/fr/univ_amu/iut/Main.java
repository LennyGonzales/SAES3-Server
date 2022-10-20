package fr.univ_amu.iut;

import fr.univ_amu.iut.database.Database;
import fr.univ_amu.iut.server.Server;

public class Main {
    public static Database database;
    public static void main(String[] args) throws Exception {
        database = new Database();

        Server server = new Server();
        server.run();

        database.closeConnection();
    }
}
