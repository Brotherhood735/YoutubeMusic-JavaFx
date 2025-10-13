package com.unimusic.View;

import com.unimusic.Controller.HomeController;
import com.unimusic.Controller.MediaPlayController;
import com.unimusic.Main;
import com.unimusic.Controller.MainController;
import com.unimusic.Model.AuthManager;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import com.unimusic.Model.UniMedia;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

/**
 * This class handles navigation between different views in the application.
 * It works as a bridge between controllers and views, allowing for simplified navigation.
 */
public class NavigationHandler {
    private static MainController mainController;
    private static MediaPlayController mediaPlayController;
    // Current authenticated username

    /**
     * Set the main controller reference
     * @param controller The MainController instance
     */
    public static void setMainController(MainController controller) {
        mainController = controller;
    }
    /**
     * Load and switch to a view
     * @param fxml The name of the FXML file to load
     */
    public static void loadMainView(String fxml, UniData data) {
        loadView(fxml, mainController, data);
    }
    public static void loadMainView(String fxml)
    {
        loadView(fxml, mainController);
    }
    private static void loadView(String fxml, UniController controller, UniData data)
    {
        try {
            URL fxmlUrl = Main.class.getResource("Fxml/" + fxml);
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Node view = loader.load();
            controller.setContent(view, );
            UniController uniController = loader.getController();
            uniController.setData(data);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }
    private static void loadView(String fxml, UniController controller)
    {
        loadView(fxml, controller, null);
    }
    /**
     * Navigate to the home view
     */
    public static void navigateToHome() {
        loadMainView("WelcomePage.fxml ");
    }

    /**
     * Navigate to the login view
     */
    public static void navigateToLogin() {
        loadMainView("Login.fxml");
    }

    /**
     * Navigate to the register view
     */
    public static void navigateToRegister() {
        loadMainView("Register.fxml");
    }

    /**
     * Navigate to the dashboard view (protected)
     * Will redirect to login if not authenticated
     */
    public static void navigateToDashboard() {
        if (AuthManager.isAuthenticated()) {
            loadMainView("Home.fxml");
        } else {
            navigateToLogin();
        }
    }

    /**
     * Navigate to the profile view (protected)
     * Will redirect to login if not authenticated
     */
    public static void navigateToProfile() {
        if (AuthManager.isAuthenticated()) {
            loadMainView("ProfileView.fxml");
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToStats() {
        if (AuthManager.isAuthenticated()) {
            loadMainView("StatsView.fxml");
        } else {
            navigateToLogin();
        }
    }

    /**
     * Logout the current user
     */
    public static void logout(){
        AuthManager.logout();
        mainController.updateNavBar(false);
        navigateToHome();
    }
}