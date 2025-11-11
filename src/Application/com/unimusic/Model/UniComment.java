package com.unimusic.Model;

public class UniComment extends UniData{
    private int depth;
    private int mediaId;
    private String username;
    private String comment;
    private UniComment parentComment;
    public UniComment(UniComment parentComment, int commentId, int mediaId,  String username, String comment, int depth){
        super(commentId);
        this.mediaId = mediaId;
        this.username = username;
        this.comment = comment;
        this.parentComment = parentComment;
        this.depth = depth;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }


    public int getDepth() {
        return depth;
    }

    public UniComment getParent() {
        return parentComment;
    }
}
