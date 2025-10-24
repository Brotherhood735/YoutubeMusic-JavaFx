package com.unimusic.Controller;

import com.unimusic.Model.AuthManager;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class WelcomeController extends UniController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        // If we're already authenticated, hide login/register buttons
        if (AuthManager.isAuthenticated()) {
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

    @Override
    public void setContent(Node content, int Idx) {


    }

}