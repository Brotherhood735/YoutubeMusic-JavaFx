package com.unimusic.Controller;

import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.View.NavigationHandler;
import com.unimusic.View.Components.NavBar;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public  class MainController extends UniController {
    @FXML
    private BorderPane mainContainer;

    @FXML
    private VBox navBarContainer;

    private NavBar navBar;

    @FXML
    public void initialize() throws IOException {
        // Set up the navigation bar
        //setNavBar();
        // Register this controller with the ViewNavigator
        NavigationHandler.setMainController(this);
        // Load the Login view by default
        NavigationHandler.navigateToLogin();
    }
    private void setNavBar() throws IOException {
        navBar = new NavBar();
        navBarContainer.getChildren().add(navBar);
    }
    /**
     * Set the content of the main area
     */
    @Override
    public void setContent(Node content, int Idx) {
        mainContainer.setCenter(content);
    }


    /**
     * Update the navigation bar based on authentication status
     */
    public void updateNavBar(boolean isAuthenticated) {
        if (navBar != null) navBar.updateAuthStatus(isAuthenticated, AuthManager.getAuthenticatedUser());
    }
}