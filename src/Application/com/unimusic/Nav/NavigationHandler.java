package com.unimusic.Nav;

import com.unimusic.Controller.*;
import com.unimusic.Controller.Media.Comment.MediaCommentsController;
import com.unimusic.Controller.Media.Comment.MediaInDepthCommentsController;
import com.unimusic.Controller.Media.MediaMainController;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.UniComment;
import com.unimusic.Model.Repository.UniData;
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
    private static MediaMainController mediaMainController;
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
        mediaMainController = controller;
    }
    /**
     * Load and switch to a view
     * @param fxml The name of the FXML file to load
     */
    public static void loadHomeView(String fxml, Callback<Class<?>, Object> controllerFactory)
    {
        loadView(fxml, homeController, controllerFactory);
    }
    public static void loadMediaView(String fxml, Callback<Class<?>, Object> controllerFactory){
        loadView(fxml, mediaMainController, controllerFactory);
    }
    public static void loadHomeView(String fxml){
       loadView(fxml, homeController);
    }
    public static void loadMainView(String fxml)
    {
        loadView(fxml, mainController);
    }
    public static void loadMediaView(String fxml)
    {
        loadView(fxml, mediaMainController);
    }
    private static void loadView(String fxml, UniController controller)
    {
        try {
            FXMLLoader loader = getLoader(fxml);
            Node view = loader.load();
            UniController newController = loader.getController();

            newController.setView(view);
            controller.setContent(view, 0);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }
    private static void loadView(String fxml, UniController controller, Callback<Class<?>, Object> controllerFactory)
    {
        try {
            FXMLLoader loader = getLoader(fxml);
            loader.setControllerFactory(controllerFactory);
            Node view = loader.load();
            UniController newController = loader.getController();
            newController.setView(view);
            controller.setContent(view, 0);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }
    public static FXMLLoader getLoader(String fxml){
        URL fxmlUrl = Main.class.getResource("Fxml/" + fxml);
        return new FXMLLoader(fxmlUrl);
    }

    public static void navigateToMediaPlay(UniData data)
    {
        if (AuthManager.isAuthenticated()) {
            UniMedia currentMedia = (UniMedia) data;
            loadHomeView("MediaMain.fxml", c -> {
                return new MediaMainController(currentMedia);
            });
        } else {
            navigateToLogin();
        }
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
    public static void navigateToHome() {
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

    public static void navigateToInDepthCommentSection(UniMedia media, UniComment currentComment, UniComment previousComment, int depthLevel)
    {
        if (AuthManager.isAuthenticated()) {
            loadMediaView("MediaCommentsInDepth.fxml", c -> {
                return new MediaInDepthCommentsController(media, currentComment, previousComment, depthLevel);
            });
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToMainCommentsSection(UniMedia media) {
        if (AuthManager.isAuthenticated()) {
            loadMediaView("MediaComments.fxml", c -> {
                return new MediaCommentsController(media);
            });
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToAddSong() {
        if (AuthManager.isAuthenticated()) {
            loadHomeView("AddSong.fxml", c -> {
                return new SongFormController();
            });
        } else {
            navigateToLogin();
        }
    }
}