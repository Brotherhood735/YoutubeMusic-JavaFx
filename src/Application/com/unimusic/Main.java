package com.unimusic;

import com.unimusic.Config.AppConfig;
import com.unimusic.Controller.UniController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public static UniController mainController;
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL mainViewUrl = getClass().getResource("Fxml/Root.fxml");
        FXMLLoader loader = new FXMLLoader(mainViewUrl);

        Parent root = loader.load();
        mainController = loader.getController();
        // Set up the scene
        Scene scene = new Scene(root, AppConfig.APP_WIDTH, AppConfig.APP_HEIGHT);
        URL paneCssUrl = getClass().getResource("Css/Pane.css");
        URL datePickerCssUrl = getClass().getResource("Css/DatePicker.css");
        URL formCssUrl = getClass().getResource("Css/Form.css");
        URL labelCssUrl = getClass().getResource("Css/Label.css");
        URL buttonCssUrl = getClass().getResource("Css/Button.css");
        URL v_hBoxCssUrl = getClass().getResource("Css/V_HBox.css");
        URL ScrollBarCssUrl = getClass().getResource("Css/ScrollBar.css");
        URL SliderCssUrl = getClass().getResource("Css/Slider.css");
        URL TextFieldCssUrl = getClass().getResource("Css/TextField.css");
        URL CardCssUrl = getClass().getResource("Css/Card.css");

        scene.getStylesheets().add(paneCssUrl.toExternalForm());
        scene.getStylesheets().add(datePickerCssUrl.toExternalForm());
        scene.getStylesheets().add(formCssUrl.toExternalForm());
        scene.getStylesheets().add(labelCssUrl.toExternalForm());
        scene.getStylesheets().add(buttonCssUrl.toExternalForm());
        scene.getStylesheets().add(v_hBoxCssUrl.toExternalForm());
        scene.getStylesheets().add(ScrollBarCssUrl.toExternalForm());
        scene.getStylesheets().add(SliderCssUrl.toExternalForm());
        scene.getStylesheets().add(TextFieldCssUrl.toExternalForm());
        scene.getStylesheets().add(CardCssUrl.toExternalForm());

        primaryStage.setTitle("UniMusicApp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
