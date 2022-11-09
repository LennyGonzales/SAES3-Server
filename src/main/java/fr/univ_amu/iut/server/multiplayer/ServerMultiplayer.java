package fr.univ_amu.iut.server.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMultiplayer {
    private static final int NB_PLAYERS = 40;
    private String code;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private int port;

    public ServerMultiplayer(String code) throws IOException {
        this.code = code;
        pool = Executors.newFixedThreadPool(NB_PLAYERS);
        serverSocket = new ServerSocket(0); // Find a free port
        port = serverSocket.getLocalPort();
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

    /**
     * Return the port
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @throws IOException
     */
    public void run() throws IOException {
        acceptClients();
    }
}
