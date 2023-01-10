package fr.univ_amu.iut.server.multiplayer;

import java.util.HashMap;

public class MultiplayerSessions {
    private static HashMap<String, MultiplayerSession> multiplayerSessions = new HashMap<>();
    public static void addSession(String sessionCode, MultiplayerSession multiplayerSession) {
        multiplayerSessions.put(sessionCode, multiplayerSession);
    }

    public static MultiplayerSession getSession(String sessionCode) {
        return multiplayerSessions.get(sessionCode);
    }

    public static void removeSession(String sessionCode) {
        multiplayerSessions.remove(sessionCode);
    }

    public static HashMap<String, MultiplayerSession> getMultiplayerSessions() {
        return multiplayerSessions;
    }

}
