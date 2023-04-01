package fr.univ_amu.iut;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creation of the Server and accepts the clients
 * @author LennyGonzales
 */
public class Server {
    private static final int NB_THREADS = 100;
    private static final int NUM_PORT = 10013;
    private SSLServerSocket sockServer;
    private ExecutorService pool;

    /**
     * Initialize the server and prepare the files needed for the handshake
     * @throws Exception if the initialization didn't go well
     */
    public Server() throws Exception {
        this.pool = Executors.newFixedThreadPool(NB_THREADS);   // Fix the number of threads
        try {
            System.setProperty("javax.net.ssl.keyStore", "keyStore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "uiYy1ae7h!ayvU");
            System.setProperty("javax.net.ssl.trustStore", "trustStore.jts");
            System.setProperty("javax.net.ssl.trustStorePassword", "uiYy1ae7h!ayvU");

            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            sockServer = (SSLServerSocket) factory.createServerSocket(NUM_PORT);
            sockServer.setEnabledProtocols(new String[] { "TLSv1.3" });
            sockServer.setNeedClientAuth(true);
        } catch (Exception e){
            throw new Exception("Initialisation socket");
        }
    }

    /**
     * Accepts the clients and call the TaskThread class
     * @throws IOException if an error occurs when waiting for a connection
     */
    public void acceptClients() throws Exception {
        while(true) {
            SSLSocket sock_client = (SSLSocket) sockServer.accept();
            pool.execute(new TaskThread(sock_client));  // Use a new thread for each client
        }
    }
}
