package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.dao.DAOQuizJDBC;
import fr.univ_amu.iut.database.table.ConfigSessions;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.server.ClientCommunication;
import fr.univ_amu.iut.server.questions.GiveQuestions;

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

public class ServerMultiplayer implements Runnable{
    private static final int NB_PLAYERS = 40;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService pool;
    private int port;
    private ConfigSessions configSessions;
    private DAOConfigSessionsJDBC configSessionsJDBC;
    private DAOQuizJDBC daoQuiz;
    private List<Qcm> qcmList;
    private List<Socket> clients;

    // Main server
    private final ClientCommunication clientCommunication;

    // Secondary server
    private ClientCommunication clientMultiplayerCommunication;
    public ServerMultiplayer(String code, ClientCommunication clientCommunication) throws IOException, SQLException {
        this.clientCommunication = clientCommunication;

        pool = Executors.newFixedThreadPool(NB_PLAYERS);
        clients = new ArrayList<>();

        // Creation of the multiplayer session's server
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(0)); // Find a free port
        serverSocketChannel.configureBlocking(false);   //serverSocketChannel.accept() not blocking until there is a connection. This allows the user to click on 'Start Game' and don't wait a new connection to start the game

        port = serverSocketChannel.socket().getLocalPort(); // Get this port

        configSessions = new ConfigSessions(port, code);  // Create an instance (a tuple)
        configSessionsJDBC = new DAOConfigSessionsJDBC();

        // Get a quiz
        daoQuiz = new DAOQuizJDBC();
        qcmList = daoQuiz.findAllQCM();
    }

    /**
     * Accept the clients
     * @throws IOException
     */
    public void acceptClients() throws IOException, SQLException {
        getUsersUntilSessionStart();    // Store users who join the session in a list and notify them that their request has been received

        deleteTupleFromDatabase();  // Delete the tuple from the database so that no other clients can join the game

        getHostAndExecute();    // Notify the session host that he can join the session | Run the session for him (give the questions)

        executeUsers();     // Run the session for all other users

    }

    /**
     * Run the session for all other users
     * @throws IOException
     */
    public void executeUsers() throws IOException {
        // Send to all other users that the game begins
        for (Socket socketClient : clients) {
            clientMultiplayerCommunication = new ClientCommunication(socketClient);
            clientMultiplayerCommunication.sendMessageToClient("BEGIN_FLAG");   // Notify the client that the session begin (he can read the quiz's data)
            pool.execute(new GiveQuestions(clientMultiplayerCommunication,qcmList));
        }
    }

    /**
     * Notify the session host that he can join the session
     * Run the session for him (give the questions)
     * @throws IOException
     */
    public void getHostAndExecute() throws IOException {
        // Send a message to the host of the multiplayer session to join the game
        clientCommunication.sendMessageToClient("CAN_JOIN_FLAG");
        clientCommunication.sendMessageToClient(Integer.toString(serverSocketChannel.socket().getLocalPort()));
        SocketChannel sc = null;
        while(sc == null) {
            sc = serverSocketChannel.accept();  // Accepts the host request
        }
        pool.execute(new GiveQuestions(new ClientCommunication(sc.socket()), qcmList));  // Give him the questions
    }

    /**
     * Store users who join the session in a list and notify them that their request has been received
     * @throws IOException
     */
    public void getUsersUntilSessionStart() throws IOException {
        int numPlayer = 0;
        do{
            SocketChannel sc = serverSocketChannel.accept();      // Accepts the client
            if (sc != null) {   // Get a connection
                clientMultiplayerCommunication = new ClientCommunication(sc.socket());
                clientMultiplayerCommunication.sendMessageToClient("PRESENCE_FLAG");    // Notify him that their request has been received
                clients.add(sc.socket());   // Add it to the list
                ++numPlayer;
            }
        } while((!(clientCommunication.isReceiveMessageFromClient())) && (numPlayer < (NB_PLAYERS - 1))); // While the multiplayer session's host doen't click on the button 'Lancer'

    }

    /**
     * Insert the session in the database (code, port)
     * @throws SQLException
     */
    public void insertTupleIntoDatabase() throws SQLException {
        configSessionsJDBC.insert(configSessions);  // Insert
    }

    /**
     * Delete the session in the database (code)
     * @throws SQLException
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
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}