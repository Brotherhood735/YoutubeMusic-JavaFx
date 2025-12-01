package com.unimusic.Controller;

import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public  class MainController extends UniController {
    @FXML
    private BorderPane mainContainer;


    @FXML
    public void initialize() throws IOException {
        System.out.println(mainContainer.getStyleClass());
        // Set up the navigation bar
        //setNavBar();
        // Register this controller with the ViewNavigator
        NavigationHandler.setMainController(this);
        // Load the Login view by default
        NavigationHandler.navigateToLogin();
    }
    /**
     * Set the content of the main area
     */
    @Override
    public void setContent(Node content, int Idx) {
        mainContainer.setCenter(content);
    }


}