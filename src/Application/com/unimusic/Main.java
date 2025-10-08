package com.unimusic;

import com.unimusic.Model.JsonManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main {
    private static JsonManager userRepository = new JsonManager();
    /**
     * Get the application-wide user repository
     */
    public static JsonManager getJsonManager() {
        return userRepository;
    }
    public static void main(String[] args) {
        Application.launch(UniMusicApp.class, args);
    }
}
