package com.unimusic.Controller.Media;

import com.unimusic.Model.Media.UniMediaUserNote;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MediaUserNoteController extends MediaMetaInfoController {

    @FXML private Label userLabel;
    private UniMediaUserNote mediaUserNote;
    public MediaUserNoteController(IMediaMainRegionController parentController, UniMediaUserNote mediaUserNote) {
        super(parentController, mediaUserNote);
        this.mediaUserNote = mediaUserNote;
    }

    @Override
    public void initialize() {
        super.initialize();
        userLabel.setText(mediaUserNote.getUsername());
    }
}
