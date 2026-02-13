package com.unimusic.Model.Repository;

import com.unimusic.Model.Repository.Implementation.*;
import com.unimusic.Model.Repository.Interface.*;

public class RepositoryFactory {
    private static RepositoryFactory instance;

    // Le istanze delle repository
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final CommentRepository commentRepository;
    private final MediaNoteRepository mediaNoteRepository;
    private final MediaMetaInfoRepository mediaMetaInfoRepository;

    private RepositoryFactory() {
        this.userRepository = new UserRepositoryImpl();
        this.mediaRepository = new MediaRepositoryImpl(this.userRepository);

        this.commentRepository = new CommentRepositoryImpl();
        this.mediaNoteRepository = new MediaNoteRepositoryImpl();
        this.mediaMetaInfoRepository = new MediaMetaInfoRepositoryImpl();
    }

    public static RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
        }
        return instance;
    }

    public UserRepository getUserRepository() { return userRepository; }
    public MediaRepository getMediaRepository() { return mediaRepository; }
    public CommentRepository getCommentRepository() { return commentRepository; }
    public MediaNoteRepository getMediaNoteRepository() { return mediaNoteRepository; }
    public MediaMetaInfoRepository getMediaMetaInfoRepository() { return mediaMetaInfoRepository; }
}