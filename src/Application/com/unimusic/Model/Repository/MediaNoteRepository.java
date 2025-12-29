package com.unimusic.Model.Repository;

import com.unimusic.Model.Media.UniMediaSegmentInfo;

import java.util.ArrayList;

public interface MediaNoteRepository {
    void saveNote(UniMediaSegmentInfo note);
    ArrayList<UniMediaSegmentInfo> getNotes(int mediaId);
    int getNoteLength(int mediaId);
    int generateNoteId();
}
