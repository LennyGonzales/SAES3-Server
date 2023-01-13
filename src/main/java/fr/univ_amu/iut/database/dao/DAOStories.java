package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.table.Story;

import java.sql.SQLException;
import java.util.List;

/**
 * The methods' signature for the Stories table
 * @author LennyGonzales
 */
public interface DAOStories extends DAO<Story,Integer> {

    /**
     * Get all the modules
     * @return all the modules
     * @throws SQLException the SQL request didn't go well
     */
    List<String> getAllModules() throws SQLException;
}
