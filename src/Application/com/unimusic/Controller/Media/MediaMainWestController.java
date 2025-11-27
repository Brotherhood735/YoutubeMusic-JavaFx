package com.unimusic.Controller.Media;

import com.unimusic.Controller.MediaMetaInfoController;
import com.unimusic.Controller.UniController;
import com.unimusic.Main;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Media.UniMediaMetaInfo;
import com.unimusic.Model.Repository.MediaMetaInfoRepository;
import com.unimusic.Nav.NavigationHandler;
import com.unimusic.View.Components.LocalPlayableAudio;
import com.unimusic.View.Components.LocalPlayableVideo;
import com.unimusic.View.Components.PlayableMedia;
import com.unimusic.View.Components.OnlineVideoPlayable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.ArrayList;

public class MediaMainWestController extends UniController {

    private MediaControlController bottomController;
    private UniMedia currentMedia;
    private AnchorPane parentArea;
    private LocalPlayableVideo localVideoRenderer;
    private LocalPlayableAudio localAudioRenderer;
    private OnlineVideoPlayable onlineVideoRenderer;
    private PlayableMedia currentRenderer;

    @FXML private Label strumentiLabel;
    @FXML private Label dataLabel;
    @FXML private Label luogoLabel;

    private MediaView mediaView;
    private ImageView imageView;
    private WebView webView;
    @FXML private VBox mainArea;
    @FXML private Button videoRequestButton;
    @FXML private Button audioRequestButton;
    @FXML private Button webRequestButton;
    @FXML private VBox additionalInfoArea;

    @FXML private MediaMetaInfoRepository metaInfoRepo;

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
        metaInfoRepo = Main.getDataRepository();

        setDefaultInfo();
        // the default is to go for the video
        startDefaultRenderer();

        ArrayList<UniMediaMetaInfo> metaInfos = metaInfoRepo.getMetaInfo(currentMedia.getId());
        if (metaInfos == null) return;
        for (UniMediaMetaInfo metaInfo : metaInfos) {

            Separator separator = new Separator();
            separator.setOrientation(Orientation.HORIZONTAL);

            Node view = setMetaInfoView(metaInfo);

            additionalInfoArea.getChildren().addAll(separator, view);
        }
    }

    private Node setMetaInfoView(UniMediaMetaInfo metaInfo) {
        try {
            FXMLLoader loader = NavigationHandler.getLoader("MediaMetaInfo.fxml");
            loader.setControllerFactory(controller -> new MediaMetaInfoController(metaInfo));
            Node view = loader.load();
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: MediaMetaInfo.fxml");
        }
        return null;
    }

    private void setDefaultInfo() {
        strumentiLabel.setText(notProvidedChecker(currentMedia.getInstruments()));
        dataLabel.setText(notProvidedChecker(currentMedia.getDate()));
        luogoLabel.setText(notProvidedChecker(currentMedia.getPlace()));
    }
    private String notProvidedChecker(String str){
        return str == null ? "Non fornito" : str;
    }
    private void startDefaultRenderer() {
        boolean isRequested = false;
        if (currentMedia.getVideoName() != null) {
            videoRequestButton.setDisable(false);
            onVideoRequest();
            isRequested = true;
        }
        if (currentMedia.getAudioName() != null)
        {
            audioRequestButton.setDisable(false);
            if(!isRequested)
            {
                onAudioRequest();
                isRequested = true;
            };
        }
        if (currentMedia.getWebUrl() != null)
        {
            webRequestButton.setDisable(false);
            if(!isRequested)
            {
                onWebRequest();
            };
        }
    }

    @FXML
    private void onVideoRequest() {
        if (localVideoRenderer != null) return;
        MediaView mediaViewNode = new MediaView();
        mediaView = mediaViewNode;
        switchRenderer(mediaViewNode, new LocalPlayableVideo(currentMedia.getVideoName(), mediaViewNode, currentMedia));
    }

    @FXML
    private void onAudioRequest() {
        if (localAudioRenderer != null) return;
        ImageView imageViewNode = new ImageView();
        imageView = imageViewNode;
        switchRenderer(imageViewNode, new LocalPlayableAudio(currentMedia.getAudioName(), imageViewNode, currentMedia));
    }

    @FXML
    private void onWebRequest() {
        if (onlineVideoRenderer != null) return;
        WebView webViewNode = new WebView();
        webView = webViewNode;
        switchRenderer(webViewNode, new OnlineVideoPlayable(currentMedia.getWebUrl(), webViewNode, currentMedia));
    }
    private void switchRenderer(Node view, PlayableMedia newRenderer) {
        // Stop all existing renderers
        if (localVideoRenderer != null) localVideoRenderer.stopRender();
        if (localAudioRenderer != null) localAudioRenderer.stopRender();
        if (onlineVideoRenderer != null) onlineVideoRenderer.stopRender();

        // Nullify old references
        localVideoRenderer = null;
        localAudioRenderer = null;
        onlineVideoRenderer = null;
        mediaView = null;
        imageView = null;
        webView = null;

        // Add and bind view
        replaceOrAddViewToMainArea(view);
        bindViewDimensionsToParentArea(view);

        // Set the new renderer
        currentRenderer = newRenderer;
        if (newRenderer instanceof LocalPlayableVideo) localVideoRenderer = (LocalPlayableVideo) newRenderer;
        else if (newRenderer instanceof LocalPlayableAudio) localAudioRenderer = (LocalPlayableAudio) newRenderer;
        else if (newRenderer instanceof OnlineVideoPlayable) onlineVideoRenderer = (OnlineVideoPlayable) newRenderer;

        bottomController.setData(getRenderer(), currentMedia, true);
        newRenderer.render();
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
        if (mainArea.getChildren().size() > 2) {
            mainArea.getChildren().remove(1);
            mainArea.getChildren().add(1, view);
        } else {
            mainArea.getChildren().add(1,view);
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
