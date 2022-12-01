package fr.univ_amu.iut.server.login;

import fr.univ_amu.iut.database.dao.DAOUserJDBC;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
public class Login {
    private ClientCommunication clientCommunication;
    public Login(ClientCommunication clientCommunication) {
        this.clientCommunication = clientCommunication;
    }

    /**
     * Verify if the username and the password is in the database
     *
     * @return true if the player is in the database
     * @throws SQLException
     */
    public boolean isLogin() throws SQLException, IOException {
        DAOUserJDBC usersDAO = new DAOUserJDBC();
        return usersDAO.isIn(clientCommunication.receiveMessageFromClient(),encryptLogin(clientCommunication.receiveMessageFromClient()));    // Verify if the username and the encrypted password is in the database
    }

    /**
     * This function supports client login
     *
     * @throws IOException
     * @throws SQLException
     */
    public void serviceLogin() throws IOException, SQLException {
        if(!isLogin()) { // Until the client is able to connect
            clientCommunication.sendMessageToClient("[-] NOT LOGIN !");
        } else {
            clientCommunication.sendMessageToClient("[+] LOGIN !");
        }
    }

    /**
     * Encrypt a specific string with SHA512 algorithm
     *
     * @param str
     * @return hashtext
     * @throws RuntimeException
     */
    public static String encryptLogin(String str)
    {
        try {
            // getInstance() is called with the SHA-512 algorithm
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // To calculate the message digest of the input string
            // Returned as a byte array
            byte[] messageDigest = md.digest(str.getBytes());

            // Convert bytes array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message summary to hexadecimal value
            StringBuilder hashtext = new StringBuilder(no.toString(16));

            // Add the previous 0 to get the 32-bit.
            while (hashtext.length() < 32) {
                hashtext.insert(0,'0');
            }
            return hashtext.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
