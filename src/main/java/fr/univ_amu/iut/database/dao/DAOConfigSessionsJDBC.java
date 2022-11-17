package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.ConfigSessions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOConfigSessionsJDBC implements DAOConfigSessions{

    private final PreparedStatement insertStatement;  // SQL query to insert a tuple
    private final PreparedStatement deleteStatement;  // SQL query to delete a tuple
    private final PreparedStatement isInStatement;    // SQL query to know if the input code is in the database
    private final PreparedStatement findPortStatement;    // SQL query to find the port with a specific code
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor
     * @throws SQLException
     */
    public DAOConfigSessionsJDBC() throws SQLException {
        insertStatement = CONNECTION.prepareStatement("INSERT INTO CONFIGSESSIONS VALUES (?, ?);");
        deleteStatement = CONNECTION.prepareStatement("DELETE FROM CONFIGSESSIONS WHERE PORT = ?;");
        isInStatement = CONNECTION.prepareStatement("SELECT * FROM CONFIGSESSIONS WHERE CODE = ?;");
        findPortStatement = CONNECTION.prepareStatement("SELECT PORT FROM CONFIGSESSIONS WHERE CODE = ?;");
    }

    /**
     * verify if the input code is in the database
     * @param code
     * @return boolean
     */
    @Override
    public boolean isIn(String code) throws SQLException {
        isInStatement.setString(1,code);
        ResultSet res = isInStatement.executeQuery();

        return (res.next());
    }

    /**
     * Find the port associated with a specific code
     * @param code
     * @return
     */
    @Override
    public int findPort(String code) throws SQLException {
        findPortStatement.setString(1,code);
        ResultSet res = isInStatement.executeQuery();

        res.next();
        return res.getInt(1);
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
