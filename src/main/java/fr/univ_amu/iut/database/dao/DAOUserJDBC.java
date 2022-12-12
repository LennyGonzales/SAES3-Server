package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements methods' for the users table
 */
public class DAOUserJDBC implements DAOUser{
    private final PreparedStatement isInStatement;
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOUserJDBC() throws SQLException {
        isInStatement = CONNECTION.prepareStatement("SELECT ID FROM USERS WHERE EMAIL = ? AND USER_PASSWORD = ?");
    }

    /**
     * verify if the user's email is in the database
     * @param email the input email of the user
     * @param password the input password of the user
     * @return  true - the user is in the database | false - the user isn't in the database
     * @throws SQLException the SQL request didn't go well
     */
    public boolean isIn(String email, String password) throws SQLException {
        isInStatement.setString(1,email);
        isInStatement.setString(2,password);

        ResultSet result = isInStatement.executeQuery();
        return (result.next());
    }

    /**
     * Allows removal of a tuple from the base
     * @param obj tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     */
    @Override
    public boolean delete(Object obj) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param obj tuple to insert into the database
     * @return the tuple inserted
     */
    @Override
    public Object insert(Object obj) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param obj tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(Object obj) {
        return false;
    }
}
