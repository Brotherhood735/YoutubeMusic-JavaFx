package com.unimusic.View;

import com.unimusic.Controller.HomeController;
import com.unimusic.Controller.MediaMainController;
import com.unimusic.Main;
import com.unimusic.Controller.MainController;
import com.unimusic.Model.AuthManager;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import com.unimusic.Model.UniMedia;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;

/**
 * This class handles navigation between different views in the application.
 * It works as a bridge between controllers and views, allowing for simplified navigation.
 */
public class NavigationHandler {
    private static MainController mainController;
    private static HomeController homeController;
    private static MediaMainController mediaController;
    // Current authenticated username

    /**
     * Set the main controller reference
     * @param controller The MainController instance
     */
    public static void setMainController(MainController controller) {
        mainController = controller;
    }
    public static void setHomeController(HomeController controller) {
        homeController = controller;
    }
    public static void setMediaController(MediaMainController controller) {
        mediaController = controller;
    }
    /**
     * Load and switch to a view
     * @param fxml The name of the FXML file to load
     */
    public static void loadMainView(String fxml, UniData data) {
        loadView(fxml, mainController, data);
    }
    public static void loadHomeView(String fxml, UniData data)
    {
        loadView(fxml, homeController, data);
    }
    public static void loadHomeView(String fxml, UniData data, Callback<Class<?>, Object> controllerFactory)
    {
        loadView(fxml, homeController, data, controllerFactory);
    }
    public static void loadHomeView(String fxml){
       loadView(fxml, homeController);
    }
    public static void loadMainView(String fxml)
    {
        loadView(fxml, mainController);
    }
    private static void loadView(String fxml, UniController controller, UniData data)
    {
        try {
            FXMLLoader loader = getLoader(fxml);
            Node view = loader.load();
            controller.setContent(view, 0);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }
    private static void loadView(String fxml, UniController controller, UniData data, Callback<Class<?>, Object> controllerFactory)
    {
        try {
            FXMLLoader loader = getLoader(fxml);
            loader.setControllerFactory(controllerFactory);
            Node view = loader.load();
            controller.setContent(view, 0);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }
    private static void loadView(String fxml, UniController controller)
    {
        loadView(fxml, controller, null);
    }
    public static FXMLLoader getLoader(String fxml){
        URL fxmlUrl = Main.class.getResource("Fxml/" + fxml);
        return new FXMLLoader(fxmlUrl);
    }
    public static void navigateToMediaPlay(UniData data)
    {
        if (AuthManager.isAuthenticated()) {
            UniMedia currentMedia = (UniMedia) data;
            loadHomeView("MediaMain.fxml", data, c -> {
                return new MediaMainController(currentMedia);
            });
        } else {
            navigateToLogin();
        }
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