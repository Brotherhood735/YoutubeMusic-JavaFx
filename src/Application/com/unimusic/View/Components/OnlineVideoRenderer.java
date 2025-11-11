package com.unimusic.View.Components;

import com.unimusic.Model.UniMedia;
import javafx.scene.web.WebView;

public class OnlineVideoRenderer extends MediaRenderer {
    private WebView webView;
    public OnlineVideoRenderer(String mediaName, UniMedia uniMedia) {
        super(mediaName, uniMedia);
        webView = new WebView();
        webView.setPrefSize(800, 600);
        webView.getEngine().load("https://www.youtube.com/embed/dQw4w9WgXcQ");
        //root.getChildren().add(webView);
    }

    @Override
    public void render() {
        //webView.fit
    }
}
