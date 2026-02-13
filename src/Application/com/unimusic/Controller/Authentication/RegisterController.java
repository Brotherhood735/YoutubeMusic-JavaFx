package com.unimusic.Controller.Authentication;

import com.unimusic.Controller.UniController;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.UniUserRequest;
import com.unimusic.Model.Authentication.User;
import com.unimusic.Model.Repository.UserRepository;
import com.unimusic.Nav.NavigationHandler;
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

    private UserRepository userRepo;

    @FXML
    public void initialize() {
        userRepo = Main.getDataRepository();
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

        if (userRepo.usernameExists(username)) {
            showError("Username already exists");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Create and save new user
        UniUserRequest newRequest = User.createRequest(username, password);
        userRepo.saveUser(newRequest.getUser());
        userRepo.addToRegistrationReqList(newRequest);

        // Show success message
        showSuccess("Fino alla conferma dell'amministratore puoi navigare l'app in modalità guest.");

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

}