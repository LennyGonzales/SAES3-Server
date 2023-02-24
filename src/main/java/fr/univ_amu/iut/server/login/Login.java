package fr.univ_amu.iut.server.login;

import fr.univ_amu.iut.database.dao.DAOUsersJDBC;
import fr.univ_amu.iut.server.ClientCommunication;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Supports the login of the user
 * @author LennyGonzales
 */
public class Login {
    private final ClientCommunication clientCommunication;
    public Login(ClientCommunication clientCommunication) {
        this.clientCommunication = clientCommunication;
    }

    /**
     * Verify if the username and the password are in the database
     *
     * @return true if the player is in the database
     * @throws SQLException the SQL request didn't go well (isIn method)
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean isLogin() throws SQLException, IOException {
        DAOUsersJDBC usersDAO = new DAOUsersJDBC();
        return usersDAO.authentication(clientCommunication.receiveMessageFromClient(),encryptLogin(clientCommunication.receiveMessageFromClient()));    // Verify if the username and the encrypted password is in the database
    }

    /**
     * Send to the client a specific flag if the login credentials are correct or incorrect
     *
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws SQLException the SQL request didn't go well (DAOUserJDBC.isIn() method)
     */
    public void serviceLogin() throws IOException, SQLException {
        if(isLogin()) {
            clientCommunication.sendMessage("LOGIN_SUCCESSFULLY");
        } else {
            clientCommunication.sendMessage("LOGIN_NOT_SUCCESSFULLY");
        }
    }

    /**
     * Encrypt a specific string with SHA512 algorithm
     *
     * @param str the String to hash
     * @return hashtext the String hashed
     * @throws RuntimeException if SHA-512 algorithm is not found
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
