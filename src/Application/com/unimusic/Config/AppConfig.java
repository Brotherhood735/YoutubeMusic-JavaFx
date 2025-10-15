package com.unimusic.Config;

import java.io.File;

public class AppConfig {

    // Application settings
    public static final String APP_TITLE = "Uni Music - JavaFX Version";
    public static final double APP_WIDTH = 1000;
    public static final double APP_HEIGHT = 700;
    // Data storage settings
    public static final String DATA_DIR = "src/resources/data";
    public static final String USERS_CSV_PATH = DATA_DIR + "/users.csv";
    public static final String MUSIC_ITEMS_CSV_PATH = DATA_DIR + "/uniMedia.csv";

    // Navigation Settings
    public static final double HOME_MEDIA_PLAY_AREA_IDX = 0;
    public static final String COMMENTS_CSV_PATH = "/comments.csv";

    // Create data directory if it doesn't exist
    static {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}