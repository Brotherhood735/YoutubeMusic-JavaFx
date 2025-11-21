package com.unimusic.Model.Repository;

import com.unimusic.Model.Media.UniMedia;

import java.util.ArrayList;

public interface MediaRepository {
    void saveMedia(UniMedia uniMedia);
    ArrayList<UniMedia> loadMediaWithInterval(int start, int end);
    int generateMediaId();

    int getMediaLength();
}
