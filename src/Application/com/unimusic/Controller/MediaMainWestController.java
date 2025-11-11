package com.unimusic.Controller;

import com.unimusic.Model.UniController;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.Components.LocalPlayableAudio;
import com.unimusic.View.Components.LocalPlayableVideo;
import com.unimusic.View.Components.PlayableMedia;
import com.unimusic.View.Components.OnlineVideoPlayable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;

public class MediaMainWestController extends UniController {

    private MediaControlController bottomController;
    private UniMedia currentMedia;
    private AnchorPane parentArea;
    private LocalPlayableVideo localVideoRenderer;
    private LocalPlayableAudio localAudioRenderer;
    private OnlineVideoPlayable onlineVideoRenderer;
    private PlayableMedia currentRenderer;
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

        if(localAudioRenderer != null) localAudioRenderer.stopRender();
        if(onlineVideoRenderer != null) onlineVideoRenderer.stopRender();
        localAudioRenderer = null;
        onlineVideoRenderer = null;
        mediaView = null;
        imageView = null;

        mediaView = new MediaView();
        bindViewDimensionsToParentArea(mediaView);
        replaceOrAddViewToMainArea(mediaView);
        localVideoRenderer = new LocalPlayableVideo("tutor.mp4", mediaView, currentMedia);
        currentRenderer = localVideoRenderer;
        bottomController.setData(getRenderer(), currentMedia, true);
        localVideoRenderer.render();
    }

    @FXML
    private void onAudioRequest()
    {
        if (localAudioRenderer != null) return;

        if(localVideoRenderer != null) localVideoRenderer.stopRender();
        if(onlineVideoRenderer != null) onlineVideoRenderer.stopRender();

        localVideoRenderer = null;
        onlineVideoRenderer = null;
        mediaView = null;

        imageView = new ImageView();
        replaceOrAddViewToMainArea(imageView);
        bindViewDimensionsToParentArea(imageView);
        localAudioRenderer = new LocalPlayableAudio("Convo.mp3", imageView, currentMedia);
        currentRenderer = localAudioRenderer;
        bottomController.setData(getRenderer(), currentMedia, true);
        localAudioRenderer.render();
    }
    @FXML
    private void onYoutubeRequest()
    {
        if (onlineVideoRenderer != null) return;

        if(localVideoRenderer != null) localVideoRenderer.stopRender();
        if(localAudioRenderer != null) localAudioRenderer.stopRender();

        localVideoRenderer = null;
        localAudioRenderer = null;
        imageView = null;

        imageView = new ImageView();
        replaceOrAddViewToMainArea(imageView);
        bindViewDimensionsToParentArea(imageView);
        onlineVideoRenderer = new OnlineVideoPlayable("https://www.youtube.com/watch?v=dQw4w9WgXcQ", imageView, currentMedia);
        currentRenderer = onlineVideoRenderer;
        bottomController.setData(getRenderer(), currentMedia, true);
        onlineVideoRenderer.render();
    }

    private void bindViewDimensionsToParentArea(Node view) {
        if (view instanceof MediaView mediaViewNode) {
            mediaViewNode.fitHeightProperty().bind(parentArea.heightProperty().subtract(100));
            mediaViewNode.fitWidthProperty().bind(parentArea.widthProperty().subtract(100));
        } else if (view instanceof ImageView imageViewNode) {
            imageViewNode.fitHeightProperty().bind(parentArea.heightProperty().subtract(100));
            imageViewNode.fitWidthProperty().bind(parentArea.widthProperty().subtract(100));
        } else if (view instanceof WebView webViewNode) {
            webViewNode.prefWidthProperty().bind(parentArea.widthProperty());
            webViewNode.prefHeightProperty().bind(parentArea.heightProperty());
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



    @Override
    public void setContent(Node content, int Idx) {

    }

    public PlayableMedia getRenderer() {
        return currentRenderer;
    }

    public void stopRenderer()
    {
        if (currentRenderer != null)
        {
            currentRenderer.pauseMedia();
            currentRenderer.getLocalMediaPlayer().dispose();
        }
    }
}
