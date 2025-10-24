package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MediaCommentsController extends UniController {

    private ArrayList<UniComment> currentMediaComments;
    private UniMedia currentMedia;
    private DataManager dataManager;
    @FXML private VBox commentsArea;
    @FXML private ScrollPane mainArea;
    @FXML private TextField commentField;

    public MediaCommentsController(UniMedia media)
    {
        currentMedia = media;
    }
    @FXML
    public void initialize() {
        dataManager = Main.getDataManager();
        loadComments();
        // once we have all the comments on that specific media we can populate the UI
        populateSection();
    }
    private void loadComments() {
        currentMediaComments = dataManager.loadComments(currentMedia.getId());
    }
    private void populateSection()
    {
        for (UniComment comment : currentMediaComments)
        {
            VBox commentBox = createCommentBox(comment);
            commentsArea.getChildren().add(commentBox);
        }
    }

    private VBox createCommentBox(UniComment comment) {
        Label usernameLabel =  new Label(comment.getUsername());
        Label commentLabel = new Label(comment.getComment());
        VBox commentBox = new VBox();
        commentBox.setPrefWidth(100);
        commentBox.setPrefHeight(100);
        commentBox.getChildren().addAll(usernameLabel, commentLabel);
        return commentBox;
    }

    @FXML
    private void sendComment()
    {
        dataManager.saveComment(currentMediaComments, createComment());
        loadComments();
    }
    // will create the comment instance by using the current values of the UI
    // elements and internal datas
    private UniComment  createComment()
    {
        double mediaId = currentMedia.getId();
        double commentId = currentMediaComments.getLast().getId() + 1;
        String commentText = commentField.getText();
        String username = AuthManager.getUser();
        return new UniComment(commentId, mediaId, username, commentText);
    }
    @Override
    public void setContent(Node content, int Idx) {

    }

}
