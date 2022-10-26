package fr.univ_amu.iut.server.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMultiplayer {
    private static final int NB_PLAYERS = 40;
    private static final int NB_THREADS = 100;
    private int num_port;
    private ServerSocket serverSocket;
    private ExecutorService pool;

    public ServerMultiplayer() throws IOException {
        pool = Executors.newFixedThreadPool(NB_THREADS);
        serverSocket = new ServerSocket(0); // Find a free port
    }

    /**
     * Accepts the clients and call the TaskThreadMultiplayer class
     * @throws IOException
     */
    public void acceptClients() throws IOException {
        for (int i = 0; i < NB_PLAYERS; ++i) {
            // pool.execute(new TaskThreadMultiplayer(serverSocket.accept()));
        }
    }

    public void run() throws IOException {
        acceptClients();
    }
}
