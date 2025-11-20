package com.unimusic.View.Components;

import com.unimusic.Model.Media.UniMedia;
import javafx.scene.media.MediaView;


public class LocalPlayableVideo extends LocalPlayableMedia {

    private MediaView mediaView;

    public LocalPlayableVideo(String videoName, MediaView view, UniMedia uniMedia) {
        super("Videos/" + videoName, uniMedia);
        this.mediaView = view;
    }
    @Override
    public void render(){
        mediaView.setPreserveRatio(true);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    @Override
    public void stopRender() {
        pauseMedia();
        mediaPlayer.dispose();
    }
}