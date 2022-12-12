package fr.univ_amu.iut.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creation of the Server and accepts the clients
 */
public class Server {
    private static final int NB_CLIENTS = 100;
    private static final int NB_THREADS = 100;
    private static final int NUM_PORT = 10013;
    private ServerSocket sockServer;
    private ExecutorService pool;

    public Server() throws Exception {
        this.pool = Executors.newFixedThreadPool(NB_THREADS);   // Fix the number of threads
        try {
            sockServer = new ServerSocket(NUM_PORT);
        } catch (Exception e){
            throw new Exception("Initialisation socket");
        }
    }

    /**
     * Accepts the clients and call the TaskThread class
     * @throws IOException
     */
    public void acceptClients() throws Exception {
        for (int i = 0; i < NB_CLIENTS; ++i) {
            Socket sock_client = sockServer.accept();
            pool.execute(new TaskThread(sock_client));  // Use a new thread for each client
        }
        sockServer.close();
    }

    public void run() throws Exception {
        acceptClients();
    }
}
