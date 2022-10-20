package fr.univ_amu.iut.database;

import fr.univ_amu.iut.Main;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotEquals;

public class TestDatabase {
    @Test
    public void should_initialize_bdd_link() {
        Database database = new Database();
        assertNotEquals(database.getConnection(),null);
        try {
            database.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
