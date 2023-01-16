package fr.univ_amu.iut.database;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.dao.DAOUsersJDBC;
import fr.univ_amu.iut.database.exceptions.UserIsNotInTheDatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestDAOUsersJDBC {
    private DAOUsersJDBC daoUserJDBC;

    @BeforeEach
    public void beforeEachTest() throws SQLException {
        if(Main.database == null) { Main.database = new Database(); }
        daoUserJDBC = new DAOUsersJDBC();
    }

    @Test
    public void shouldIsIn() throws SQLException {
        assertEquals(true, daoUserJDBC.authentication("lenny.gonzales@etu.univ-amu.fr", "b9d38d92d629d3b3cbedaec45b9e592f985a30f8ab959cb92a1722656df62d32e040bc6cb31b09f08bd033dd5b5993a94fb07ad73bad1f86c5f1cc853183c073"));
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
