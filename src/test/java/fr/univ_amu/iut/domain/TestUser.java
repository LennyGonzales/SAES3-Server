package fr.univ_amu.iut.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUser {
    private User user;

    @Before
    public void beforeEachTest() {
        this.user = new User();
    }

    @Test
    public void testGetEmail() {
        user.setEmail("test@test.com");
        assertEquals("test@test.com",user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("newtest@test.com");
        assertEquals("newtest@test.com",user.getEmail());
    }

    @Test
    public void testGetPassword() {
        user.setPassword("testPassword");
        assertEquals("testPassword",user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newTestPassword");
        assertEquals("newTestPassword",user.getPassword());
    }

    @Test
    public void testGetUserStatus() {
        user.setUserStatus("testUserStatus");
        assertEquals("testUserStatus",user.getUserStatus());
    }

    @Test
    public void testSetUserStatus() {
        user.setUserStatus("newTestUserStatus");
        assertEquals("newTestUserStatus",user.getUserStatus());
    }

    @Test
    public void testGetPoints() {
        user.setPoints(10);
        assertEquals(10,user.getPoints());
    }

    @Test
    public void testSetPoints() {
        user.setPoints(20);
        assertEquals(20,user.getPoints());
    }
}
