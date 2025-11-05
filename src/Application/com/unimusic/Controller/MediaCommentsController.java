package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.*;
import com.unimusic.View.NavigationHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MediaCommentsController extends UniController {

    protected ArrayList<UniComment> currentMediaComments;
    protected UniMedia currentMedia;
    protected CsvDataManager csvDataManager;
    protected int depthLevel = 0;
    @FXML protected VBox commentsArea;
    @FXML protected VBox mainArea;
    @FXML protected TextField commentField;

    public MediaCommentsController(UniMedia media)
    {
        currentMedia = media;
    }
    @FXML
    public void initialize() {
        csvDataManager = Main.getDataManager();
        loadComments();
        // once we have all the comments on that specific media we can populate the UI
        populateSection();
        VBox.setVgrow(mainArea, Priority.ALWAYS);
    }
    protected void loadComments() {
        currentMediaComments = csvDataManager.loadComments(currentMedia.getId(), depthLevel);
    }
    protected void populateSection()
    {
        commentsArea.getChildren().clear();
        for (UniComment comment : currentMediaComments)
        {
            VBox commentBox = createCommentBox(comment);
            commentsArea.getChildren().add(commentBox);
        }
    }

    protected VBox createCommentBox(UniComment comment) {
        Label usernameLabel =  new Label(comment.getUsername());
        Label commentLabel = new Label(comment.getComment());
        Button respondButton = new  Button("See Responses");
        respondButton.setOnMouseClicked( e -> onDepthCommentRequest(currentMedia, comment));

        VBox commentBox = new VBox();

        commentLabel.setWrapText(true);
        usernameLabel.setOpacity(0.5);
        Insets insets = new Insets(5,5,5,5);
        commentBox.setPadding(insets);
        commentBox.setPrefWidth(100);
        commentBox.setPrefHeight(100);
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

        csvDataManager.saveComment(createComment(), depthLevel);
        currentMediaComments = csvDataManager.loadComments(currentMedia.getId(), depthLevel);
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
