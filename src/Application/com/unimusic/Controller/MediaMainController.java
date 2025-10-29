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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MediaMainController extends UniController {
    //TODO: to implement the left side business logic
    private Pane leftSideArea;
    @FXML private VBox rightSideArea;
    private UniMedia currentMedia;
    private int rightSideAreaIdx;

    public MediaMainController(UniMedia media)
    {
        currentMedia = media;
        rightSideAreaIdx = -1;
    }
    @FXML
    public void initialize() {
        NavigationHandler.setMediaController(this);
        loadViewOnRight("MediaComments.fxml");
    }
    public UniMedia getCurrentMedia()
    {
        return currentMedia;
    }
    private void loadViewOnRight(String fxml)
    {
        FXMLLoader loader = NavigationHandler.getLoader(fxml);
        loader.setControllerFactory( c -> new MediaCommentsController(currentMedia));
        try {
            Node view = loader.load();
            setRightContent(view);
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
        if (rightSideAreaIdx != 0 | rightSideAreaIdx == -1) {
            rightSideAreaIdx = 0;
            loadViewOnRight("MediaComments.fxml");
        }
    }

    @FXML
    void onLyrics(ActionEvent event) {
        loadViewOnRight("onLyrics.fxml");

    }


    public void setMedia(UniMedia media)
    {
        currentMedia = media;
    }


    private void setRightContent(Node content)
    {

        int size = rightSideArea.getChildren().size();
        // the section is already initialized we nedd to remove before
        // putting something else on it
        if (size > 1)
        {
            rightSideArea.getChildren().remove(1);
        }

        rightSideArea.getChildren().add(content);
    }

    @Override
    public void setContent(Node content, int Idx)
    {
        setRightContent(content);
    }

}
