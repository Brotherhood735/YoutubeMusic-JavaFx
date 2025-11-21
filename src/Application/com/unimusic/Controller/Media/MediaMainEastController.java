package com.unimusic.Controller.Media;

import com.unimusic.Controller.Media.Comment.MediaCommentsController;
import com.unimusic.Controller.UniController;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Nav.NavigationHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MediaMainEastController extends UniController {
    private UniMedia currentMedia;
    private int areaIdx;
    private AnchorPane parentArea;
    @FXML private VBox mainArea;

    @FXML
    public void initialize() {
        AnchorPane.setTopAnchor(mainArea, 0.0);
        AnchorPane.setBottomAnchor(mainArea, 0.0);
        AnchorPane.setLeftAnchor(mainArea, 0.0);
        AnchorPane.setRightAnchor(mainArea, 0.0);
        MediaMainController.setEastController(this);
        loadView("MediaComments.fxml", 0);
    }

    public MediaMainEastController(UniMedia currentMedia, AnchorPane parentArea) {
        this.currentMedia = currentMedia;
        this.parentArea = parentArea;
        areaIdx = -1;
    }

    @FXML
    void onOther(ActionEvent event) {
        loadView("onOther.fxml", 2);

    }

    @FXML
    void onComments(ActionEvent event) {
        loadView("MediaComments.fxml", 0);
    }

    @FXML
    void onLyrics(ActionEvent event) {
        loadView("onLyrics.fxml", 1);

    }


    @Override
    public void setContent(Node content, int Idx) {

    }

    public void loadView(String fxml, int idx)
    {
        if (idx == areaIdx) return;
        areaIdx = idx;
        FXMLLoader loader = NavigationHandler.getLoader(fxml);
        loader.setControllerFactory( c -> new MediaCommentsController(currentMedia));
        try {
            Node view = loader.load();
            setContent(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setContent(Node content)
    {

        int size = mainArea.getChildren().size();
        // the section is already initialized we nedd to remove before
        // putting something else on it
        if (size > 1)
        {
            mainArea.getChildren().remove(1);
        }

        mainArea.getChildren().add(content);
    }
}
