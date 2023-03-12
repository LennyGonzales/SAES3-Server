package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.server.module.Modules;
import fr.univ_amu.iut.server.questions.GiveQuestions;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
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
     * Create the multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void createMultiplayerSession() throws IOException, SQLException, EmptyQuestionsListException, CloneNotSupportedException {
        // Module choice
        modules.sendModules();
        CommunicationFormat message = communication.receiveMessage();

        // Session code
        String codeSession = createCode();
        communication.sendMessage(new CommunicationFormat(Flags.CODE, codeSession));

        // If the user doesn't return on the menu page
        if(!(message.getFlag().equals(Flags.BACK_TO_MENU))) {
            // Create the session
            MultiplayerSession multiplayerSession = new MultiplayerSession(codeSession, message.getContent().toString(), communication);
            MultiplayerSessions.addSession(codeSession, multiplayerSession);   // Add session to the multiplayerSessions HashMap

            message = communication.receiveMessage();   // Users can join
            if(message.getFlag().equals(Flags.BEGIN)) {
                multiplayerSession.start();     // Notify all the users
                MultiplayerSessions.removeSession(codeSession); // Remove the session

                // Start the story for the host
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
            communication.sendMessage(new CommunicationFormat(Flags.SESSION_EXISTS));
            return true;
        }
        communication.sendMessage(new CommunicationFormat(Flags.SESSION_NOT_EXISTS));
        return false;
    }

    /**
     * Join a multiplayer session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws EmptyQuestionsListException if the questions lists are empty
     */
    public void joinMultiplayerSession(String code) throws IOException, EmptyQuestionsListException, CloneNotSupportedException {
        if(checkMultiplayerSessionExistence(code)) {  // Verify if the multiplayer session exists
            MultiplayerSession multiplayerSession = MultiplayerSessions.getSessionWithSessionCode(code);    // Get the multiplayer session instance

            CommunicationFormat message = communication.receiveMessage();   // Get the user email
            if(message.getFlag().equals(Flags.EMAIL)) {
                multiplayerSession.addUser(communication, message.getContent().toString());  // Add it to the user list

                if (communication.receiveMessage().getFlag().equals(Flags.BEGIN)) {  // If the session begin (the host of the session start it), we give the questions to the user
                    GiveQuestions giveQuestions = new GiveQuestions(communication, multiplayerSession.getQcmList(), multiplayerSession.getWrittenResponseQuestionList());
                    giveQuestions.run();
                }
            }
        }
    }
}
