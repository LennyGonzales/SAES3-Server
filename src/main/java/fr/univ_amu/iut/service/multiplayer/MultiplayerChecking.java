package fr.univ_amu.iut.service.multiplayer;

import fr.univ_amu.iut.domain.MultiplayerSession;

/**
 * Supports the current multiplayer session management for the user (know if there is a multiplayer session now, or set a multiplayer session)
 * @author LennyGonzales
 */
public class MultiplayerChecking {
    private MultiplayerSession currentMultiplayerSession;

    /**
     * Get the current multiplayer session
     * @return the current multiplayer session
     */
    public MultiplayerSession getCurrentMultiplayerSession() {
        return currentMultiplayerSession;
    }

    /**
     * Set the multiplayer session
     * @param currentMultiplayerSession the current multiplayer session
     */
    public void setCurrentMultiplayerSession(MultiplayerSession currentMultiplayerSession) {
        this.currentMultiplayerSession = currentMultiplayerSession;
    }

}
