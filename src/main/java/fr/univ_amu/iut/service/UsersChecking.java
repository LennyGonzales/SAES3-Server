package fr.univ_amu.iut.service;

import fr.univ_amu.iut.service.dao.DAOUsers;

import java.sql.SQLException;

/**
 * Check all actions for users
 * @author LennyGonzales
 */
public class UsersChecking {

    /**
     * Check the user authentication
     * @param email email of the user
     * @param password password of the user
     * @param daoUsers interface (Reversing dependencies) to access database
     * @return if the authentication was successful
     * @throws SQLException
     */
    public boolean authenticate(String email, String password, DAOUsers daoUsers) throws SQLException {
        return (daoUsers.getUser(email, password) != null);
    }
}
