package com.unimusic.Model.Repository;

import java.util.ArrayList;

public interface CommentRepository {
    ArrayList<UniComment> loadComments(double id, int depthLevel, boolean getOnlyParentComments);
    ArrayList<UniComment> loadComments(double id, int depthLevel);
    ArrayList<UniComment> loadComments(int depthLevel);
    void saveComment(UniComment comment, int depthLevel);

    UniComment loadComment(int id);

    void deleteComment(UniComment comment);

    int getCommentsSize();
}
