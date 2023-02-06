
package fr.univ_amu.iut.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Supports the connection with the database and allows to close it
 * @author LennyGonzales
 */
public class Database {
    private static final String DATABASE_OLD = "OLD";
    private static Connection connection;

    /**
     * Create the connection with the database
     */
    public Database() {
        try {
            initConnections();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initConnections() throws SQLException {
        Properties env = new Properties();
        try {
            env.load(new FileInputStream("databaseCredentials.env"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initSingleConnection(env, DATABASE_OLD);
    }

    public void initSingleConnection(Properties env, String databaseName) throws SQLException {
        connection = DriverManager.getConnection(
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