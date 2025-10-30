package com.unimusic.Controller;

import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.NavigationHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;

public class MediaMainController extends UniController {
    //TODO: to implement the left side business logic
    private Pane leftSideArea;
    private static MediaControlController bottomController;
    private static MediaMainEastController eastController;
    private static MediaMainWestController westController;
    private UniMedia currentMedia;

    @FXML private BorderPane root;
    @FXML private AnchorPane eastArea;
    @FXML private AnchorPane westArea;
    public MediaMainController(UniMedia media)
    {
        currentMedia = media;
    }

    @FXML
    public void initialize() {
        NavigationHandler.setMediaController(this);
        HomeController.setMediaMainController(this);
        loadView("MediaMainEast.fxml", eastArea,
                c -> { return new MediaMainEastController(currentMedia, eastArea);});
        loadView("MediaMainWest.fxml", westArea,
                c -> { return new MediaMainWestController(currentMedia, westArea);});
        bottomController.setData(westController.getRenderer(), currentMedia, true);
    }

    public static void setEastController(MediaMainEastController controller)
    {
        eastController = controller;
    }
    public static void setWestController(MediaMainWestController controller) {
        westController = controller;
    }
    public static void setBottomController(MediaControlController controller) {
        bottomController = controller;
    }
    private void loadView(String fxml, AnchorPane area, Callback<Class<?>, Object> controllerFactory)
    {
        area.getChildren().clear();
        try {
            FXMLLoader loader = NavigationHandler.getLoader(fxml);
            loader.setControllerFactory(controllerFactory);
            Node view = loader.load();
            area.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }

    public UniMedia getCurrentMedia()
    {
        return currentMedia;
    }

    @FXML
    private ScrollPane mainView;



    @Override
    public void setContent(Node content, int Idx)
    {
        eastController.setContent(content);
    }

}
