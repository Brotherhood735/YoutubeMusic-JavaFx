package com.unimusic.Model;


public class UniMedia extends UniData
{
    String title;
    String videoUrl;
    String audioUrl;
    String webUrl;
    String type;
    String artist;
    String thumbnailUrl;

    public UniMedia(String title,String videoUrl, String audioUrl, String webUrl,
                    String artist, String thumbnailUrl, String type) {
        this.title = title;
        this.videoUrl = videoUrl;
        this.audioUrl = audioUrl;
        this.webUrl = webUrl;
        this.artist = artist;
        this.thumbnailUrl = thumbnailUrl;
        this.type = type;
    }
    public String getTitle() { return title; }
    public String getVideoUrl() { return videoUrl; }
    public String getAudioUrl() { return audioUrl; }
    public String getWebUrl() { return webUrl; }
    public String getArtist() { return artist; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getType() { return type; }

}
