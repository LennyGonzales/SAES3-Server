package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.Database;
import fr.univ_amu.iut.domain.User;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.dao.DAOUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestDAOUsers {
    private DAOUsers daoUser;
    private static Connection connection;
    private Database database;

    @BeforeEach
    public void beforeEachTest() throws SQLException {
        if(connection == null) {
            database = new Database();
            connection = database.initSingleConnection("TEST_USERS");
        }
        daoUser = new DAOUsersJDBC(connection);
    }

    @Test
    public void shouldIsIn() throws SQLException {
        assertNotEquals(null, daoUser.getUser("lenny.gonzales@etu.univ-amu.fr", "b9d38d92d629d3b3cbedaec45b9e592f985a30f8ab959cb92a1722656df62d32e040bc6cb31b09f08bd033dd5b5993a94fb07ad73bad1f86c5f1cc853183c073"));
    }

    @Test
    public void shouldIsNotIn() throws SQLException {
        assertEquals(null, daoUser.getUser("randomEmail", "randomPassword"));
    }

    @Test
    public void shouldThrowUserIsNotInTheDatabaseExceptionByCallingGetPointsByEmail() {
        String email = "emailNotInTheDatabase";
        assertThrows(UserIsNotInTheDatabaseException.class, () -> daoUser.getPointsByEmail(email));
    }

    @Test
    public void shouldThrowUserIsNotInTheDatabaseExceptionByCallingSetPointsByEmail() {
        String email = "emailNotInTheDatabase";
        assertThrows(UserIsNotInTheDatabaseException.class, () -> daoUser.setPointsByEmail(email, 100));
    }

    @Test
    public void shouldEmailInTheDatabase() throws UserIsNotInTheDatabaseException, SQLException {
        String email = "lenny.gonzales@etu.univ-amu.fr";
        assertTrue(daoUser.verifyEmail(email));
    }

    @Test
    public void shouldEmailIsNotInTheDatabase() throws UserIsNotInTheDatabaseException, SQLException {
        String email = "emailNotInTheDatabase";
        assertFalse(daoUser.verifyEmail(email));
    }

    @Test
    void delete() throws SQLException {
        User user = new User();
        boolean result = daoUser.delete(user);
        assertFalse(result);
    }

    @Test
    void insert() throws SQLException {
        User user = new User();
        User result = daoUser.insert(user);
        assertNull(result);
    }

    @Test
    void update() {
        User user = new User();
        boolean result = daoUser.update(user);
        assertFalse(result);
    }
}
