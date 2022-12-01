package fr.univ_amu.iut.database.table;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class TestConfigSessions {
    @Test
    public void shouldGetPort() {
        ConfigSessions configSessions = new ConfigSessions(15273, "aaaaaaaa");
        assertEquals(configSessions.getPort(),15273);
    }

    @Test
    public void shouldGetCode() {
        ConfigSessions configSessions = new ConfigSessions(15273, "aaaaaaaa");
        assertEquals(configSessions.getCode(),"aaaaaaaa");
    }
}
