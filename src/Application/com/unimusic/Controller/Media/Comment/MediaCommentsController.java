package com.unimusic.Controller.Media.Comment;

import com.unimusic.Controller.UniController;
import com.unimusic.Main;
import com.unimusic.Model.Authentication.AuthManager;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.CommentRepository;
import com.unimusic.Model.Repository.UniComment;
import com.unimusic.Nav.NavigationHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class MediaCommentsController extends UniController {

    protected ArrayList<UniComment> currentMediaComments;
    protected UniMedia currentMedia;
    protected CommentRepository commentRepo;
    protected int depthLevel = 0;
    @FXML protected ScrollPane commentsAreaParent;
    @FXML protected VBox commentsArea;
    @FXML protected VBox mainArea;
    @FXML protected TextField commentField;

    public MediaCommentsController(UniMedia media)
    {
        currentMedia = media;
    }
    @FXML
    public void initialize() {
        commentRepo = Main.getDataRepository();
        loadComments();
        // once we have all the comments on that specific media we can populate the UI
        populateSection();
        VBox.setVgrow(mainArea, Priority.ALWAYS);
    }
    protected void loadComments() {
        currentMediaComments = commentRepo.loadComments(currentMedia.getId(), depthLevel);
    }
    protected void populateSection()
    {
        commentsAreaParent.setFitToWidth(true);
        commentsAreaParent.setFitToHeight(false);
        commentsArea.setFillWidth(true);
        commentsArea.getChildren().clear();
        for (UniComment comment : currentMediaComments)
        {
            VBox commentBox = createCommentBox(comment);
            Separator seperator = new Separator();
            seperator.setOrientation(Orientation.HORIZONTAL);
            commentsArea.getChildren().add(commentBox);
            commentsArea.getChildren().add(seperator);
        }
    }

    protected VBox createCommentBox(UniComment comment) {
        Label usernameLabel =  new Label(comment.getUsername());
        Label commentLabel = new Label(comment.getComment());
        commentLabel.setPrefHeight(3 * commentLabel.getFont().getSize() * 1.2);
        Button respondButton = new  Button("See Responses");
        respondButton.setOnMouseClicked( e -> onDepthCommentRequest(currentMedia, comment));
        VBox commentBox = new VBox();
        commentBox.setFillWidth(true);
        commentLabel.setWrapText(true);
        commentLabel.setMaxWidth(Double.MAX_VALUE);
        usernameLabel.setOpacity(0.5);
        Insets insets = new Insets(5,5,5,5);
        commentBox.setPadding(insets);
        commentBox.setPrefWidth(200);
        commentBox.setPrefHeight(200);
        commentBox.getChildren().addAll(usernameLabel, commentLabel,  respondButton);
        return commentBox;
    }

    protected void onDepthCommentRequest(UniMedia media,  UniComment comment)
    {
        NavigationHandler.navigateToInDepthCommentSection(media, comment, null, depthLevel + 1);
    }

    @FXML
    protected void sendComment()
    {

        commentRepo.saveComment(createComment(), depthLevel);
        currentMediaComments = commentRepo.loadComments(currentMedia.getId(), depthLevel);
        loadComments();
        populateSection();
    }
    // will create the comment instance by using the current values of the UI
    // elements and internal datas
    protected UniComment  createComment()
    {
        int mediaId = currentMedia.getId();
        int commentId = currentMediaComments.size() + 1;
        String commentText = commentField.getText();
        String username = AuthManager.getUser();
        return new UniComment(null, commentId, mediaId, username, commentText, depthLevel);
    }
    @Override
    public void setContent(Node content, int Idx)
    {

    }

}
