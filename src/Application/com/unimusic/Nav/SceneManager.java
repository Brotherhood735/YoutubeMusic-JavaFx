package com.unimusic.Nav;

import com.unimusic.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneManager {

    public static final double defaultWidth = 600;
    public static final double defaultHeight = 400;
    private static Stage currentStage;

    public static void setStage(Stage stage) {
        currentStage = stage;
    }
    public static void changeScene(String fxmlName) throws IOException
    {
        changeScene(fxmlName, defaultWidth, defaultHeight);
    }
    public static void changeScene(String fxmlName, double width, double height) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlName));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        changeScene(scene);
    }
    public static void changeScene(Scene scene) throws IOException
    {
        currentStage.setScene(scene);
        currentStage.show();
    }
    public static void setTitle(String title)
    {
        currentStage.setTitle(title);
    }
}