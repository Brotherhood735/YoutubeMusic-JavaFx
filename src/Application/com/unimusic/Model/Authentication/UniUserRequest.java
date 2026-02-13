package com.unimusic.Model.Authentication;

import javafx.beans.property.*;

public class UniUserRequest {
    private final StringProperty username;
    private final String password;
    private final BooleanProperty admin;
    private User user;
    public UniUserRequest(String username, String password) {
        this.username = new SimpleStringProperty(username);
        this.admin = new SimpleBooleanProperty(false);
        this.password = password;
        this.user = new User(username, password, false, true);
    }

    public StringProperty usernameProperty() { return username; }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password; }
    public User getUser() { return user; }
    public BooleanProperty adminProperty() { return admin; }
    public boolean isAdmin() { return admin.get(); }
    public void setAdmin(boolean value) { this.admin.set(value); }
}