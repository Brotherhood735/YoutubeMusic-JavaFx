package com.unimusic.Controller.Authentication;

import com.unimusic.Controller.UniController;
import com.unimusic.Model.Authentication.AuthenticationFacade;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController extends UniController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;

    private AuthenticationFacade authFacade;

    @FXML
    public void initialize() {
        authFacade = AuthenticationFacade.getInstance();
        statusLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        String error = authFacade.register(username, password, confirmPassword);

        if (error == null) {
            showSuccess("Fino alla conferma dell'amministratore puoi navigare l'app in modalità guest.");
            clearFields();
        } else {
            showError(error);
        }
    }

    private void clearFields() {
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
    public void setContent(Node content, int Idx) { }
}