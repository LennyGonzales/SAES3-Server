package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOUserJDBC implements DAOUser{
    private PreparedStatement isInStatement;
    private static final Connection CONNECTION = Main.database.getConnection();

    public DAOUserJDBC() throws SQLException {
        isInStatement = CONNECTION.prepareStatement("SELECT ID FROM USERS WHERE EMAIL = ? AND USER_PASSWORD = ?");
    }

    /**
     * verify if the user's email is in the database
     * @param email
     * @return boolean
     */
    public boolean isIn(String email, String password) throws SQLException {
        isInStatement.setString(1,email);
        isInStatement.setString(2,password);
        ResultSet result = isInStatement.executeQuery();

        return (result.next());
    }

    @Override
    public boolean delete(Object obj) {
        return false;
    }

    @Override
    public Object insert(Object obj) {
        return null;
    }

    @Override
    public boolean update(Object obj) {
        return false;
    }
}
