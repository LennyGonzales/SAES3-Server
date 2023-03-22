package fr.univ_amu.iut.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestDatabase {

    @Test
    public void should_initialize_bdd_users() throws SQLException {
        Database database = new Database();
        database.initConnections();
        assertNotEquals(database.getConnections().get("USERS"),null);
        database.closeConnections();
    }

    @Test
    public void should_initialize_bdd_stories() throws SQLException {
        Database database = new Database();
        database.initConnections();
        assertNotEquals(database.getConnections().get("STORIES"),null);
        database.closeConnections();
    }

    @Test
    public void should_close_bdd() {
        Database database = new Database();
        assertTrue(database.closeConnections());
    }

    @Test
    public void should_initialize_bdd_test_users() throws SQLException {
        Database database = new Database();
        Connection connection = database.initSingleConnection("TEST_USERS");
        assertNotEquals(database.initSingleConnection("TEST_USERS"), null);
        connection.close();
    }

    @Test
    public void should_initialize_bdd_test_stories() throws SQLException {
        Database database = new Database();
        Connection connection = database.initSingleConnection("TEST_STORIES");
        assertNotEquals(database.initSingleConnection("TEST_STORIES"), null);
        connection.close();
    }
}
