package com.unimusic.View.Components;

import com.unimusic.Model.UniMedia;
import javafx.scene.media.MediaView;


public class LocalVideoRenderer extends MediaRenderer {

    private MediaView mediaView;

    public LocalVideoRenderer(String videoName, MediaView view, UniMedia uniMedia) {
        super("Videos/" + videoName, uniMedia);
        this.mediaView = view;
    }
    @Override
    public void render(){
        mediaView.setPreserveRatio(true);
        mediaView.setMediaPlayer(mediaPlayer);
    }
}