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
    public boolean isLogin() throws IOException, SQLException { return false; }

    public void serviceLogin() throws IOException, SQLException {
        System.out.println("[+] FLAG FOR LOGIN !");
    }
    /**
     * A function which find the service type and call the function associated
     *
     * @throws IOException
     */
    public void serviceType() throws SQLException, IOException {  // Find the service between {Login, Solo, Multijoueur, Entraînement}
        String str;
        while ((str = in.readLine()) != null) {
            switch (str) {
                case "LOGIN_FLAG":
                    serviceLogin();
            }
        }
        System.out.println("END");
        stopRunning();
    }

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
