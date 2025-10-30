package com.unimusic.Controller;

import com.unimusic.Model.AuthManager;
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

    @Override
    protected void loadComments() {
        currentMediaComments = dataManager.loadComments(currentComment.getId(), depthLevel, true);
    }

    @FXML
    private void onReturnHandler()
    {
        // go to the main commments section
        if (depthLevel == 1)
        {
            NavigationHandler.navigateToMainCommentsSection(currentMedia);
            return;
        }
        // otherwise go to a lower depth in comment section
        NavigationHandler.navigateToInDepthCommentSection(currentMedia, previousComment, previousComment.getParent(), depthLevel - 1);
    }

    @Override
    protected void onDepthCommentRequest(UniMedia media, UniComment comment) {
        NavigationHandler.navigateToInDepthCommentSection(media, comment, currentComment, depthLevel + 1);
    }
    private void populateComments() {

    }

    @Override
    protected UniComment createComment() {
        int mediaId = currentMedia.getId();
        int commentId = Integer.parseInt(String.valueOf(currentComment.getId()) + (currentMediaComments.size() + 1));
        String commentText = commentField.getText();
        String username = AuthManager.getUser();
        UniComment parentComment = currentComment;
        return new UniComment(parentComment, commentId, mediaId, username, commentText, depthLevel);
    }

    @Override
    public void setContent(Node content, int Idx) {

    }
}
