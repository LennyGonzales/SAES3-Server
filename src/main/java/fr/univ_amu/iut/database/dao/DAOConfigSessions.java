package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.table.ConfigSessions;

import java.sql.SQLException;

public interface DAOConfigSessions extends DAO<ConfigSessions,Integer> {
    /**
     * verify if the input code is in the database
     * @param code
     * @return boolean
     */
    boolean isIn(String code) throws SQLException;

    /**
     * Find the port associated with a specific code
     * @param code
     * @return
     */
    int findPort(String code) throws SQLException;
}
