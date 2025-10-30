package com.unimusic.Controller;

import com.unimusic.Config.AppConfig;
import com.unimusic.Main;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.Components.VideoRenderer;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;

public class MediaMainWestController extends UniController {

    private UniMedia currentMedia;
    private AnchorPane parentArea;
    private VideoRenderer videoRenderer;


    @FXML private MediaView mediaView;
    @FXML private VBox mainArea;



    public MediaMainWestController(UniMedia currentMedia, AnchorPane westArea) {
        this.currentMedia = currentMedia;
        this.parentArea = westArea;
    }
    @FXML
    public void initialize() {
        AnchorPane.setTopAnchor(mainArea, 0.0);
        AnchorPane.setBottomAnchor(mainArea, 0.0);
        AnchorPane.setLeftAnchor(mainArea, 0.0);
        AnchorPane.setRightAnchor(mainArea, 0.0);
        mediaView.fitHeightProperty().bind(parentArea.heightProperty().subtract(100));
        mediaView.fitWidthProperty().bind(parentArea.widthProperty().subtract(100));
        MediaMainController.setWestController(this);
        // the default is to go for the video
        onVideoRequest();
    }

    @FXML
    private void onVideoRequest()
    {
        videoRenderer = new VideoRenderer(AppConfig.DATA_DIR + "/one piece.mp4");
        MediaPlayer video = videoRenderer.getVideo();

        mediaView.setPreserveRatio(true);
        mediaView.setMediaPlayer(video);
    }

    @FXML
    private void onAudioRequest()
    {
    }
    @FXML
    private void onYoutubeRequest()
    {
    }


    @Override
    public void setContent(Node content, int Idx) {

    }

    public VideoRenderer getRenderer() {
        return videoRenderer;
    }
}
