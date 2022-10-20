package fr.univ_amu.iut.server;

import fr.univ_amu.iut.database.dao.DAOUserJDBC;
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
        return usersDAO.isIn(username,password);    // Verify if the username and the password is in the database
    }

}
