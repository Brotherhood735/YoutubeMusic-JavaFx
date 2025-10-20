package com.unimusic.Model;

public class UniComment extends UniData{
    private double commentId;
    private double mediaId;
    private String username;
    private String comment;
    public UniComment(double commentId, double mediaId,  String username, String comment){
        this.commentId = commentId;
        this.mediaId = mediaId;
        this.username = username;
        this.comment = comment;
    }

    public double getMediaId() {
        return mediaId;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public double getId() {
        return commentId;
    }
}
