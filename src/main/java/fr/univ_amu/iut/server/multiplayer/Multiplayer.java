package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.server.module.Modules;
import fr.univ_amu.iut.server.questions.GiveQuestions;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.*;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Supports the multiplayer's actions (create and join)
 * @author LennyGonzales
 */
public class Multiplayer {
    private final Communication communication;
    private Modules modules;

    public Multiplayer(Communication communication) throws SQLException {
        this.communication = communication;
        modules = new Modules(communication);
    }

    /**
     * Generate the session's code (a random string with a length of 8)
     * @return the session's code
     */
    public String createCode() {
        return UUID.randomUUID().toString().substring(0,8);
    }

    /**
     * Send the code to the user
     * @param code the session's code
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendCodeToTheUser(String code) throws IOException {
        communication.sendMessageToClient("CODE_FLAG");
        communication.sendMessageToClient(code);
    }

    /**
     * Create the multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void createMultiplayerSession() throws IOException, SQLException, EmptyQuestionsListException {
        // Module choice
        modules.sendModules();
        String choice = modules.getModuleChoice();

        // If the user doesn't return on the menu page
        if(!(choice.equals("BACK_TO_MENU_FLAG"))) {
            // Session code
            String code = createCode();
            sendCodeToTheUser(code);

            // Create the session
            MultiplayerSession multiplayerSession = new MultiplayerSession(code, choice, communication);
            MultiplayerSessions.addSession(code, multiplayerSession);   // Add session to the multiplayerSessions HashMap

            // If the multiplayer session isn't cancel, give the questions to the user
            if((multiplayerSession.isGameWillPlayed()) && ((communication.receiveMessageFromClient()).equals("BEGIN_FLAG"))) {
                GiveQuestions giveQuestions = new GiveQuestions(communication, multiplayerSession.getQcmList(), multiplayerSession.getWrittenResponseQuestionList());
                giveQuestions.run();
            }
        }
    }

    /**
     * Check the existence of a multiplayer session with his code
     * @param sessionCode the multiplayer session code
     * @return true - the multiplayer session exists | else, false
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean checkMultiplayerSessionExistence(String sessionCode) throws IOException {
        if(MultiplayerSessions.getMultiplayerSessions().containsKey(sessionCode)) {  // Verify if the multiplayer session exists
            communication.sendMessageToClient("SESSION_EXISTS_FLAG");
            return true;
        }
        communication.sendMessageToClient("SESSION_NOT_EXISTS_FLAG");
        return false;
    }

    /**
     * Join a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws EmptyQuestionsListException if the questions lists are empty
     */
    public void joinMultiplayerSession() throws IOException, EmptyQuestionsListException {
        String sessionCode = communication.receiveMessageFromClient();    // Get the code of the multiplayer session to join

        if(checkMultiplayerSessionExistence(sessionCode)) {  // Verify if the multiplayer session exists
            MultiplayerSession multiplayerSession = MultiplayerSessions.getSessionWithSessionCode(sessionCode);    // Get the multiplayer session instance

            String mail = communication.receiveMessageFromClient();   // Get the user email
            multiplayerSession.addUser(communication, mail);  // Add it to the user list

            if ((communication.receiveMessageFromClient()).equals("BEGIN_FLAG")) {    // If the session begin (the host of the session start it), we give the questions to the user
                GiveQuestions giveQuestions = new GiveQuestions(communication, multiplayerSession.getQcmList(), multiplayerSession.getWrittenResponseQuestionList());
                giveQuestions.run();
            }
        }
    }
}
