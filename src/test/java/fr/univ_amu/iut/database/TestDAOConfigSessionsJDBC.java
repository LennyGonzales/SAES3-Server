package fr.univ_amu.iut.database;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.table.ConfigSessions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class TestDAOConfigSessionsJDBC {
    private DAOConfigSessionsJDBC daoConfigSessionsJDBC;

    @BeforeEach
    public void beforeEachTest() throws SQLException {
        Main.database = new Database();
        daoConfigSessionsJDBC = new DAOConfigSessionsJDBC();
    }

    @Test
    public void shouldIsNotIn() throws SQLException {
        assertEquals(false, daoConfigSessionsJDBC.isIn("aubroiaberuioaber"));
    }

    @Test
    public void shouldInsertAndDelete() throws SQLException {
        ConfigSessions configSessions = new ConfigSessions(20000, "azerqsdf");
        assertEquals(configSessions,daoConfigSessionsJDBC.insert(configSessions));
        daoConfigSessionsJDBC.delete(configSessions);
        assertEquals(false, daoConfigSessionsJDBC.isIn("azerqsdf"));
    }

    @Test
    public void shouldFindPort() throws SQLException {
        ConfigSessions configSessions = new ConfigSessions(20000, "azerqsdf");
        daoConfigSessionsJDBC.insert(configSessions);
        assertEquals(20000, daoConfigSessionsJDBC.findPort("azerqsdf"));
        daoConfigSessionsJDBC.delete(configSessions);
    }
}
