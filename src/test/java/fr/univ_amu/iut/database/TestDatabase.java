package fr.univ_amu.iut.database;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class TestDatabase {

    @Test
    public void should_initialize_bdd_users() {
        Database database = new Database();
        assertNotEquals(database.getConnections().get("USERS"),null);
        database.closeConnections();
    }

    @Test
    public void should_initialize_bdd_stories() {
        Database database = new Database();
        assertNotEquals(database.getConnections().get("STORIES"),null);
        database.closeConnections();
    }

    @Test
    public void should_close_bdd() {
        Database database = new Database();
        assertTrue(database.closeConnections());
    }
}
