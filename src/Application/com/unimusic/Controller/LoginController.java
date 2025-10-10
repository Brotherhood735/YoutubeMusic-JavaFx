package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.User;
import com.unimusic.Model.csvManager;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private csvManager csvManager;

    @FXML
    public void initialize() {
        csvManager = Main.getJsonManager();
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

        User user = csvManager.getUser(username);
        if (user != null && user.checkPassword(password)) {
            // Login successful
            NavigationHandler.setAuthenticatedUser(username);
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
}