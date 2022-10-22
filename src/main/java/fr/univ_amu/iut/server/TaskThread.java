package fr.univ_amu.iut.server;


import fr.univ_amu.iut.database.dao.DAOQuizJDBC;
import fr.univ_amu.iut.database.table.Qcm;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class TaskThread implements Runnable {
    private Socket sockClient;
    private BufferedReader in;
    private BufferedWriter out;
    private String str;

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
     * Send questions and answer to the client and verify if the answer is correct
     * @throws SQLException
     * @throws IOException
     */
    public void serviceSolo() throws SQLException, IOException {
        DAOQuizJDBC daoQuiz = new DAOQuizJDBC();
        List<Qcm> qcmList = daoQuiz.findAllQCM();
        for(Qcm q : qcmList) {
            out.write(q.getQuestion());
            out.newLine();
            out.write(q.getAnswer1());
            out.newLine();
            out.write(q.getAnswer2());
            out.newLine();
            out.write(q.getAnswer3());
            out.newLine();

            out.flush();

            if((str = in.readLine()) != null) {
                if(q.getTrueAnswer() == Integer.parseInt(str)) {
                    out.write("CORRECT_ANSWER_FLAG");
                } else {
                    out.write("WRONG_ANSWER_FLAG");
                }
                out.newLine();
                out.flush();
            }
        }
    }

    /**
     * A function which find the service type and call the function associated
     *
     * @throws IOException
     */
    public void serviceType() throws SQLException, IOException {  // Find the service between {Login, Solo, Multijoueur, Entraînement}
        while ((str = in.readLine()) != null) { // As long as the server receives no requests, it waits
            switch (str) {
                case "LOGIN_FLAG":
                    serviceLogin();
                    break;
                case "SOLO_FLAG":
                    serviceSolo();
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
        in.close();
        out.close();
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
