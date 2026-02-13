package com.unimusic.Model.Repository.Interface;

import com.unimusic.Model.Media.UniMediaUserNote;

import java.util.ArrayList;

public interface MediaNoteRepository {
    void saveNote(UniMediaUserNote note);
    ArrayList<UniMediaUserNote> getNotes(int mediaId);
    int getNoteLength(int mediaId);
    int generateNoteId();
}
