package fr.univ_amu.iut.server;

import fr.univ_amu.iut.database.dao.DAOUserJDBC;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
public class Login {
    private String username;
    private String password;
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Verify if the username and the password is in the database
     *
     * @return
     * @throws SQLException
     */
    public boolean verifyLogin() throws SQLException {
        DAOUserJDBC usersDAO = new DAOUserJDBC();
        return usersDAO.isIn(username,encryptLogin(password));    // Verify if the username and the encrypted password is in the database
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
