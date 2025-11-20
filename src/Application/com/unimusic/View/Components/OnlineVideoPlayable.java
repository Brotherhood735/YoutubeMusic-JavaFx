package com.unimusic.View.Components;

import com.unimusic.Model.Media.UniMedia;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
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
    public MediaPlayer getLocalMediaPlayer() {
        return null;
    }

    @Override
    public EmbeddedMediaPlayer getOnlineMediaPlayer() {
        return null;
    }

    @Override
    public Media getLocalMedia() {
        return null;
    }

    @Override
    public void playMedia() {
    }
    @Override
    public void render() {
    }

    @Override
    public void stopRender() {
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
    public Duration getMediaDuration() {
        return null;
    }

    @Override
    public void pauseMedia() {
    }

    @Override
    public void resetMedia() {
    }
}
