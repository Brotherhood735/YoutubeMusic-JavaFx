package com.unimusic.Model.Authentication;

public class User {
    private String id;
    private String username;
    private String password;
    private boolean isAdmin;
    private boolean isGuest;

    public User(String username, String password, boolean isAdmin, boolean isGuest) {
        this.id = username; // Using username as ID for simplicity
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isGuest = isGuest;
    }

    public static User create(String username, String password, boolean isAdmin, boolean isGuest) {
        return new User(username, password, isAdmin, isGuest);
    }

    public static UserProperty createRequest(String username, String password) {
        return new UserProperty(username, password);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public boolean isGuest() {
        return isGuest;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public void setGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }
}