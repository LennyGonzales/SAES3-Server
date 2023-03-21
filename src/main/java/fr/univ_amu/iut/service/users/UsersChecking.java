package fr.univ_amu.iut.service.users;

import fr.univ_amu.iut.domain.User;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.dao.DAOUsers;

import java.sql.SQLException;

/**
 * Check all actions for users
 * @author LennyGonzales
 */
public class UsersChecking {
    private User user;

    public User getUser() {
        return this.user;
    }

    /**
     * Check the user authentication
     * @param email email of the user
     * @param password password of the user
     * @param daoUsers interface (Reversing dependencies) to access database
     * @return if the authentication was successful
     * @throws SQLException if the getUser query didn't go well
     */
    public boolean authenticate(String email, String password, DAOUsers daoUsers) throws SQLException {
        User user = daoUsers.getUser(email, password);
        boolean isAuthenticated = (user != null);
        if(isAuthenticated) {
            this.user = user;
        }
        return isAuthenticated;
    }

    /**
     * Increase the user points
     * @param numberOfPoints the number of points
     * @param daoUsers interface (Reversing dependencies) to access database
     * @throws UserIsNotInTheDatabaseException if the user isn't in the database
     * @throws SQLException if one of the queries didn't go well
     */
    public void increaseUserPoints(int numberOfPoints, DAOUsers daoUsers) throws UserIsNotInTheDatabaseException, SQLException {
        daoUsers.setPointsByEmail(user.getEmail(), daoUsers.getPointsByEmail(user.getEmail()) + numberOfPoints);   // we also check if the user is still in the database
        user = daoUsers.getUser(user.getEmail(), user.getPassword()); // Update the instance
    }
}
