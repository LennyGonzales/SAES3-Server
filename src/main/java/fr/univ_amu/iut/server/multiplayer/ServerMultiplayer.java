package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.dao.DAOQuizJDBC;
import fr.univ_amu.iut.database.table.ConfigSessions;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.server.questions.GiveQuestions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
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

    // Main server
    private BufferedReader in;
    private BufferedWriter out;

    // Secondary server
    private BufferedWriter outSecondary;

    public ServerMultiplayer(String code, BufferedReader in, BufferedWriter out) throws IOException, SQLException {
        this.in = in;
        this.out = out;
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
    public void acceptClients() throws IOException, SQLException {
        int numPlayer = 0;
        try {
            serverSocketChannel.configureBlocking(false);   //serverSocketChannel.accept() not blocking until there is a connection. This allows the user to click on 'Start Game' and don't wait a new connection to start the game
            do{
                SocketChannel sc = serverSocketChannel.accept();
                if (sc != null) {   // Get a connection

                    outSecondary = new BufferedWriter(new OutputStreamWriter(sc.socket().getOutputStream()));
                    outSecondary.write("PRESENCE_FLAG");
                    outSecondary.newLine();
                    outSecondary.flush();

                    clients.add(sc.socket());
                    ++numPlayer;
                }
            } while((!(in.ready())) && (numPlayer < (NB_PLAYERS - 1))); // While the multiplayer session's host doen't click on the button 'Lancer'

            deleteTupleFromDatabase();
            // Send a message to the multiplayer session's host
            out.write("CAN_JOIN_FLAG");
            out.newLine();
            out.write(Integer.toString(serverSocketChannel.socket().getLocalPort()));
            out.newLine();
            out.flush();

            SocketChannel sc = null;
            while(sc == null) {
                sc = serverSocketChannel.accept();
            }
            pool.execute(new GiveQuestions(sc.socket(), qcmList));

            for (Socket socketClient : clients) {
                outSecondary = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
                outSecondary.write("BEGIN_FLAG");
                outSecondary.newLine();
                outSecondary.flush();
                pool.execute(new GiveQuestions(socketClient,qcmList));
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
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}