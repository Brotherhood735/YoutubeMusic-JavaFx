package com.unimusic;

import com.unimusic.Nav.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;



public class UniMusicApp extends Application {
    public static String LoginFxmlFileName = "Fxml/Login.fxml";
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);
        SceneManager.changeScene(LoginFxmlFileName);
        SceneManager.setTitle("UniMusicApp");
    }
}
