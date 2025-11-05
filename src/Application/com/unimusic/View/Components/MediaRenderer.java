package com.unimusic.View.Components;

import com.unimusic.Main;
import com.unimusic.Model.UniMedia;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public class MediaRenderer {
    protected Media media;
    protected MediaPlayer mediaPlayer;
    private UniMedia uniMedia;

    public MediaRenderer(String mediaName, UniMedia uniMedia)
    {
        URL fxmlUrl = Main.class.getResource(mediaName);
        media = new Media(fxmlUrl.toString());
        mediaPlayer = new MediaPlayer(media);
        this.uniMedia = uniMedia;
    }


    public MediaPlayer getMediaPlayer() {

        return mediaPlayer;
    }
    public Media getMedia() { return media; }

    public void playMedia() {
        mediaPlayer.play();
    }

    public void pauseMedia() {
        mediaPlayer.pause();
    }

    public void resetMedia() {
        if(mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }
    public void render(){};
}
