package fr.univ_amu.iut.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Supports the connection with the database and allows to close it
 * @author LennyGonzales
 */
public class Database {
    private static final String URL = "jdbc:postgresql://peanut.db.elephantsql.com/lhnhqbrm";
    private static final String LOGIN = "lhnhqbrm";
    private static final String PASSWORD = "KWjwsNe0T5pXCtXHh5M3tXy_ppanNir4";
    private static Connection connection;

    /**
     * Create the connection with the database
     */
    public Database() {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the connection with the database
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     *  Close the connection with the database
     *
     * @return true if the disconnection worked
     *         false if the disconnection didn't work
     */
    public static boolean closeConnection() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}