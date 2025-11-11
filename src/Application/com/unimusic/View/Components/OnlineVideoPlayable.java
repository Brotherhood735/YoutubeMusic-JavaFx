package com.unimusic.View.Components;

import com.unimusic.Model.UniMedia;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class OnlineVideoPlayable implements PlayableMedia {

    private ImageView imageView;
    private EmbeddedMediaPlayer mediaPlayer;
    private MediaPlayerFactory factory;
    private String videoUrl;
    public OnlineVideoPlayable(String videoUrl, ImageView view, UniMedia uniMedia) {
        this.videoUrl = videoUrl;
        this.imageView = view;
        // 1. Create JavaFX ImageView for video output
        imageView.setPreserveRatio(true);
        // 2. Create MediaPlayer factory & player
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        // 3. Link VLCJ output to JavaFX ImageView
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(imageView));
        // 6. Play any supported link (local or streaming)
    }

    @Override
    public MediaPlayer getLocalMediaPlayer() {
        return null;
    }

    @Override
    public EmbeddedMediaPlayer getOnlineMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public Media getLocalMedia() {
        return null;
    }

    @Override
    public void playMedia() {
        // Cleanup properly on exit
        if (mediaPlayer != null) {
            //mediaPlayer.controls().play();
        }
    }
    @Override
    public void render() {
        imageView.setPreserveRatio(true);
        mediaPlayer.media().play(videoUrl);
    }

    @Override
    public void stopRender() {

    }

    @Override
    public void seek(Duration duration) {
        //mediaPlayer.controls().setTime((long) duration.toSeconds());
    }

    @Override
    public void setVolume(double volume) {
        //mediaPlayer.audio().setVolume((int) volume);

    }

    @Override
    public void setOnReady(Runnable runnable) {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter()
        {
            @Override
            public void mediaPlayerReady(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {
                runnable.run();
            }
        });
    }

    @Override
    public void addCurrentTimeListener(TimeChangedListener listener) {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
            @Override
            public void timeChanged(uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer, long newTime) {
                listener.timeChanged(new Duration(newTime));
            }
        });
    }

    @Override
    public Duration getMediaDuration() {
        long duration = mediaPlayer.media().info().duration();
        return Duration.millis(duration);
    }

    @Override
    public void pauseMedia() {
        // Cleanup properly on exit
        if (mediaPlayer != null) {
            mediaPlayer.controls().stop();
            mediaPlayer.release();
        }
        if (factory != null) {
            factory.release();
        }
    }

    @Override
    public void resetMedia() {

    }
}
