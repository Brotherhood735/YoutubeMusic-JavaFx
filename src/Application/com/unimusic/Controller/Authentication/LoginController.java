package com.unimusic.Controller.Authentication;

import com.unimusic.Controller.UniController;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Authentication.User;
import com.unimusic.Model.Repository.UserRepository;
import com.unimusic.Nav.NavigationHandler;
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

    private UserRepository userRepo;

    @FXML
    public void initialize() {
        userRepo = Main.getDataRepository();
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

        User user = userRepo.getRegistredUser(username);
        if (user != null && user.checkPassword(password)) {
            // Login successful
            AuthManager.setUser(user);
            NavigationHandler.navigateToHome();
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

}