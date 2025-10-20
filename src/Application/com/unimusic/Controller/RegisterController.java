package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import com.unimusic.Model.User;
import com.unimusic.Model.DataManager;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController extends UniController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label statusLabel;

    private DataManager dataManager;

    @FXML
    public void initialize() {
        dataManager = Main.getDataManager();
        statusLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill out all fields");
            return;
        }

        if (dataManager.usernameExists(username)) {
            showError("Username already exists");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Create and save new user
        User newUser = User.create(username, password);
        dataManager.saveUser(newUser);

        // Show success message
        showSuccess("Registration successful! Please log in.");

        // Clear fields
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    private void handleBackToLogin() {
        NavigationHandler.navigateToLogin();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
        statusLabel.setVisible(true);
    }

    @Override
    public void setContent(Node content, int Idx) {

    }

    @Override
    public void setData(UniData data) {

    }
}