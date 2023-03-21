package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.domain.User;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.dao.DAOUsers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements methods' for the Users table
 * @author LennyGonzales
 */
public class DAOUsersJDBC implements DAOUsers {
    private final PreparedStatement authenticationStatement;
    private final PreparedStatement getPointsByEmailStatement;
    private final PreparedStatement setPointsStatement;
    private final PreparedStatement verifyEmailStatement;
    private static final Connection CONNECTION = Main.database.getConnections().get("USERS");

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOUsersJDBC() throws SQLException {
        authenticationStatement = CONNECTION.prepareStatement("SELECT EMAIL, USER_PASSWORD, USER_STATUS, POINTS FROM USERS WHERE EMAIL = ? AND USER_PASSWORD = ?");
        getPointsByEmailStatement = CONNECTION.prepareStatement("SELECT POINTS FROM USERS WHERE EMAIL = ?");
        setPointsStatement = CONNECTION.prepareStatement("UPDATE USERS SET POINTS = ? WHERE EMAIL = ?");
        verifyEmailStatement = CONNECTION.prepareStatement("SELECT COUNT(*) FROM USERS WHERE EMAIL = ?");
    }

    /**
     * Get the user by email and password
     * @param email the input email of the user
     * @param password the input password of the user
     * @return an instance of User or null
     * @throws SQLException the SQL request didn't go well
     */
    public User getUser(String email, String password) throws SQLException {
        authenticationStatement.setString(1,email);
        authenticationStatement.setString(2,password);

        ResultSet result = authenticationStatement.executeQuery();
        if(result.next()) {
            User user = new User();
            user.setEmail(result.getString(1));
            user.setPassword(result.getString(2));
            user.setUserStatus(result.getString(3));
            user.setPoints(result.getInt(4));
            return user;
        }
        return null;
    }

    /**
     * Get the points of a user by his email
     * @param email the email of the user
     * @return the points of the user
     * @throws SQLException the SQL request didn't go well
     * @throws UserIsNotInTheDatabaseException if the user isn't in the database
     */
    @Override
    public int getPointsByEmail(String email) throws SQLException, UserIsNotInTheDatabaseException {
        if(verifyEmail(email)) {
            getPointsByEmailStatement.setString(1, email);

            ResultSet result = getPointsByEmailStatement.executeQuery();
            result.next();
            return result.getInt(1);
        }
        throw new UserIsNotInTheDatabaseException(email);
    }

    /**
     * Set the points of a user
     * @param email the email of the user
     * @param newUserPoints the new points of the user
     * @throws SQLException the SQL request didn't go well
     */
    @Override
    public void setPointsByEmail(String email, int newUserPoints) throws SQLException, UserIsNotInTheDatabaseException {
        if(!verifyEmail(email)) {
            throw new UserIsNotInTheDatabaseException(email);
        }
        setPointsStatement.setInt(1, newUserPoints);
        setPointsStatement.setString(2, email);

        setPointsStatement.executeUpdate();
    }

    /**
     * Verify if an email is in the database
     * @param email the email to verify
     * @return true - if the email is in the database | else, return false
     * @throws SQLException if the SQL request didn't go well
     */
    @Override
    public boolean verifyEmail(String email) throws SQLException {
        verifyEmailStatement.setString(1,email);
        ResultSet result = verifyEmailStatement.executeQuery();
        result.next();
        return (result.getInt(1) > 0);
    }



    /**
     * Allows removal of a tuple from the base
     * @param user tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     */
    @Override
    public boolean delete(User user) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param user tuple to insert into the database
     * @return the tuple inserted
     */
    @Override
    public User insert(User user) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param user tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(User user) {
        return false;
    }
}
