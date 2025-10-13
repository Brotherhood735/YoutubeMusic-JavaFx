package com.unimusic.Controller;

import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import com.unimusic.Model.UniMedia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class MediaPlayController extends UniController {

    private UniMedia currentMedia;

    @FXML
    private ScrollPane mainView;

    @FXML
    void onOther(ActionEvent event) {

    }

    @FXML
    void onComments(ActionEvent event) {

    }

    @FXML
    void onLyrics(ActionEvent event) {

    }


    public void setMedia(UniMedia media)
    {
        currentMedia = media;
    }

    @Override
    public void setContent(Node content, int Idx) {

    }

    @Override
    public void setData(UniData data) {
        currentMedia = (UniMedia) data;
    }
}
