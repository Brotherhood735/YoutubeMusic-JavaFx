package com.unimusic.View.Components;

import com.unimusic.Model.Media.UniMedia;
import javafx.scene.media.Media;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class OnlineVideoPlayable implements PlayableMedia {

    private WebView webView;
    private WebEngine engine;
    private String videoUrl;
    private UniMedia uniMedia;
    public OnlineVideoPlayable(String videoUrl, WebView webView, UniMedia uniMedia) {
        this.videoUrl = videoUrl;
        this.webView = webView;
        this.engine = webView.getEngine();
        this.uniMedia = uniMedia;
        engine.load(videoUrl);
    }

    @Override
    public void init() {

    }

    @Override
    public MediaPlayer getLocalMediaPlayer() {
        return null;
    }

    @Override
    public EmbeddedMediaPlayer getEmbeddedMediaPlayer() {
        return null;
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void seek(Duration duration) {

    }

    @Override
    public void setVolume(double volume) {

    }

    @Override
    public void setOnReady(Runnable runnable) {

    }

    @Override
    public void addCurrentTimeListener(TimeChangedListener listener) {

    }

    @Override
    public Duration getMaxDuration() {
        return null;
    }
}
