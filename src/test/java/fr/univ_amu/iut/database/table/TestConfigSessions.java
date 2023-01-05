package fr.univ_amu.iut.database.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class TestConfigSessions {
    private ConfigSessions configSessions;
    @BeforeEach
    public void beforeEachTest() {
        configSessions = new ConfigSessions(15273, "aaaaaaaa");
    }
    @Test
    public void shouldGetPort() {
        assertEquals(configSessions.getPort(),15273);
    }

    @Test
    public void shouldGetCode() {
        assertEquals(configSessions.getCode(),"aaaaaaaa");
    }
}
