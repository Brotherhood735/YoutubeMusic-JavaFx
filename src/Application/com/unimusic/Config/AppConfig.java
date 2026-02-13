package com.unimusic.Config;

import java.io.File;

public class AppConfig {

    // Application settings
    public static final String APP_TITLE = "Uni Music - JavaFX Version";
    public static final double APP_WIDTH = 1400;
    public static final double APP_HEIGHT = 800;
    // Data storage settings
    public static final String RESOURCES_DIR = "src/Application/resources/com/unimusic";
    public static final String DATA_DIR = RESOURCES_DIR + "/data";
    public static final String USERS_CSV_PATH = DATA_DIR + "/users.csv";
    public static final String DEFAULT_MUSIC_IMAGE_PATH = DATA_DIR + "/default_music_icon.png";
    public static final String UNI_MEDIA_CSV_PATH = DATA_DIR + "/uniMedia.csv";
    public static final String UNI_MEDIA_META_INFO_CSV_PATH = DATA_DIR + "/uniMediaMetaInfo.csv";
    public static final String UNI_USER_REGISTRATION_REQ_CSV_PATH = DATA_DIR + "/uniUsersRegistrationReq.csv";
    public static final String UNI_MEDIA_NOTES_CSV_PATH = DATA_DIR + "/uniMediaNotes.csv";
    public static final String COMMENTS_CSV_PATH = DATA_DIR + "/comments depth level.csv";
    public static final String USERS_DEFAULT_ROW = "admin,admin,1\n";
    // Navigation Settings
    public static final double HOME_MEDIA_PLAY_AREA_IDX = 0;

    //data storage headers
    public static final String COMMENTS_CSV_HEADER = "id_comment,id_media,id_parent,username,comment,depthLevel";
    public static final String UNI_USER_REGISTRATION_REQ_HEADER = "username, password";
    public static final String UNI_MEDIA_CSV_HEADER = "id_media,title,video_name,audio_name,web_url,artist,image_name,type,instruments,date,place,username,genre";
    public static final String USERS_CSV_HEADER = "username,password,is_admin,is_guest";
    public static final String UNI_MEDIA_META_INFO_CSV_HEADER = "id_meta_info,id_media,username,title,artists,instruments,extra_info,start_time,end_time";

    // Create data directory if it doesn't exist
    static {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}