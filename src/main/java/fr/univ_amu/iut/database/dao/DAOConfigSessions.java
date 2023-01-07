package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.table.ConfigSessions;

import java.sql.SQLException;

/**
 * The methods' signature for the ConfigSessions table
 */
public interface DAOConfigSessions extends DAO<ConfigSessions,Integer> {
    /**
     * Verify if the input code (code of a session) is in the database
     * @param code The code of a session
     * @return true - The code is in the database (The session exists) | false - The code isn't in the database
     * @throws SQLException if the request didn't go well
     */
    boolean isIn(String code) throws SQLException;

    /**
     * Find the port associated with a specific code
     * @param code The code of a session
     * @return the port associated to a session (session's code)
     * @throws SQLException if the request didn't go well
     */
    int findPort(String code) throws SQLException;
}
