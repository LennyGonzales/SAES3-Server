package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOConfigSessionsJDBC;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.server.TaskThread;
import fr.univ_amu.iut.server.multiplayer.ServerMultiplayer;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Multiplayer {
    private Socket sockClient;
    private BufferedReader in;
    private BufferedWriter out;
    private String str;

    public Multiplayer(Socket sockClient) throws IOException {
        this.sockClient = sockClient;
        this.in = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(sockClient.getOutputStream()));
    }

    /**
     * Create the multiplayer session
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public void createSession(String code) throws IOException, SQLException {
        ServerMultiplayer serverMultiplayer = new ServerMultiplayer(code, in, out);
        serverMultiplayer.run();
    }

    /**
     * Supports the creation of a multiplayer session
     * @throws IOException
     */
    public void createMultiplayerSession() throws IOException, SQLException {
        String code = UUID.randomUUID().toString().substring(0,8);
        out.write("CODE_FLAG");
        out.newLine();
        out.write(code);
        out.newLine();
        out.flush();
        createSession(code);
    }

    /**
     * Join a multiplayer session
     * @throws IOException
     */
    public void joinMultiplayerSession() throws IOException, SQLException {
        DAOConfigSessionsJDBC configSessionsJDBC = new DAOConfigSessionsJDBC();
        //--------------
        out.write("JOIN_SESSION");
        out.newLine();
        out.flush();
        //--------------
        if((str = in.readLine()) != null) { // Get the input code and ask if the code is in the database
            if(configSessionsJDBC.isIn(str)) {
                out.write("CODE_EXISTS_FLAG");
                out.newLine();
                out.write(Integer.toString(configSessionsJDBC.findPort(str)));    // Give the port

            } else {
                out.write("CODE_NOT_EXISTS_FLAG");
            }
            out.newLine();
            out.flush();
        } else {
            TaskThread.stopRunning();
        }
    }
}
