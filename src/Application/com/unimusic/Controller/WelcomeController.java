package com.unimusic.Controller;

import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        // If we're already authenticated, hide login/register buttons
        if (NavigationHandler.isAuthenticated()) {
            loginButton.setVisible(false);
            registerButton.setVisible(false);
        }
    }

    @FXML
    private void handleLogin() {
        NavigationHandler.navigateToLogin();
    }

    @FXML
    private void handleRegister() {
        NavigationHandler.navigateToRegister();
    }

    @FXML
    private void handleDashboard() {
        NavigationHandler.navigateToDashboard();
    }
}