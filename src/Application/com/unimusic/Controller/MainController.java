package com.unimusic.Controller;

import com.unimusic.View.NavigationHandler;
import com.unimusic.View.Components.NavBar;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    private BorderPane mainContainer;

    @FXML
    private VBox navBarContainer;

    private NavBar navBar;

    @FXML
    public void initialize() {
        // Set up the navigation bar


        // Register this controller with the ViewNavigator
        NavigationHandler.setMainController(this);

        // Load the Login view by default
        NavigationHandler.navigateToLogin();
    }
    private void setNavBar(NavBar navBar)
    {
        navBar = new NavBar();
        navBarContainer.getChildren().add(navBar);
    }
    /**
     * Set the content of the main area
     */
    public void setContent(Node content) {
        mainContainer.setCenter(content);
    }

    /**
     * Update the navigation bar based on authentication status
     */
    public void updateNavBar(boolean isAuthenticated) {
        navBar.updateAuthStatus(isAuthenticated, NavigationHandler.getAuthenticatedUser());
    }
}