package com.unimusic.View;

import com.unimusic.Main;
import com.unimusic.Controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

/**
 * This class handles navigation between different views in the application.
 * It works as a bridge between controllers and views, allowing for simplified navigation.
 */
public class NavigationHandler {
    // Reference to the main controller
    private static MainController mainController;

    // Current authenticated username
    private static String authenticatedUser = null;

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
    public static void loadView(String fxml) {
        try {
            URL fxmlUrl = Main.class.getResource("Fxml/" + fxml);
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Node view = loader.load();
            mainController.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }

    /**
     * Navigate to the home view
     */
    public static void navigateToHome() {
        loadView("WelcomePage.fxml ");
    }

    /**
     * Navigate to the login view
     */
    public static void navigateToLogin() {
        loadView("Login.fxml");
    }

    /**
     * Navigate to the register view
     */
    public static void navigateToRegister() {
        loadView("Register.fxml");
    }

    /**
     * Navigate to the dashboard view (protected)
     * Will redirect to login if not authenticated
     */
    public static void navigateToDashboard() {
        if (isAuthenticated()) {
            loadView("Home.fxml");
        } else {
            navigateToLogin();
        }
    }

    /**
     * Navigate to the profile view (protected)
     * Will redirect to login if not authenticated
     */
    public static void navigateToProfile() {
        if (isAuthenticated()) {
            loadView("ProfileView.fxml");
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToStats() {
        if (isAuthenticated()) {
            loadView("StatsView.fxml");
        } else {
            navigateToLogin();
        }
    }

    /**
     * Set the authenticated user
     * @param username The username of the authenticated user
     */
    public static void setAuthenticatedUser(String username) throws IOException {
        authenticatedUser = username;
        mainController.updateNavBar(isAuthenticated());
    }

    /**
     * Get the authenticated user
     * @return The username of the authenticated user, or null if not authenticated
     */
    public static String getAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Check if a user is authenticated
     * @return true if a user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        return authenticatedUser != null;
    }

    /**
     * Logout the current user
     */
    public static void logout(){
        authenticatedUser = null;
        mainController.updateNavBar(false);
        navigateToHome();
    }
}