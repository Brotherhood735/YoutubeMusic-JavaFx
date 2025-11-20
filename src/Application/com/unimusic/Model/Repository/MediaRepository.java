package com.unimusic.Model.Repository;

import com.unimusic.Model.Media.UniMedia;

import java.util.ArrayList;

public interface MediaRepository {
    ArrayList<UniMedia> loadMedia();
    ArrayList<UniMedia> loadMediaWithInterval(int start, int end);
}
