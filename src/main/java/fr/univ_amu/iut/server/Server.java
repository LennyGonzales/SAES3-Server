package fr.univ_amu.iut.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int NB_CLIENTS = 100;
    private static final int NB_THREADS = 30;
    private static final int NUM_PORT = 10013;
    private ServerSocket sockServer;
    private ExecutorService pool;

    public Server() throws Exception {
        this.pool = Executors.newFixedThreadPool(NB_THREADS);
        try {
            sockServer = new ServerSocket(NUM_PORT);
        } catch (Exception e){
            throw new Exception("Initialisation socket");
        }
    }

    public void acceptClient() throws Exception {
        for (int i = 0; i < NB_CLIENTS; ++i) {
            Socket sock_client = sockServer.accept();
            pool.execute(new TaskThread(sock_client));
        }
        sockServer.close();
    }

    public void run() throws Exception {
        acceptClient();
    }
}
