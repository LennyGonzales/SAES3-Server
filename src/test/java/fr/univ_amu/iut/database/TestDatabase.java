package fr.univ_amu.iut.database;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class TestDatabase {
    @Test
    public void should_initialize_bdd_link() {
        Database database = new Database();
        assertNotEquals(database.getConnection(),null);
        database.closeConnection();
    }

    @Test
    public void should_close_bdd_connection() {
        Database database = new Database();
        assertTrue(database.closeConnection());
    }
}
