package com.unimusic.Controller;

import com.unimusic.Model.UniComment;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class MediaInDepthCommentsController extends MediaCommentsController {
    @FXML private Label parentCommentLabel;
    private MediaCommentsController parentCommentSection;
    private UniComment currentComment;
    private UniComment previousComment;

    public MediaInDepthCommentsController(UniMedia media, UniComment currentComment, UniComment previousComment, int depthLevel)
    {
        super(media);
        this.depthLevel =  depthLevel;
        this.previousComment = previousComment;
        this.currentComment = currentComment;
    }

    @Override
    public void initialize()
    {
        super.initialize();
        populate();
    }

    private void populate(){
        parentCommentLabel.setText(currentComment.getComment());
    }

    @FXML
    private void onReturnHandler()
    {
        // go to the main commments section
        if (depthLevel == 1)
        {
            NavigationHandler.navigateToMediaPlay(currentMedia);
            return;
        }
        // otherwise go to a lower depth in comment section
        NavigationHandler.navigateToInDepthCommentSection(currentMedia, previousComment, null, depthLevel - 1);
    }

    @Override
    protected void onDepthCommentRequest(UniMedia media, UniComment comment) {
        NavigationHandler.navigateToInDepthCommentSection(media, comment, currentComment, depthLevel + 1);
    }
    private void populateComments() {

    }

    @Override
    public void setContent(Node content, int Idx) {

    }
}
