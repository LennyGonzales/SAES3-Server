
package fr.univ_amu.iut.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

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
        try {
            initConnections();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize the connections with the databases
     * @throws SQLException if the connection didn't go well
     */
    public void initConnections() throws SQLException {
        // Read the .env file
        Properties env = new Properties();
        try {
            env.load(new FileInputStream("databaseCredentials.env"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // init the connections
        connections.put(DATABASE_USERS, initSingleConnection(env, DATABASE_USERS));
        connections.put(DATABASE_STORIES, initSingleConnection(env, DATABASE_STORIES));
    }

    /**
     * Initialize a connection with a database
     * @param env the env properties
     * @param databaseName the name of the database
     * @return the connection with the database
     * @throws SQLException if the connection didn't go well
     */
    public Connection initSingleConnection(Properties env, String databaseName) throws SQLException {
        return DriverManager.getConnection(
                env.getProperty("DB_" + databaseName + "_URL"),
                env.getProperty("DB_" + databaseName + "_LOGIN"),
                env.getProperty("DB_" + databaseName + "_PASSWORD")
        );
    }

    /**
     * Return the connection with the database
     *
     * @return the connection
     */
    public HashMap<String, Connection> getConnections() {
        return connections;
    }

    /**
     *  Close the connections with the databases
     *
     * @return true if the disconnection worked
     *         false if the disconnection didn't work
     */
    public static boolean closeConnection() {
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