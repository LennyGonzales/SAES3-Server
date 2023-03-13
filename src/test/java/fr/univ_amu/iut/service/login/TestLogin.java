package fr.univ_amu.iut.service.login;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.control.Controllers;
import fr.univ_amu.iut.database.dao.DAOUsersJDBC;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.*;
public class TestLogin {
    @Test
    public void shouldLoggedIn() throws IOException, SQLException {
        /*
        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(10014);

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        factory.createSocket("127.0.0.1", 10014);

        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        Controllers controller = new Controllers(new Communication(sslSocket), new DAOUsersJDBC());

        assertFalse(controller.loginAction(Arrays.asList("iy","iy")));
         */
    }
}
