package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.dao.DAOQuizJDBC;
import fr.univ_amu.iut.database.table.ConfigSessions;
import fr.univ_amu.iut.database.table.Qcm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
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
    private String code;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService pool;
    private int port;
    private ConfigSessions configSessions;
    private DAOConfigSessionsJDBC configSessionsJDBC;
    private DAOQuizJDBC daoQuiz;
    private List<Qcm> qcmList;
    private List<Socket> clients;
    private BufferedReader in;

    public ServerMultiplayer(String code, BufferedReader in) throws IOException, SQLException {
        this.in = in;
        this.code = code;
        pool = Executors.newFixedThreadPool(NB_PLAYERS);
        clients = new ArrayList<>();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(0)); // Find a free port
        port = serverSocketChannel.socket().getLocalPort();

        configSessions = new ConfigSessions(port, code);  //Create an instance (a tuple)
        configSessionsJDBC = new DAOConfigSessionsJDBC();

        daoQuiz = new DAOQuizJDBC();
        qcmList = daoQuiz.findAllQCM();
    }

    /**
     * Accepts the clients and call the TaskThreadMultiplayer class
     * @throws IOException
     */
    public void acceptClients() throws IOException {
        int numPlayer = 0;
        try {
            serverSocketChannel.configureBlocking(false);   //serverSocketChannel.accept() not blocking until there is a connection. This allows the user to click on 'Start Game' and don't wait a new connection to start the game
            while ((!(in.ready())) && (numPlayer < NB_PLAYERS)) {
                SocketChannel sc = serverSocketChannel.accept();
                if (sc != null) {   // Get a connection
                    clients.add(sc.socket());
                    ++numPlayer;
                }
            }
            for (Socket socketClient : clients) {
                pool.execute(new TaskThreadMultiplayer(socketClient,qcmList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            deleteTupleFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}