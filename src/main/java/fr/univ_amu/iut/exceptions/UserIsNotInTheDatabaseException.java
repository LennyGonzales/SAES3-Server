package fr.univ_amu.iut.exceptions;

/**
 * If the user isn't in the database
 * @author LennyGonzales
 */
public class UserIsNotInTheDatabaseException extends Exception {
    public UserIsNotInTheDatabaseException(String email) {
        super(email + " isn't in the database");
    }
}
