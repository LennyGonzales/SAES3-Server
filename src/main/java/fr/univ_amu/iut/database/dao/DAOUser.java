package fr.univ_amu.iut.database.dao;

import java.sql.SQLException;

/**
 * The methods' signature for the users table
 */
public interface DAOUser extends DAO{
    /**
     * verify if the user's email is in the database
     * @param email the input email of the user
     * @param password the input password of the user
     * @return  true - the user is in the database | false - the user isn't in the database
     * @throws SQLException the SQL request didn't go well
     */
    boolean isIn(String email, String password) throws SQLException;
}