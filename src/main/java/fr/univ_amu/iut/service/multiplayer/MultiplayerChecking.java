package fr.univ_amu.iut.service.multiplayer;

import fr.univ_amu.iut.domain.MultiplayerSession;

/**
 *
 * @author LennyGonzales
 */
public class MultiplayerChecking {
    private MultiplayerSession currentMultiplayerSession;

    public MultiplayerSession getCurrentMultiplayerSession() {
        return currentMultiplayerSession;
    }

    public void setCurrentMultiplayerSession(MultiplayerSession currentMultiplayerSession) {
        this.currentMultiplayerSession = currentMultiplayerSession;
    }

}
