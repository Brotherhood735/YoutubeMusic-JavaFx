package com.unimusic.Controller;

import com.unimusic.Model.UniController;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.Components.LocalVideoRenderer;
import com.unimusic.View.Components.MediaRenderer;
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

    private MediaRenderer renderer;
    private UniMedia currentMedia;
    private boolean canModifyProgressBar;

    @FXML private Button playPauseBtn;
    @FXML private Button muteAudibleBtn;
    @FXML private Label nowPlayingTitle;
    @FXML private Label nowPlayingArtist;
    @FXML private Slider progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label timeStart;
    @FXML private Label timeEnd;
    private boolean isPlaying = false;
    private boolean isMute = false;

    public void setData(MediaRenderer renderer, UniMedia media, boolean isPlaying)
    {
        this.renderer = renderer;
        this.currentMedia = media;
        canModifyProgressBar = true;
        setNowPlaying(media.getTitle(), media.getArtist());
        setDuration();
        onPlayPause();
    }

    private void setDuration()
    {
        MediaPlayer mediaPlayer = renderer.getMediaPlayer();
        Media media = mediaPlayer.getMedia();
        mediaPlayer.setOnReady( () -> {
            Duration duration = media.getDuration();
            int minutes = ((int) media.getDuration().toMinutes());
            double seconds = media.getDuration().toSeconds() - (minutes * 60);
            timeEnd.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));
            progressBar.setMax(media.getDuration().toSeconds());
            progressBar.setValue(0);
            volumeSlider.setMin(0);
            volumeSlider.setMax(1);
            volumeSlider.setValue(0.5);
            mediaPlayer.setVolume(volumeSlider.getValue());
        });
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            int minutes = ((int) newTime.toMinutes());
            double seconds = newTime.toSeconds() - (minutes * 60);
            timeStart.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));
            if (!canModifyProgressBar) return;
            progressBar.setValue(newTime.toSeconds());
        });
        progressBar.setOnMouseClicked(event -> {canModifyProgressBar = false;});
        progressBar.setOnMouseReleased((obs) ->
        {
            mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            canModifyProgressBar = true;
        });

        volumeSlider.setOnMouseReleased(
                event -> {
                    mediaPlayer.setVolume(volumeSlider.getValue());
                }
        );
    }


    @FXML
    private void initialize()
    {
        MediaMainController.setBottomController(this);
    }


    @FXML
    private void onPlayPause() {
        isPlaying = !isPlaying;
        playPauseBtn.setText(isPlaying ? "⏸" : "▶");
        setRendererPlay(isPlaying);
    }
    @FXML
    private void onMuteAudible()
    {
        isMute = !isMute;
        muteAudibleBtn.setText(isMute ? "X" : "\uD83D\uDD0A");
        setRendererMute(isMute);
    }

    private void setRendererMute(boolean isMute) {
        renderer.getMediaPlayer().setMute(isMute);
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
