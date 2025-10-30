package com.unimusic.Controller;

import com.unimusic.Model.UniController;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.Components.VideoRenderer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MediaControlController extends UniController
{

    private VideoRenderer renderer;
    private UniMedia currentMedia;


    @FXML private Button playPauseBtn;
    @FXML private Label nowPlayingTitle;
    @FXML private Label nowPlayingArtist;
    @FXML private Slider progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label timeStart;
    @FXML private Label timeEnd;
    private boolean isPlaying = false;

    public void setData(VideoRenderer renderer, UniMedia media, boolean isPlaying)
    {
        this.renderer = renderer;
        this.currentMedia = media;
        setNowPlaying(media.getTitle(), media.getArtist());
        setDuration();
        onPlayPause();
    }

    private void setDuration()
    {
        MediaPlayer media = renderer.getVideo();
        Media m = media.getMedia();
        media.setOnReady( () -> {
            Duration duration = m.getDuration();
            int minutes = ((int) m.getDuration().toMinutes());
            double seconds = m.getDuration().toSeconds() - (minutes * 60);
            timeEnd.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));
        });
        media.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            int minutes = ((int) newTime.toMinutes());
            double seconds = newTime.toSeconds() - (minutes * 60);
            timeStart.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));

        });
    }


    @FXML
    private void initialize()
    {
        HomeController.setMediaController(this);
        MediaMainController.setBottomController(this);
    }


    @FXML
    private void onPlayPause() {
        isPlaying = !isPlaying;
        playPauseBtn.setText(isPlaying ? "⏸" : "▶");
        setRendererPlay(isPlaying);
    }
    private void setRendererPlay(boolean isPlaying)
    {
        if (isPlaying)
        {
            renderer.playMedia();
        }
        else
        {
            renderer.pauseMedia();
        }
    }
    @FXML
    private void onPrevious() {

        System.out.println("Previous track");
    }

    @FXML
    private void onNext() {
        System.out.println("Next track");
    }
    @Override
    public void setContent(Node content, int Idx)
    {

    }
    public void setNowPlaying(String title, String artist)
    {
        nowPlayingTitle.setText(title);
        nowPlayingArtist.setText(artist);
    }
}
