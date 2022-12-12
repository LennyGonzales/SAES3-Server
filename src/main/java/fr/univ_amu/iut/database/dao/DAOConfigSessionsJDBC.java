package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.ConfigSessions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements methods' for the ConfigSessions table
 */
public class DAOConfigSessionsJDBC implements DAOConfigSessions{

    private final PreparedStatement insertStatement;  // SQL query to insert a tuple
    private final PreparedStatement deleteStatement;  // SQL query to delete a tuple
    private final PreparedStatement isInStatement;    // SQL query to know if the input code is in the database
    private final PreparedStatement findPortStatement;    // SQL query to find the port with a specific code
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOConfigSessionsJDBC() throws SQLException {
        insertStatement = CONNECTION.prepareStatement("INSERT INTO CONFIGSESSIONS VALUES (?, ?);");
        deleteStatement = CONNECTION.prepareStatement("DELETE FROM CONFIGSESSIONS WHERE PORT = ?;");
        isInStatement = CONNECTION.prepareStatement("SELECT * FROM CONFIGSESSIONS WHERE CODE = ?;");
        findPortStatement = CONNECTION.prepareStatement("SELECT PORT FROM CONFIGSESSIONS WHERE CODE = ?;");
    }

    /**
     * Verify if the input code (code of a session) is in the database
     * @param code The code of a session
     * @return true - The code is in the database (The session exists) | false - The code isn't in the database
     * @throws SQLException if the request didn't go well
     */
    @Override
    public boolean isIn(String code) throws SQLException {
        isInStatement.setString(1,code);
        ResultSet res = isInStatement.executeQuery();

        return (res.next());
    }

    /**
     * Find the port associated with a specific code
     * @param code The code of a session
     * @return the port associated to a session (session's code)
     * @throws SQLException if the request didn't go well
     */
    @Override
    public int findPort(String code) throws SQLException {
        findPortStatement.setString(1,code);
        ResultSet res = isInStatement.executeQuery();

        res.next();
        return res.getInt(1);
    }

    /**
     * Allows removal of a tuple from the base
     * @param configSessions tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     * @throws SQLException if the deletion didn't go well
     */
    @Override
    public boolean delete(ConfigSessions configSessions) throws SQLException {
        deleteStatement.setInt(1, configSessions.getPort());
        deleteStatement.executeUpdate();
        return true;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param configSessions tuple to insert into the database
     * @return the tuple inserted
     * @throws SQLException if the insertion didn't go well
     */
    @Override
    public ConfigSessions insert(ConfigSessions configSessions) throws SQLException {
        insertStatement.setInt(1, configSessions.getPort());
        insertStatement.setString(2, configSessions.getCode());

        insertStatement.executeUpdate();
        return configSessions;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param configSessions tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(ConfigSessions configSessions) {
        return false;
    }
}
