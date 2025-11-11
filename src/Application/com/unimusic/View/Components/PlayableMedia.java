package com.unimusic.View.Components;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public interface PlayableMedia {

    public MediaPlayer getLocalMediaPlayer();
    public EmbeddedMediaPlayer getOnlineMediaPlayer();
    public Media getLocalMedia();

    public void playMedia();

    public void pauseMedia();

    public void resetMedia();
    public void render();
    public void stopRender();
    public void seek(Duration duration);
    public void setVolume(double volume);
    public void setOnReady(Runnable runnable);

    public interface TimeChangedListener{
        void timeChanged(Duration newTime);
    }
    public void addCurrentTimeListener(TimeChangedListener listener);

    public Duration getMediaDuration();
}
