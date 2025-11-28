package com.unimusic.View.Components;

import com.unimusic.Model.Media.UniMedia;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LocalPlayableAudio extends LocalPlayableMedia
{
    private Image image;
    private UniMedia uniMedia;
    public LocalPlayableAudio(String audioName, ImageView view, UniMedia uniMedia) {
        super("data/" + audioName, view);
        this.uniMedia = uniMedia;
        this.image = null;
    }
    @Override
    public void init(){
        videoImageView.setPreserveRatio(true);
    }

}