package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.table.History;

import java.sql.SQLException;
import java.util.List;

/**
 * The methods' signature for the ConfigSessions table
 */
public interface DAOHistory extends DAO<History,Integer> {

    /**
     * Get all the modules
     * @return all the modules
     * @throws SQLException the SQL request didn't go well
     */
    List<String> getAllModules() throws SQLException;
}
