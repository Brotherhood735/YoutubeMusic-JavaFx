package com.unimusic.Controller;

import com.unimusic.Model.UniComment;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class MediaInDepthCommentsController extends MediaCommentsController {
    @FXML private Label parentCommentLabel;
    private MediaCommentsController parentCommentSection;
    private UniComment parentComment;

    public MediaInDepthCommentsController(UniMedia media, UniComment parentComment)
    {
        super(media);
        this.parentComment = parentComment;
    }

    @Override
    public void initialize()
    {
        super.initialize();
        populate();
    }

    private void populate(){
        parentCommentLabel.setText(parentComment.getComment());
    }

    @FXML
    private void onReturnHandler()
    {

    }
    private void populateComments() {

    }

    @Override
    public void setContent(Node content, int Idx) {

    }
}
