package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.*;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController  extends UniController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private DataManager dataManager;

    @FXML
    public void initialize() {
        dataManager = Main.getDataManager();
        statusLabel.setVisible(false);
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }

        User user = dataManager.getUser(username);
        if (user != null && user.checkPassword(password)) {
            // Login successful
            AuthManager.setUser(username);
            NavigationHandler.navigateToDashboard();
        } else {
            showError("Invalid username or password");
        }
    }
    @FXML
    private void register() {
        NavigationHandler.navigateToRegister();
    }
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }

    @Override
    public void setContent(Node content, int Idx) {

    }

    @Override
    public void setData(UniData data) {

    }

}