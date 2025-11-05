package com.unimusic.Model;

import java.util.ArrayList;
import java.util.List;

public interface MediaRepository {
    ArrayList<UniMedia> loadMedia();
    ArrayList<UniMedia> loadMediaWithInterval(int start, int end);
}
