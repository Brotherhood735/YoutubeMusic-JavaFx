package com.unimusic.Nav;

import com.unimusic.Controller.*;
import com.unimusic.Controller.Admin.AdminTabController;
import com.unimusic.Controller.Media.Comment.MediaCommentsController;
import com.unimusic.Controller.Media.Comment.MediaInDepthCommentsController;
import com.unimusic.Controller.Media.IMediaMainRegionController;
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

    public static void setMainController(MainController controller) {
        mainController = controller;
    }
    public static void setHomeController(HomeController controller) {
        homeController = controller;
    }
    public static void setMediaController(MediaMainController controller) {
        mediaMainController = controller;
    }

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
            loadHomeView("Media Main/MediaMain.fxml", c -> {
                return new MediaMainController(currentMedia);
            });
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToLogin() {
        loadMainView("Auth/Login.fxml");
    }

    public static void navigateToRegister() {
        loadMainView("Auth/Register.fxml");
    }

    public static void navigateToHome() {
        if (AuthManager.isAuthenticated()) {
            loadMainView("Main/Home.fxml");
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToInDepthCommentSection(IMediaMainRegionController parentController, UniMedia media, UniComment currentComment, UniComment previousComment, int depthLevel)
    {
        if (AuthManager.isAuthenticated()) {
            loadMediaView("Media Main/Media Main East/MediaCommentsInDepth.fxml", c -> {
                return new MediaInDepthCommentsController(parentController, media, currentComment, previousComment, depthLevel);
            });
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToMainCommentsSection(IMediaMainRegionController parentController, UniMedia media) {
        if (AuthManager.isAuthenticated()) {
            loadMediaView("Media Main/Media Main East/MediaComments.fxml", c -> {
                return new MediaCommentsController(parentController, media);
            });
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToAddSong() {
        if (AuthManager.isAuthenticated()) {
            loadHomeView("Add Information/AddSong.fxml", c -> {
                return new SongFormController();
            });
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToAdminPanel() {
        if (AuthManager.isAuthenticated()) {
            loadHomeView("Admin/AdminTab.fxml", c -> {
                return new AdminTabController();
            });
        } else {
            navigateToLogin();
        }
    }
}