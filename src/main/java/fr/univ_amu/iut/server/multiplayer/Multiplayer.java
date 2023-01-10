package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.server.ClientCommunication;
import fr.univ_amu.iut.server.module.Modules;
import fr.univ_amu.iut.server.questions.GiveQuestions;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.*;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Supports the multiplayer's actions (creation and join)
 */
public class Multiplayer {
    private final ClientCommunication clientCommunication;
    private Modules modules;

    public Multiplayer(ClientCommunication clientCommunication) throws SQLException {
        this.clientCommunication = clientCommunication;
        modules = new Modules(clientCommunication);
    }

    /**
     * Create the session's code
     * @return the session's code
     */
    public String createCode() {
        return UUID.randomUUID().toString().substring(0,8);
    }

    /**
     * Send the code to the host
     * @param code the session's code
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendCode(String code) throws IOException {
        clientCommunication.sendMessageToClient("CODE_FLAG");
        clientCommunication.sendMessageToClient(code);
    }

    /**
     * Create the multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void createMultiplayerSession() throws IOException, SQLException, ClassNotFoundException, EmptyQuestionsListException {
        // Module choice
        modules.sendModulesToTheHost();
        String choice = modules.getModuleChoice();

        if(!(choice.equals("BACK_TO_MENU_FLAG"))) {
            // Code
            String code = createCode();
            sendCode(code);

            // Create session
            MultiplayerSession multiplayerSession = new MultiplayerSession(code, choice, clientCommunication);
            MultiplayerSessions.addSession(code, multiplayerSession);   // Add session to the multiplayerSessions HashMap


            if((multiplayerSession.isGameWillPlayed()) && ((clientCommunication.receiveMessageFromClient()).equals("BEGIN_FLAG"))) {
                GiveQuestions giveQuestions = new GiveQuestions(clientCommunication, multiplayerSession.getQcmList(), multiplayerSession.getWrittenResponseQuestionList());
                giveQuestions.run();
            }
        }
    }

    /**
     * Join a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void joinMultiplayerSession() throws IOException, EmptyQuestionsListException {
        String sessionCode = clientCommunication.receiveMessageFromClient();
        if(!(MultiplayerSessions.getMultiplayerSessions().containsKey(sessionCode))) {
            clientCommunication.sendMessageToClient("SESSION_NOT_EXISTS_FLAG");
        } else {
            MultiplayerSession multiplayerSession = MultiplayerSessions.getSession(sessionCode);

            String mail = clientCommunication.receiveMessageFromClient();
            multiplayerSession.addClient(clientCommunication, mail);

            if ((clientCommunication.receiveMessageFromClient()).equals("BEGIN_FLAG")) {
                GiveQuestions giveQuestions = new GiveQuestions(clientCommunication, multiplayerSession.getQcmList(), multiplayerSession.getWrittenResponseQuestionList());
                giveQuestions.run();
            }
        }
    }
}
