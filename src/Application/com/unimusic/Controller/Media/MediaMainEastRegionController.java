package com.unimusic.Controller.Media;

import com.unimusic.Controller.AddMediaMetaInfoController;
import com.unimusic.Controller.Media.Comment.MediaCommentsController;
import com.unimusic.Controller.UniController;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Nav.NavigationHandler;
import com.unimusic.View.Components.PlayableMedia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;

public class MediaMainEastRegionController extends UniController implements IMediaMainRegionController {
    private UniMedia currentMedia;
    private int areaIdx;
    private AnchorPane parentArea;
    private MediaControlController bottomController;
    @FXML private VBox mainArea;

    @FXML
    public void initialize() {
        AnchorPane.setTopAnchor(mainArea, 0.0);
        AnchorPane.setBottomAnchor(mainArea, 0.0);
        AnchorPane.setLeftAnchor(mainArea, 0.0);
        AnchorPane.setRightAnchor(mainArea, 0.0);
        MediaMainController.setEastController(this);
        loadView("Media Main/Media Main East/MediaComments.fxml", 0, c -> new MediaCommentsController(this, currentMedia));
    }

    public PlayableMedia getRenderer() {
        return bottomController.getRenderer();
    }
    public MediaMainEastRegionController(UniMedia currentMedia, AnchorPane parentArea, MediaControlController bottomController) {
        this.currentMedia = currentMedia;
        this.parentArea = parentArea;
        this.bottomController = bottomController;
        areaIdx = -1;
    }

    @FXML
    void onOther(ActionEvent event) {
        if (AuthManager.getUser().isGuest()) return;
        loadView("Add Information/AddMediaMetaInfo.fxml", 2, c -> new AddMediaMetaInfoController(currentMedia, bottomController.getRenderer()));

    }

    @FXML
    void onComments(ActionEvent event) {
        loadView("Media Main/Media Main East/MediaComments.fxml", 0, c -> new MediaCommentsController(this, currentMedia));
    }

    @FXML
    void onLyrics(ActionEvent event) {
        loadView("Media Main/Media Main East/MediaDocuments.fxml", 1, c -> new MediaDocumentsController(currentMedia));
    }


    @Override
    public void setContent(Node content, int Idx) {

    }

    public void loadView(String fxml, int idx, Callback<Class<?>, Object> controllerFactory)
    {
        if (idx == areaIdx) return;
        areaIdx = idx;
        FXMLLoader loader = NavigationHandler.getLoader(fxml);
        loader.setControllerFactory(controllerFactory);
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
        // the section is already initialized
        if (size > 1)
        {
            mainArea.getChildren().remove(1);
        }

        mainArea.getChildren().add(content);
    }
}
