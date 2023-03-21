package fr.univ_amu.iut.service.multiplayer;

import fr.univ_amu.iut.domain.MultiplayerSession;

import java.util.HashMap;

/**
 * Stores multiplayer sessions
 * @author LennyGonzales
 */
public class MultiplayerSessionsManager {
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
     * @param multiplayerSession the multiplayer session
     */
    public static void removeSession(MultiplayerSession multiplayerSession) {
        multiplayerSessions.values().remove(multiplayerSession);
    }
}
