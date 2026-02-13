package com.unimusic.Controller.Media.Comment;

import com.unimusic.Controller.Media.IMediaMainRegionController;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Repository.UniComment;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MediaInDepthCommentsController extends MediaCommentsController {
    @FXML private Label parentCommentLabel;
    private MediaCommentsController parentCommentSection;
    private UniComment currentComment;
    private UniComment previousComment;
    public MediaInDepthCommentsController(IMediaMainRegionController parentController, UniMedia media, UniComment currentComment, UniComment previousComment, int depthLevel)
    {
        super(parentController, media);
        this.depthLevel =  depthLevel;
        this.previousComment = previousComment;
        this.currentComment = currentComment;
    }

    @Override
    public void initialize()
    {
        super.initialize();
        populate();
        setVgrow();
    }

    @Override
    protected void setVgrow() {
        VBox.setVgrow(mainArea, Priority.ALWAYS);
        VBox.setVgrow(commentsArea, Priority.ALWAYS);
    }

    private void populate(){
        parentCommentLabel.setText(currentComment.getComment());
    }

    @Override
    protected void loadComments() {
        currentMediaComments = commentRepo.loadComments(currentComment.getId(), depthLevel, true);
    }

    @FXML
    public void onReturnHandler()
    {
        // go to the main commments section
        if (depthLevel == 1)
        {
            NavigationHandler.navigateToMainCommentsSection(parentController, currentMedia);
            return;
        }
        // otherwise go to a lower depth in comment section
        if (previousComment.getParent() == null)
        {
            previousComment = commentRepo.loadComment(previousComment.getId());
        }
        NavigationHandler.navigateToInDepthCommentSection(parentController, currentMedia, previousComment, previousComment.getParent(), depthLevel - 1);
    }

    @Override
    protected void onDepthCommentRequest(UniMedia media, UniComment comment) {
        NavigationHandler.navigateToInDepthCommentSection(parentController, media, comment, currentComment, depthLevel + 1);
    }
    private void populateComments() {

    }

    @Override
    protected UniComment createComment() {
        int mediaId = currentMedia.getId();
        int commentId = commentRepo.getCommentsSize() + 1;
        String commentText = commentField.getText();
        String username = AuthManager.getUser().getUsername();
        UniComment parentComment = currentComment;
        return new UniComment(parentComment, commentId, mediaId, username, commentText, depthLevel);
    }

    @Override
    public void setContent(Node content, int Idx) {

    }
}
