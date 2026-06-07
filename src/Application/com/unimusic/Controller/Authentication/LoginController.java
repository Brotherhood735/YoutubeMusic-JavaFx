package com.unimusic.Controller.Authentication;

import com.unimusic.Controller.UniController;
import com.unimusic.Model.Authentication.AuthenticationFacade;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends UniController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private AuthenticationFacade authFacade;

    @FXML
    public void initialize() {
        authFacade = AuthenticationFacade.getInstance();
        statusLabel.setVisible(false);
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean success = authFacade.login(username, password);

        if (success) {
            NavigationHandler.navigateToHome();
        } else {
            showError("username o password invalidi");
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
    public void setContent(Node content, int Idx) { }
}