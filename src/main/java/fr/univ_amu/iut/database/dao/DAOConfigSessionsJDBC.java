package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.ConfigSessions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOConfigSessionsJDBC implements DAOConfigSessions{

    private PreparedStatement insertStatement;  // SQL query to insert a tuple
    private PreparedStatement deleteStatement;  // SQL query to delete a tuple
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor
     * @throws SQLException
     */
    public DAOConfigSessionsJDBC() throws SQLException {
        insertStatement = CONNECTION.prepareStatement("INSERT INTO CONFIGSESSIONS VALUES (?, ?);");
        deleteStatement = CONNECTION.prepareStatement("DELETE FROM CONFIGSESSIONS WHERE PORT = ?;");
    }

    /**
     *
     * @param configSessions Objet à supprimer dans la base
     * @return
     */
    @Override
    public boolean delete(ConfigSessions configSessions) throws SQLException {
        deleteStatement.setInt(1, configSessions.getPort());
        deleteStatement.executeUpdate();
        return true;
    }

    /**
     *
     * @param configSessions Objet à insérer dans la base
     * @return
     * @throws SQLException
     */
    @Override
    public ConfigSessions insert(ConfigSessions configSessions) throws SQLException {
        insertStatement.setInt(1, configSessions.getPort());
        insertStatement.setString(2, configSessions.getCode());

        insertStatement.executeUpdate();
        return configSessions;
    }

    /**
     *
     * @param configSessions Objet à mettre à jour dans la base
     * @return
     */
    @Override
    public boolean update(ConfigSessions configSessions) {
        return false;
    }
}
