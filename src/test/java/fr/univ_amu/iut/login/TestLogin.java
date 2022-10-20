package fr.univ_amu.iut.login;

import fr.univ_amu.iut.server.Login;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

public class TestLogin {
    @Test
    public void should_hash_text() {
        assertEquals(Login.encryptLogin("AnyString"), "1da03e4b0de2cbbf4fdbf6064cbedc63c0b63aed26ba0437a69268cdd2b893b75ee44bb0e2488c8951726ab3f1eb6bb24e1a06bfd2b3d86ea0e8668f92f206ad");
    }
}
