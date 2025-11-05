package com.unimusic.View.Components;

import com.unimusic.Model.UniMedia;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LocalAudioRenderer extends MediaRenderer
{
    private Image image;
    private ImageView imageView;
    public LocalAudioRenderer(String audioName, ImageView view, UniMedia uniMedia) {
        super("data/" + audioName, uniMedia);
        this.imageView = view;
        this.image = null;
    }

    @Override
    public void render() {
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
    }
}