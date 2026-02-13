package com.unimusic.View.Components;

import com.unimusic.Config.AppConfig;
import com.unimusic.Main;
import com.unimusic.Model.Media.UniMedia;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.log.LogEventListener;
import uk.co.caprica.vlcj.log.LogLevel;
import uk.co.caprica.vlcj.log.NativeLog;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.net.URL;

public class LocalPlayableAudio extends LocalPlayableMedia
{
    private Image image;
    private UniMedia uniMedia;
    public LocalPlayableAudio(String audioName, ImageView view, UniMedia uniMedia) {
        super(AppConfig.RESOURCES_DIR + "/data/" + audioName, view);
        URL imageUrl = Main.class.getResource("Images/"+ uniMedia.getImageName());
        this.image = new Image(imageUrl.toString());
        view.setImage(this.image);

        this.uniMedia = uniMedia;
        view.setImage(image);
        uniMedia.getImageName();
    }
    @Override
    public void init(){
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