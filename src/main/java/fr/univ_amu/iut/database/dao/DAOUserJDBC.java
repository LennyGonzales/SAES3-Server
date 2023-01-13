package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.exceptions.UserIsNotInTheDatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements methods' for the users table
 * @author LennyGonzales
 */
public class DAOUserJDBC implements DAOUser{
    private final PreparedStatement authenticationStatement;
    private final PreparedStatement getPointsByEmailStatement;
    private final PreparedStatement setPointsStatement;
    private final PreparedStatement verifyEmailStatement;
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOUserJDBC() throws SQLException {
        authenticationStatement = CONNECTION.prepareStatement("SELECT ID FROM USERS WHERE EMAIL = ? AND USER_PASSWORD = ?");
        getPointsByEmailStatement = CONNECTION.prepareStatement("SELECT POINTS FROM USERS WHERE EMAIL = ?");
        setPointsStatement = CONNECTION.prepareStatement("UPDATE USERS SET POINTS = ? WHERE EMAIL = ?");
        verifyEmailStatement = CONNECTION.prepareStatement("SELECT COUNT(*) FROM USERS WHERE EMAIL = ?");
    }

    /**
     * verify if the user's email is in the database
     * @param email the input email of the user
     * @param password the input password of the user
     * @return  true - the user is in the database | false - the user isn't in the database
     * @throws SQLException the SQL request didn't go well
     */
    public boolean authentication(String email, String password) throws SQLException {
        authenticationStatement.setString(1,email);
        authenticationStatement.setString(2,password);

        ResultSet result = authenticationStatement.executeQuery();
        return (result.next());
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
    public boolean verifyEmail(String email) throws SQLException, UserIsNotInTheDatabaseException {
        verifyEmailStatement.setString(1,email);
        ResultSet result = verifyEmailStatement.executeQuery();
        result.next();
        return (result.getInt(1) > 0);
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
