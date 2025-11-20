package com.unimusic;

import com.unimusic.Config.AppConfig;
import com.unimusic.Controller.UniController;
import com.unimusic.Model.Repository.DataRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    private static final DataRepository dataRepo = new DataRepository();
    public static UniController mainController;
    /**
     * Get the application-wide user repository
     */
    public static DataRepository getDataRepository() {
        return dataRepo;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main application view

        URL mainViewUrl = getClass().getResource("Fxml/Main.fxml");
        FXMLLoader loader = new FXMLLoader(mainViewUrl);

        Parent root = loader.load();
        mainController = loader.getController();
        // Set up the scene
        Scene scene = new Scene(root, AppConfig.APP_WIDTH, AppConfig.APP_HEIGHT);
        URL cssUrl = getClass().getResource("Css/Styles.css");
        scene.getStylesheets().add(cssUrl.toExternalForm());

        // Configure and show the stage
        primaryStage.setTitle("Dash App - JavaFX Version");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
