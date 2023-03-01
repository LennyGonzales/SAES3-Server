package fr.univ_amu.iut.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Creation of the Server and accepts the clients
 * @author LennyGonzales
 */
public class Server {
    private static final int NB_CLIENTS = 100;
    private static final int NB_THREADS = 100;
    private static final int NUM_PORT = 10013;
    private SSLServerSocket sockServer;
    private ExecutorService pool;

    public Server() throws Exception {
        this.pool = Executors.newFixedThreadPool(NB_THREADS);   // Fix the number of threads
        try {
            Security.addProvider(new BouncyCastleProvider());
            System.setProperty("javax.net.ssl.keyStore", "myKeyStore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "password");   // ----!!!
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            sockServer = (SSLServerSocket) factory.createServerSocket(NUM_PORT);
            sockServer.setEnabledProtocols(new String[] { "TLSv1.3" });

        } catch (Exception e){
            throw new Exception("Initialisation socket");
        }
    }

    /**
     * Accepts the clients and call the TaskThread class
     * @throws IOException if an error occurs when waiting for a connection
     */
    public void acceptClients() throws Exception {
        for (int i = 0; i < NB_CLIENTS; ++i) {
            SSLSocket sock_client = (SSLSocket) sockServer.accept();
            pool.execute(new TaskThread(sock_client));  // Use a new thread for each client
        }
        sockServer.close();
    }
}
