package com.unimusic.View.Components;

import javafx.util.Duration;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public interface PlayableMedia {

    void init();
    MediaPlayer getLocalMediaPlayer();
    EmbeddedMediaPlayer getEmbeddedMediaPlayer();

    void play();

    void pause();

    void restart();
    void stop();
    void dispose();
    void seek(Duration duration);
    void setVolume(double volume);
    void setOnReady(Runnable runnable);

    interface TimeChangedListener{
        void timeChanged(Duration newTime);
    }
    void addCurrentTimeListener(TimeChangedListener listener);

    Duration getMaxDuration();
}
