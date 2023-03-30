
package fr.univ_amu.iut.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Supports the connection with the database and allows to close it
 * @author LennyGonzales
 */
public class Database {
    private static final String DATABASE_USERS = "USERS";
    private static final String DATABASE_STORIES = "STORIES";

    private static HashMap<String, Connection> connections;


    /**
     * Create the connection with the database
     */
    public Database() {
        connections = new HashMap<>();
    }

    /**
     * Initialize the connections with the databases
     * @throws SQLException if the connection didn't go well
     */
    public void initConnections() throws SQLException {
        connections.put(DATABASE_USERS, initSingleConnection(DATABASE_USERS));
        connections.put(DATABASE_STORIES, initSingleConnection(DATABASE_STORIES));
    }

    /**
     * Initialize a connection with a database
     * @param databaseName the name of the database
     * @return the connection with the database
     * @throws SQLException if the connection didn't go well
     */
    public Connection initSingleConnection(String databaseName) throws SQLException {
        return DriverManager.getConnection(
                System.getenv("DB_" + databaseName + "_URL"),
                System.getenv("DB_LOGIN"),
                System.getenv("DB_PASSWORD")
        );
    }

    /**
     * Return the connection with the database
     * @return the connection
     */
    public HashMap<String, Connection> getConnections() {
        return connections;
    }

    /**
     *  Close the connections with the databases
     * @return true if the disconnection worked
     *         false if the disconnection didn't work
     */
    public static boolean closeConnections() {
        connections.values().forEach(connection ->
        {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }
}