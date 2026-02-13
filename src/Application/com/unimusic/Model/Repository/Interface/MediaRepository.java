package com.unimusic.Model.Repository.Interface;

import com.unimusic.Model.Media.UniMedia;

import java.util.ArrayList;

public interface MediaRepository {
    void saveMedia(UniMedia uniMedia);
    ArrayList<UniMedia> loadMediaWithInterval(int start, int end);
    ArrayList<UniMedia> loadMediaWithInterval(ArrayList<UniMedia> medium, int start, int end);
    int generateMediaId();
    ArrayList<UniMedia> getSongs();
    ArrayList<UniMedia> getConcerts();
}