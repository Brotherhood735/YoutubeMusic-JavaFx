package com.unimusic.Model.Media;

import com.unimusic.Model.Repository.UniData;
import javafx.util.Duration;

public class UniMediaMetaInfo extends UniData {
    private String title;
    private String artists;
    private String instruments;
    private Duration startTime;
    private Duration endTime;
    public UniMediaMetaInfo(int id) {
        super(id, UniDataType.MetaInfo);
    }
    public UniMediaMetaInfo(int id, String title, String artists, String instruments, Duration startTime, Duration endTime) {
        super(id, UniDataType.MetaInfo);
        this.title = title;
        this.artists = artists;
        this.instruments = instruments;
        this.startTime = startTime;
        this.endTime = endTime;
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
    public Duration getStartTime() {
        return startTime;
    }
    public Duration getEndTime() {
        return endTime;
    }

    public void setStartTime(Duration startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Duration endTime) {
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
