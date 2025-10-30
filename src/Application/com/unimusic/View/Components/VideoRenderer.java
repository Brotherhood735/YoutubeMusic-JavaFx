package com.unimusic.View.Components;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;



public class VideoRenderer {


    private File mediaFile;
    private Media media;
    private MediaPlayer mediaPlayer;

    public VideoRenderer(String videoPath)
    {
        mediaFile = new File(videoPath);
        String mediaUrl = mediaFile.toURI().toString();
        media = new Media(mediaUrl);
        mediaPlayer = new MediaPlayer(media);
    }


    public MediaPlayer getVideo() {

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
}