package com.unimusic.Model;


public class UniMedia extends UniData
{
    String title;
    String contentUrl;
    public MediaType type;
    String artist;
    String thumbnailUrl;

    public UniMedia(String title, String contentUrl, String artist, String thumbnailUrl, MediaType type) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.artist = artist;
        this.thumbnailUrl = thumbnailUrl;
        this.type = type;
    }
    public String getTitle() { return title; }
    public String getContentUrl() { return contentUrl; }
    public String getArtist() { return artist; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public MediaType getType() { return type; }

}
