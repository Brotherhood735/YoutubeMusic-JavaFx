package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Repository.Interface.CommentRepository;
import com.unimusic.Model.Repository.CsvBaseRepository;
import com.unimusic.Model.Repository.UniComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class CommentRepositoryImpl extends CsvBaseRepository<UniComment> implements CommentRepository {

    private int commentIdCounter = 0;
    public CommentRepositoryImpl() {
        super(AppConfig.COMMENTS_CSV_PATH, AppConfig.COMMENTS_CSV_HEADER);
        loadAll();
    }

    @Override
    protected UniComment parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 6) {
            try {
                int id = Integer.parseInt(parts[0]);
                int mediaId = Integer.parseInt(parts[1]);
                int parentId = Integer.parseInt(parts[2]);
                String username = parts[3];
                String text = parts[4];
                int depth = Integer.parseInt(parts[5]);

                UniComment parent = null;
                if (parentId != -1) {
                    parent = findById(parentId);
                }

                return new UniComment(parent, id, mediaId, username, text, depth);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected String serialize(UniComment comment) {
        String parentId = (comment.getParent() != null) ? String.valueOf(comment.getParent().getId()) : "-1";
        return comment.getId() + "," +
                comment.getMediaId() + "," +
                parentId + "," +
                comment.getUsername() + "," +
                comment.getComment() + "," +
                comment.getDepth();
    }

    private UniComment findById(int id) {
        return memoryCache.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public ArrayList<UniComment> loadComments(double mediaId, int depthLevel) {
        return memoryCache.stream()
                .filter(c -> c.getMediaId() == (int)mediaId && c.getDepth() == depthLevel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<UniComment> loadComments(double mediaId, int depthLevel, boolean getOnlyParentComments) {
        return memoryCache.stream()
                .filter(c -> c.getDepth() == depthLevel)
                .filter(c -> {
                    if (getOnlyParentComments) {
                        // Qui la logica originale era un po' ambigua sull'ID,
                        // ma assumendo che "mediaId" sia l'ID del genitore se getOnlyParentComments è true
                        return c.getParent() != null && c.getParent().getId() == (int)mediaId;
                    } else {
                        return c.getMediaId() == (int)mediaId;
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<UniComment> loadComments(int depthLevel) {
        return memoryCache.stream()
                .filter(c -> c.getDepth() == depthLevel)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void saveComment(UniComment comment, int depthLevel) {
        memoryCache.removeIf(c -> c.getId() == comment.getId());
        memoryCache.add(comment);
        saveAll();
    }

    @Override
    public UniComment loadComment(int id) {
        return findById(id);
    }

    @Override
    public void deleteComment(UniComment comment) {
        Set<Integer> idsToDelete = new HashSet<>();
        collectDescendants(comment.getId(), idsToDelete);

        memoryCache.removeIf(c -> idsToDelete.contains(c.getId()));
        saveAll();
    }

    @Override
    public int getNewCommentId() {

        if (memoryCache.isEmpty()) {
            return 0;
        }
        int maxId = 0;
        for (UniComment comment : memoryCache) {
            if (comment.getId() > maxId) {
                maxId = comment.getId();
            }
        }
        return maxId;
    }

    private void collectDescendants(int currentId, Set<Integer> idsToDelete) {
        idsToDelete.add(currentId);
        List<UniComment> children = memoryCache.stream()
                .filter(c -> c.getParent() != null && c.getParent().getId() == currentId)
                .collect(Collectors.toList());

        for (UniComment child : children) {
            collectDescendants(child.getId(), idsToDelete);
        }
    }
}