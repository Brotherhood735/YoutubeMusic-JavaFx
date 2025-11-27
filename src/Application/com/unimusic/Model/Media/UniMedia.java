package com.unimusic.Model.Media;


import com.unimusic.Model.Repository.UniData;

import java.util.ArrayList;

public class UniMedia extends UniData
{
    String title;
    String videoName;
    String audioName;
    String webUrl;
    String type;
    String artist;
    String imageName;
    String instruments;
    String date;
    String place;
    public UniMedia(int id)
    {
        super(id, UniDataType.Media);

    }
    public UniMedia(int id, String title, String videoName, String audioName, String webUrl,
                    String artist, String imageName, String type, String instruments, String date, String place) {
        super(id, UniDataType.Media);
        this.title = title;
        this.videoName = videoName;
        this.audioName = audioName;
        this.webUrl = webUrl;
        this.artist = artist;
        this.imageName = imageName;
        this.type = type;
        this.instruments = instruments;
        this.date = date;
        this.place = place;
    }
    public String getTitle() { return title; }
    public String getVideoName() { return videoName; }
    public String getAudioName() { return audioName; }
    public String getWebUrl() { return webUrl; }
    public String getArtist() { return artist; }
    public String getImageName() { return imageName; }
    public String getType() { return type; }
    public String getDate() { return date; }
    public String getPlace() { return place; }

    public String getInstruments() { return instruments; }
    public void setTitle(String title) { this.title = title; }
    public void setVideoName(String videoName) { this.videoName = videoName; }
    public void setAudioName(String audioName) { this.audioName = audioName; }
    public void setWebUrl(String webUrl) { this.webUrl = webUrl; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public void setType(String type) { this.type = type; }
    public void setInstruments(String instruments) { this.instruments = instruments; }
    public void setDate(String date) { this.date = date; }
    public void setPlace(String place) { this.place = place; }

}
