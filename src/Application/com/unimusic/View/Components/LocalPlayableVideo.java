package com.unimusic.View.Components;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Media.UniMedia;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;


public class LocalPlayableVideo extends LocalPlayableMedia {

    public LocalPlayableVideo(String videoName, ImageView view, UniMedia uniMedia) {
        super(AppConfig.RESOURCES_DIR + "/Videos/" + videoName, view);
    }

    @Override
    public void init() {
        videoImageView.setPreserveRatio(true);
        mediaPlayerFactory = new MediaPlayerFactory();
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        embeddedMediaPlayer.videoSurface().set(new FXCallbackVideoSurface());

        embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter()
        {
            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("Error playing media");
            }
        });
        play(mediaUrl);
    }
}