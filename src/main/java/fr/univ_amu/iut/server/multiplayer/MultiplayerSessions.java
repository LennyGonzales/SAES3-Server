package fr.univ_amu.iut.server.multiplayer;

import java.util.HashMap;

/**
 * Stores multiplayer sessions
 * @author LennyGonzales
 */
public class MultiplayerSessions {
    private static HashMap<String, MultiplayerSession> multiplayerSessions = new HashMap<>();

    /**
     * Add a multiplayer session
     * @param sessionCode the multiplayer session code
     * @param multiplayerSession the multiplayer session
     */
    public static void addSession(String sessionCode, MultiplayerSession multiplayerSession) {
        multiplayerSessions.put(sessionCode, multiplayerSession);
    }

    /**
     * Get a multiplayer session with his session code
     * @param sessionCode the session code
     * @return the multiplayer session instance
     */
    public static MultiplayerSession getSessionWithSessionCode(String sessionCode) {
        return multiplayerSessions.get(sessionCode);
    }

    /**
     * Remove a multiplayer session
     * @param sessionCode his session code
     */
    public static void removeSession(String sessionCode) {
        multiplayerSessions.remove(sessionCode);
    }

    /**
     * Get multiplayer sessions and their code
     * @return multiplayer sessions in a HashMap
     */
    public static HashMap<String, MultiplayerSession> getMultiplayerSessions() {
        return multiplayerSessions;
    }

}
