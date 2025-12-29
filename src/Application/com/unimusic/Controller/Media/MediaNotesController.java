package com.unimusic.Controller.Media;

import com.unimusic.Controller.AddMediaNoteController;
import com.unimusic.Controller.Media.Comment.MediaCommentsController;
import com.unimusic.Main;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Media.UniMediaSegmentInfo;
import com.unimusic.Model.Repository.MediaNoteRepository;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class MediaNotesController {
    private MediaMainEastController parentController;
    private UniMedia currentMedia;
    @FXML private ScrollPane notesAreaParent;
    @FXML private VBox notesArea;
    private MediaNoteRepository noteRepo;
    @FXML
    public void initialize() {
        noteRepo = Main.getDataRepository();
        ArrayList<UniMediaSegmentInfo> metaInfos = noteRepo.getNotes(currentMedia.getId());
        if (metaInfos == null) return;
        for (UniMediaSegmentInfo metaInfo : metaInfos) {

            Separator separator = new Separator();
            separator.setOrientation(Orientation.HORIZONTAL);

            Node view = setMediaNoteView(metaInfo);

            notesArea.getChildren().addAll(separator, view);
        }
    }
    private Node setMediaNoteView(UniMediaSegmentInfo metaInfo) {
        try {
            FXMLLoader loader = NavigationHandler.getLoader("MediaMetaInfo.fxml");
            loader.setControllerFactory(controller -> new MediaMetaInfoController(parentController, metaInfo));
            Node view = loader.load();
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: MediaMetaInfo.fxml");
        }
        return null;
    }
    public MediaNotesController(MediaMainEastController parentController, UniMedia currentMedia){
        this.parentController = parentController;
        this.currentMedia = currentMedia;
    }

    @FXML
    private void onComments()
    {
        parentController.loadView("MediaComments.fxml", 0, e -> new MediaCommentsController(parentController, currentMedia));

    }
    @FXML
    private void addNote()
    {
        loadView("AddMediaNote.fxml", c -> new AddMediaNoteController(currentMedia, parentController.getRenderer()));
    }

    public void loadView(String fxml, Callback<Class<?>, Object> controllerFactory)
    {
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
        notesAreaParent.setContent(content);
    }
}
