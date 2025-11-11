package com.unimusic.Controller;

import com.unimusic.Model.UniController;
import com.unimusic.Model.UniMedia;
import com.unimusic.View.Components.PlayableMedia;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.util.Duration;

public class MediaControlController extends UniController
{

    private PlayableMedia renderer;
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

    public void setData(PlayableMedia renderer, UniMedia media, boolean isPlaying)
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
        renderer.setOnReady( () -> {
            Duration duration = renderer.getMediaDuration();
            int minutes = ((int) duration.toMinutes());
            double seconds = duration.toSeconds() - (minutes * 60);
            timeEnd.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));
            progressBar.setMax(duration.toSeconds());
            progressBar.setValue(0);
            volumeSlider.setMin(0);
            volumeSlider.setMax(1);
            volumeSlider.setValue(0.5);
            renderer.setVolume(volumeSlider.getValue());
        });

        renderer.addCurrentTimeListener((newTime) -> {
            int minutes = ((int) newTime.toMinutes());
            double seconds = newTime.toSeconds() - (minutes * 60);
            timeStart.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));
            if (!canModifyProgressBar) return;
            progressBar.setValue(newTime.toSeconds());
        });

        progressBar.setOnMouseClicked(event -> {canModifyProgressBar = false;});
        progressBar.setOnMouseReleased((obs) ->
        {
            renderer.seek(Duration.seconds(progressBar.getValue()));
            canModifyProgressBar = true;
        });

        volumeSlider.setOnMouseReleased(
                event -> {
                    renderer.setVolume(volumeSlider.getValue());
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
        renderer.getLocalMediaPlayer().setMute(isMute);
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
