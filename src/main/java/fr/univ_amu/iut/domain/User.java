package fr.univ_amu.iut.domain;

/**
 * Represents a tuple of the Users table
 * @author LennyGonzales
 */
public class User {
    private String email;
    private String password;
    private String userStatus;
    private int points;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
