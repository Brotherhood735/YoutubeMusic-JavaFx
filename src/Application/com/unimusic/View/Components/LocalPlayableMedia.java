package com.unimusic.View.Components;

import com.unimusic.Main;
import com.unimusic.Model.UniMedia;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.net.URL;

public class LocalPlayableMedia implements PlayableMedia {
    protected Media media;
    protected MediaPlayer mediaPlayer;
    private UniMedia uniMedia;

    protected LocalPlayableMedia(String mediaName, UniMedia uniMedia)
    {
        URL fxmlUrl = Main.class.getResource(mediaName);
        media = new Media(fxmlUrl.toString());
        mediaPlayer = new MediaPlayer(media);
        this.uniMedia = uniMedia;
    }


    public Media getMedia() { return media; }

    @Override
    public MediaPlayer getLocalMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public EmbeddedMediaPlayer getOnlineMediaPlayer() {
        return null;
    }

    @Override
    public Media getLocalMedia() {
        return media;
    }

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
    public void stopRender(){}

    @Override
    public void seek(Duration duration) {
        mediaPlayer.seek(duration);
    }

    @Override
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void setOnReady(Runnable runnable) {
        mediaPlayer.setOnReady(runnable);
    }

    @Override
    public void addCurrentTimeListener(TimeChangedListener changeListener) {
        mediaPlayer.currentTimeProperty().addListener(
                (obs, oldTime, newTime) -> {
            changeListener.timeChanged(newTime);
        });
    }

    @Override
    public Duration getMediaDuration() {
        return media.getDuration();
    }
}
