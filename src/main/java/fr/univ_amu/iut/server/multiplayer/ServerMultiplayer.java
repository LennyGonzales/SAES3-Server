package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.dao.DAOQcmJDBC;
import fr.univ_amu.iut.database.dao.DAOWrittenResponseQuestionJDBC;
import fr.univ_amu.iut.database.table.ConfigSessions;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;
import fr.univ_amu.iut.server.ClientCommunication;
import fr.univ_amu.iut.server.questions.GiveQuestions;
import fr.univ_amu.iut.server.questions.exceptions.EmptyQuestionsListException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Supports the multiplayer session's server
 */
public class ServerMultiplayer implements Runnable{
    private static final int NB_PLAYERS = 40;
    private final ServerSocketChannel serverSocketChannel;
    private final ExecutorService pool;
    private final ConfigSessions configSessions;
    private final DAOConfigSessionsJDBC configSessionsJDBC;
    private final List<Qcm> qcmList;
    private final List<WrittenResponseQuestion> writtenResponseQuestionList;
    private final List<Socket> clients;

    // Main server
    private final ClientCommunication clientCommunication;

    // Secondary server
    private ClientCommunication clientMultiplayerCommunication;
    public ServerMultiplayer(String code, String module, ClientCommunication clientCommunication) throws IOException, SQLException {
        this.clientCommunication = clientCommunication;

        pool = Executors.newFixedThreadPool(NB_PLAYERS);
        clients = new ArrayList<>();

        // Creation of the multiplayer session's server
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(0)); // Find a free port
        serverSocketChannel.configureBlocking(false);   //serverSocketChannel.accept() not blocking until there is a connection. This allows the user to click on 'Start Game' and don't wait a new connection to start the game

        // serverSocketChannel.socket().getLocalPort() => get the port of the socket
        configSessions = new ConfigSessions(serverSocketChannel.socket().getLocalPort(), code);  // Create an instance (a tuple) of the table 'ConfigSessions'
        configSessionsJDBC = new DAOConfigSessionsJDBC();

        // Get a quiz
        DAOQcmJDBC daoQcm = new DAOQcmJDBC();
        qcmList = daoQcm.getACertainNumberOfQCM(5, module);
        DAOWrittenResponseQuestionJDBC daoWrittenResponseQuestion = new DAOWrittenResponseQuestionJDBC();
        writtenResponseQuestionList = daoWrittenResponseQuestion.getACertainNumberOfWrittenResponseQuestion(5, module);
    }

    /**
     * Accept the clients
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void acceptClients() throws IOException, SQLException, EmptyQuestionsListException, ClassNotFoundException {
        if(!getUsersUntilSessionStart().equals("BACK_TO_MENU_FLAG")) {    // Store users who join the session in a list and notify them that their request has been received

            deleteTupleFromDatabase();  // Delete the tuple from the database so that no other clients can join the game

            getHostAndExecute();    // Notify the session host that he can join the session | Run the session for him (give the questions)

            executeUsers();     // Run the session for all other users
        }
    }

    /**
     * Run the session for all other users
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void executeUsers() throws IOException, EmptyQuestionsListException {
        // Send to all other users that the game begins
        for (Socket socketClient : clients) {
            clientMultiplayerCommunication = new ClientCommunication(socketClient);
            clientMultiplayerCommunication.sendMessageToClient("BEGIN_FLAG");   // Notify the client that the session begin (he can read the quiz's data)
            pool.execute(new GiveQuestions(clientMultiplayerCommunication,qcmList, writtenResponseQuestionList));
        }
    }

    /**
     * Notify the session host that he can join the session
     * Run the session for him (give the questions)
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void getHostAndExecute() throws IOException, EmptyQuestionsListException {
        // Send a message to the host of the multiplayer session to join the game
        clientCommunication.sendMessageToClient("CAN_JOIN_FLAG");
        clientCommunication.sendMessageToClient(Integer.toString(serverSocketChannel.socket().getLocalPort()));
        SocketChannel sc = null;
        while(sc == null) {
            sc = serverSocketChannel.accept();  // Accepts the host request
        }
        pool.execute(new GiveQuestions(new ClientCommunication(sc.socket()), qcmList, writtenResponseQuestionList));  // Give him the questions
    }

    /**
     * Store users who join the session in a list and notify them that their request has been received
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public String getUsersUntilSessionStart() throws IOException, ClassNotFoundException {
        int numPlayer = 0;
        do{
            SocketChannel sc = serverSocketChannel.accept();      // Accepts the client
            if (sc != null) {   // Get a connection
                clientMultiplayerCommunication = new ClientCommunication(sc.socket());
                clientMultiplayerCommunication.sendMessageToClient("PRESENCE_FLAG");    // Notify him that their request has been received
                clients.add(sc.socket());   // Add it to the list
                clientCommunication.sendMessageToClient(clientMultiplayerCommunication.receiveMessageFromClient()); // Receive the mail of the user who joined the session and send it to the host of the multiplayer session
                ++numPlayer;
            }
        } while((!(clientCommunication.isReceiveMessageFromClient())) && (numPlayer < (NB_PLAYERS - 1))); // While the multiplayer session's host doesn't click on the button 'Lancer'

        if((clientCommunication.receiveMessageFromClient()).equals("BACK_TO_MENU_FLAG")) {
            return "BACK_TO_MENU_FLAG";
        }
        return "CONTINUE";
    }

    /**
     * Insert the session in the database (code, port)
     * @throws SQLException if the SQL request didn't go well (insert method)
     */
    public void insertTupleIntoDatabase() throws SQLException {
        configSessionsJDBC.insert(configSessions);  // Insert
    }

    /**
     * Delete the session in the database (code)
     * @throws SQLException if the SQL request didn't go well (delete method)
     */
    public void deleteTupleFromDatabase() throws SQLException {
        configSessionsJDBC.delete(configSessions);
    }

    @Override
    public void run() {
        try {
            insertTupleIntoDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            acceptClients();
        } catch (IOException | SQLException | EmptyQuestionsListException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}