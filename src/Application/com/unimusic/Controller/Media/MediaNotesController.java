package com.unimusic.Controller.Media;

import com.unimusic.Controller.AddMediaNoteController;
import com.unimusic.Controller.Media.Comment.MediaCommentsController;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Media.UniMediaUserNote;
import com.unimusic.Model.Repository.Interface.MediaNoteRepository;
import com.unimusic.Model.Repository.RepositoryFactory;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;

public class MediaNotesController {
    private IMediaMainRegionController parentController;
    private UniMedia currentMedia;
    @FXML private ScrollPane notesAreaParent;
    @FXML private VBox notesArea;
    @FXML private VBox mainArea;
    private MediaNoteRepository noteRepo;
    @FXML
    public void initialize() {
        VBox.setVgrow(mainArea, Priority.ALWAYS);
        noteRepo = RepositoryFactory.getInstance().getMediaNoteRepository();
        ArrayList<UniMediaUserNote> notes = noteRepo.getNotes(currentMedia.getId());
        if (notes == null) return;
        for (UniMediaUserNote note : notes) {
            Separator separator = new Separator();
            separator.setOrientation(Orientation.HORIZONTAL);

            Node view = setMediaNoteView(note);

            notesArea.getChildren().addAll(separator, view);
        }
    }
    private Node setMediaNoteView(UniMediaUserNote metaInfo) {
        try {
            FXMLLoader loader = NavigationHandler.getLoader("Media Main/Media Main East/MediaNote.fxml");
            loader.setControllerFactory(controller -> new MediaUserNoteController(parentController, metaInfo));
            Node view = loader.load();
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: MediaNote.fxml");
        }
        return null;
    }
    public MediaNotesController(IMediaMainRegionController parentController, UniMedia currentMedia){
        this.parentController = parentController;
        this.currentMedia = currentMedia;
    }

    @FXML
    private void onComments()
    {
        parentController.loadView("Media Main/Media Main East/MediaComments.fxml", 0, e -> new MediaCommentsController(parentController, currentMedia));
    }
    @FXML
    private void addNote()
    {
        if (AuthManager.getUser().isGuest()) return;
        loadView("Add Information/AddMediaNote.fxml", c -> new AddMediaNoteController(currentMedia, parentController.getRenderer()));
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
