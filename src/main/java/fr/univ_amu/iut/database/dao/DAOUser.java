package fr.univ_amu.iut.database.dao;

import java.sql.SQLException;

public interface DAOUser extends DAO{
    /**
     * verify if the user's email is in the database
     * @param email
     * @return boolean
     */
    boolean isIn(String email, String password) throws SQLException;
}