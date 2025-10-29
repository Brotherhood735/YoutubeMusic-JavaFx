package com.unimusic.Model;

public class UniComment extends UniData{
    private double mediaId;
    private String username;
    private String comment;
    private UniComment parentComment;
    public UniComment(UniComment parentComment, double commentId, double mediaId,  String username, String comment){
        this.mediaId = mediaId;
        this.username = username;
        this.comment = comment;
        this.parentComment = parentComment;
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


}
