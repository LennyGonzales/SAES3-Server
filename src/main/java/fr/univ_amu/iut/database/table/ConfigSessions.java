package fr.univ_amu.iut.database.table;

/**
 * Represents a tuple of the CONFIGSESSIONS table
 */
public class ConfigSessions {
    private int port;
    private String code;

    public ConfigSessions(int port, String code) {
        this.port = port;
        this.code = code;
    }

    public int getPort() {
        return port;
    }

    public String getCode() {
        return code;
    }
}
