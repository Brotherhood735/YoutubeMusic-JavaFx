package com.unimusic.Model.Media;

public class UniMediaUserNote extends UniMediaSegmentInfo {
    private String username;

    public UniMediaUserNote(int id, String username, UniMediaSegmentInfo data, UniDataType type) {
        super(id, data, type);
        this.username = username;
    }
    public UniMediaUserNote(int id, String username, int mediaId, String title, String artists, String instruments, String extraInfo, double startTime, double endTime, UniDataType type) {
        super(id, mediaId, title, artists, instruments, extraInfo, startTime, endTime, type);
        this.username = username;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
