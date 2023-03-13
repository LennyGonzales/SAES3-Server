package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.domain.Story;
import fr.univ_amu.iut.service.dao.DAOModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods' for the Stories table
 * @author LennyGonzales
 */
public class DAOModuleJDBC implements DAOModule {
    private final PreparedStatement getAllModulesStatement;
    private static final Connection CONNECTION = Main.database.getConnections().get("STORIES");

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOModuleJDBC() throws SQLException {
        getAllModulesStatement = CONNECTION.prepareStatement("SELECT DISTINCT MODULE FROM WRITTENRESPONSEQUESTIONS UNION SELECT DISTINCT module FROM MULTIPLECHOICEQUESTIONS;");
    }

    /**
     * Get all the modules
     * @return all the modules
     * @throws SQLException the SQL request didn't go well
     */
    @Override
    public List<String> getAllModules() throws SQLException {
        ResultSet result = getAllModulesStatement.executeQuery();

        List<String> modules = new ArrayList<>();
        while(result.next()) {
            modules.add(result.getString(1));
        }
        return modules;
    }

    /**
     * Allows removal of a tuple from the base
     * @param story tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     */
    @Override
    public boolean delete(Story story) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param story tuple to insert into the database
     * @return the tuple inserted
     */
    @Override
    public Story insert(Story story) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param story tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(Story story) {
        return false;
    }
}
