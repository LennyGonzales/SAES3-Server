package fr.univ_amu.iut.service;

import fr.univ_amu.iut.service.dao.DAOUsers;

import java.sql.SQLException;

public class UsersChecking {

    public boolean authenticate(String email, String password, DAOUsers daoUsers) throws SQLException {
        return (daoUsers.getUser(email, password) != null);
    }
}
