package com.unimusic.Model.Media;

import com.unimusic.Model.Repository.UniData;

public class UniMediaSegmentInfo extends UniData {
    private int mediaId;
    private String title;
    private String artists;
    private String instruments;
    private String extraInfo;
    private double startTime;
    private double endTime;
    public UniMediaSegmentInfo(int id, int mediaId, UniDataType type) {
        super(id, type);
        this.mediaId = mediaId;
    }
    public UniMediaSegmentInfo(int id, int mediaId, String title, String artists, String instruments, String extraInfo, double startTime, double endTime, UniDataType type) {
        super(id, type);
        Init(mediaId, title, artists, instruments, extraInfo, startTime, endTime);
    }
    private void Init(int mediaId, String title, String artists, String instruments, String extraInfo, double startTime, double endTime){
        this.mediaId = mediaId;
        this.title = title;
        this.artists = artists;
        this.instruments = instruments;
        this.extraInfo = extraInfo;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public UniMediaSegmentInfo(int id, UniMediaSegmentInfo data, UniDataType type)
    {
        super(id, type);
        Init(data.mediaId, data.getTitle(), data.getArtists(),
                data.getInstruments(), data.getExtraInfo(),
                data.getStartTime(), data.getEndTime());
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
    public String getExtraInfo() { return extraInfo; }
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
    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }
}
