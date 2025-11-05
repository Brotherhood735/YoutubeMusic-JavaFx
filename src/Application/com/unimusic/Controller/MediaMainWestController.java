package com.unimusic.Controller;

import com.unimusic.Model.UniController;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.Components.LocalAudioRenderer;
import com.unimusic.View.Components.LocalVideoRenderer;
import com.unimusic.View.Components.MediaRenderer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MediaMainWestController extends UniController {

    private MediaControlController bottomController;
    private UniMedia currentMedia;
    private AnchorPane parentArea;
    private LocalVideoRenderer localVideoRenderer;
    private LocalAudioRenderer localAudioRenderer;
    private MediaRenderer currentRenderer;
    private MediaView mediaView;
    private ImageView imageView;
    @FXML private VBox mainArea;



    public MediaMainWestController(UniMedia currentMedia, AnchorPane westArea, MediaControlController bottomController) {
        this.currentMedia = currentMedia;
        this.parentArea = westArea;
        this.bottomController = bottomController;
    }
    @FXML
    public void initialize() {
        AnchorPane.setTopAnchor(mainArea, 0.0);
        AnchorPane.setBottomAnchor(mainArea, 0.0);
        AnchorPane.setLeftAnchor(mainArea, 0.0);
        AnchorPane.setRightAnchor(mainArea, 0.0);
        MediaMainController.setWestController(this);
        // the default is to go for the video
        onAudioRequest();
    }

    @FXML
    private void onVideoRequest()
    {
        if (localVideoRenderer != null) return;
        if (localAudioRenderer != null)
        {
            localAudioRenderer.pauseMedia();
            localAudioRenderer.getMediaPlayer().dispose();
            localAudioRenderer = null;
            imageView = null;
        }
        mediaView = new MediaView();
        bindViewDimensionsToParentArea(mediaView);
        replaceOrAddViewToMainArea(mediaView);
        localVideoRenderer = new LocalVideoRenderer("tutor.mp4", mediaView, currentMedia);
        currentRenderer = localVideoRenderer;
        bottomController.setData(getRenderer(), currentMedia, true);
        localVideoRenderer.render();
    }

    @FXML
    private void onAudioRequest()
    {
        if (localAudioRenderer != null) return;
        if (localVideoRenderer != null)
        {
            localVideoRenderer.pauseMedia();
            localVideoRenderer.getMediaPlayer().dispose();
            localVideoRenderer = null;
            mediaView = null;
        }
        imageView = new ImageView();
        replaceOrAddViewToMainArea(imageView);
        bindViewDimensionsToParentArea(imageView);
        localAudioRenderer = new LocalAudioRenderer("Convo.mp3", imageView, currentMedia);
        currentRenderer = localAudioRenderer;
        bottomController.setData(getRenderer(), currentMedia, true);
        localAudioRenderer.render();
    }


    private void bindViewDimensionsToParentArea(Node view) {
        if (view instanceof MediaView mediaViewNode) {
            mediaViewNode.fitHeightProperty().bind(parentArea.heightProperty().subtract(100));
            mediaViewNode.fitWidthProperty().bind(parentArea.widthProperty().subtract(100));
        } else if (view instanceof ImageView imageViewNode) {
            imageViewNode.fitHeightProperty().bind(parentArea.heightProperty().subtract(100));
            imageViewNode.fitWidthProperty().bind(parentArea.widthProperty().subtract(100));
        }
    }

    private void replaceOrAddViewToMainArea(Node view) {
        if (mainArea.getChildren().size() > 1) {
            mainArea.getChildren().remove(1);
            mainArea.getChildren().add(1, view);
        } else {
            mainArea.getChildren().add(view);
        }
    }
    @FXML
    private void onYoutubeRequest()
    {
    }


    @Override
    public void setContent(Node content, int Idx) {

    }

    public MediaRenderer getRenderer() {
        return currentRenderer;
    }
}
