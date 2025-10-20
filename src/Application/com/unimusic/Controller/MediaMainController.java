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
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MediaMainController extends UniController {
    //TODO: to implement the left side business logic
    private Pane leftSideArea;
    @FXML private ScrollPane rightSideArea;
    private UniMedia currentMedia;

    @FXML
    public void initialize() {
        NavigationHandler.setMediaController(this);
        //loadViewOnRight("onComments.fxml");
    }
    private void loadViewOnRight(String fxml)
    {
        FXMLLoader loader = NavigationHandler.getLoader(fxml);
        try {
            updateRightArea(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private ScrollPane mainView;

    @FXML
    void onOther(ActionEvent event) {
        loadViewOnRight("onOther.fxml");

    }

    @FXML
    void onComments(ActionEvent event) {
        loadViewOnRight("MediaComments.fxml");
    }

    @FXML
    void onLyrics(ActionEvent event) {
        loadViewOnRight("onLyrics.fxml");

    }


    public void setMedia(UniMedia media)
    {
        currentMedia = media;
    }


    private void updateRightArea(Node content)
    {
        rightSideArea.setContent(content);
    }

    @Override
    public void setContent(Node content, int Idx) {

    }

    @Override
    public void setData(UniData data) {
        currentMedia = (UniMedia) data;
    }
}
