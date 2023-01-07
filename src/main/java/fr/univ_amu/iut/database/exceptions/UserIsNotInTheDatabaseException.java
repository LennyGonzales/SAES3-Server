package fr.univ_amu.iut.database.exceptions;

public class UserIsNotInTheDatabaseException extends Exception {
    public UserIsNotInTheDatabaseException(String email) {
        super(email + " isn't in the database");
    }
}
