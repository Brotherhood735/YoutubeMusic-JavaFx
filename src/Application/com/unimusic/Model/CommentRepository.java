package com.unimusic.Model;

import java.util.List;

public interface CommentRepository {
    List<UniComment> loadComments(double id, int depthLevel, boolean getOnlyParentComments);
    void saveComment(UniComment comment, int depthLevel);
}
