package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.History;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods' for the History table
 * @author LennyGonzales
 */
public class DAOHistoryJDBC implements DAOHistory{
    private final PreparedStatement getAllModulesStatement;
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOHistoryJDBC() throws SQLException {
        getAllModulesStatement = CONNECTION.prepareStatement("SELECT DISTINCT MODULE FROM HISTORY;");
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
     * @param history tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     */
    @Override
    public boolean delete(History history) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param history tuple to insert into the database
     * @return the tuple inserted
     */
    @Override
    public History insert(History history) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param history tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(History history) {
        return false;
    }
}
