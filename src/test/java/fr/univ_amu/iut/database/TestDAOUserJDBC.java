package fr.univ_amu.iut.database;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.dao.DAOUserJDBC;
import fr.univ_amu.iut.database.exceptions.UserIsNotInTheDatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestDAOUserJDBC {
    private DAOUserJDBC daoUserJDBC;

    @BeforeEach
    public void beforeEachTest() throws SQLException {
        if(Main.database == null) { Main.database = new Database(); }
        daoUserJDBC = new DAOUserJDBC();
    }

    @Test
    public void shouldIsIn() throws SQLException {
        assertEquals(true, daoUserJDBC.authentication("lenny.gonzales@etu.univ-amu.fr", "30dbc25af96ed2343b0110580f12c4d34645b6b4ac0fd6a011ceeef8a6f76126726139c29022292f186d112c59a4c42ad75b92a6e987016cfe4a9de431b5e320"));
    }

    @Test
    public void shouldIsNotIn() throws SQLException {
        assertEquals(false, daoUserJDBC.authentication("randomEmail", "randomPassword"));
    }

    @Test
    public void shouldThrowUserIsNotInTheDatabaseExceptionByCallingGetPointsByEmail() {
        String email = "emailNotInTheDatabase";
        assertThrows(UserIsNotInTheDatabaseException.class, () -> daoUserJDBC.getPointsByEmail(email));
    }

    @Test
    public void shouldThrowUserIsNotInTheDatabaseExceptionByCallingSetPointsByEmail() {
        String email = "emailNotInTheDatabase";
        assertThrows(UserIsNotInTheDatabaseException.class, () -> daoUserJDBC.setPointsByEmail(email, 100));
    }

    @Test
    public void shouldEmailInTheDatabase() throws UserIsNotInTheDatabaseException, SQLException {
        String email = "lenny.gonzales@etu.univ-amu.fr";
        assertTrue(daoUserJDBC.verifyEmail(email));
    }

    @Test
    public void shouldEmailIsNotInTheDatabase() throws UserIsNotInTheDatabaseException, SQLException {
        String email = "emailNotInTheDatabase";
        assertFalse(daoUserJDBC.verifyEmail(email));
    }
}
