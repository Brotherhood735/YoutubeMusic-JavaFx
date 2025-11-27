package com.unimusic.Model.Media;

import com.unimusic.Model.Repository.UniData;
import javafx.util.Duration;

public class UniMediaMetaInfo extends UniData {
    private int mediaId;
    private String title;
    private String artists;
    private String instruments;
    private double startTime;
    private double endTime;
    public UniMediaMetaInfo(int id, int mediaId) {
        super(id, UniDataType.MetaInfo);
        this.mediaId = mediaId;
    }
    public UniMediaMetaInfo(int id, int mediaId, String title, String artists, String instruments, double startTime, double endTime) {
        super(id, UniDataType.MetaInfo);
        this.mediaId = mediaId;
        this.title = title;
        this.artists = artists;
        this.instruments = instruments;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public int getMediaId() {
        return mediaId;
    }
    public String getTitle() {
        return title;
    }
    public String getArtists() {
        return artists;
    }
    public String getInstruments() {
        return instruments;
    }
    public double getStartTime() {
        return startTime;
    }
    public double getEndTime() {
        return endTime;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtists(String artists) {
        this.artists = artists;
    }
    public void setInstruments(String instruments) {
        this.instruments = instruments;
    }

}
