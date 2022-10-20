package fr.univ_amu.iut.server;


import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class TaskThread implements Runnable {
    private Socket sockClient;
    private BufferedReader in;
    private BufferedWriter out;

    public TaskThread(Socket sockClient) throws IOException {
        this.sockClient = sockClient;
        this.in = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(sockClient.getOutputStream()));
    }

    /**
     * A function which gets the inputs of the client and call login.verifyLogin()
     *
     * @return true if the player is login
     * @throws IOException
     * @throws SQLException
     */
    public boolean isLogin() throws IOException, SQLException {
        Login login = new Login(in.readLine(),in.readLine());   // Get the username and the password
        return login.verifyLogin();
    }

    /**
     * This function supports client login
     *
     * @throws IOException
     * @throws SQLException
     */
    public void serviceLogin() throws IOException, SQLException {
        while(!isLogin()) { // Until the client is able to connect
            out.write("[-] NOT LOGIN !");
            out.newLine();
            out.flush();
            while(!in.ready()); // Wait until the client retry to connect
        }
        out.write("[+] LOGIN !");
        out.newLine();
        out.flush();
    }
    /**
     * A function which find the service type and call the function associated
     *
     * @throws IOException
     */
    public void serviceType() throws SQLException, IOException {  // Find the service between {Login, Solo, Multijoueur, Entraînement}
        String str;
        while ((str = in.readLine()) != null) { // As long as the server receives no requests, it waits
            switch (str) {
                case "LOGIN_FLAG":
                    serviceLogin();
                    break;
            }
        }
        stopRunning();
    }

    /**
     * Stop the connection with the client
     *
     * @throws IOException
     */
    public void stopRunning() throws IOException {
        sockClient.close();
    }

    @Override
    public void run() {
        try {
            serviceType();
        } catch (IOException | SQLException e){
            throw new RuntimeException(e);
        }
    }
}
