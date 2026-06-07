package com.unimusic.Controller.Media;

import com.unimusic.Controller.UniController;
import com.unimusic.Main;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.View.Components.PlayableMedia;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;

public class MediaControlController extends UniController
{

    private PlayableMedia renderer;
    private boolean canModifyProgressBar;

    @FXML private Button playPauseBtn;
    @FXML private Button muteAudibleBtn;
    @FXML private Label nowPlayingTitle;
    @FXML private Label nowPlayingArtist;
    @FXML private Slider progressBar;
    @FXML private Slider volumeSlider;
    @FXML private Label timeStart;
    @FXML private Label timeEnd;
    @FXML private ImageView coverImageView;
    private boolean isPlaying = false;
    private boolean isMute = false;

    public void init(PlayableMedia renderer, UniMedia media)
    {
        this.renderer = renderer;
        canModifyProgressBar = true;
        setNowPlaying(media.getTitle(), media.getArtist());
        URL imageUrl = Main.class.getResource("Images/"+ media.getImageName());
        coverImageView.setImage(new Image(imageUrl.toString()));
        setDuration();
    }
    public PlayableMedia getRenderer()
    {
        return renderer;
    }
    private void setDuration()
    {
        renderer.setOnReady( () -> {
            Platform.runLater(() ->
            {
                setOnReadyValues(renderer);
                progressBar.setOnMouseReleased((obs) ->
                {
                    renderer.seek(Duration.millis(progressBar.getValue()));
                    canModifyProgressBar = true;
                });

                volumeSlider.setOnMouseReleased(
                        event -> {
                            renderer.setVolume(volumeSlider.getValue());
                        }
                );
            });
        });
        renderer.addCurrentTimeListener((newTime) -> {
            Platform.runLater(() ->
            {
                int minutes = ((int) newTime.toMinutes());
                double seconds = newTime.toSeconds() - (minutes * 60);
                timeStart.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));
                if (!canModifyProgressBar) return;
                progressBar.setValue(newTime.toMillis());
            });
        });
        progressBar.setOnMouseClicked(event -> {canModifyProgressBar = false;});

        progressBar.setOnMouseExited(event ->
        {
            canModifyProgressBar = true;
        });
    }

    private void setOnReadyValues(PlayableMedia renderer) {
        Duration duration = renderer.getMaxDuration();
        int minutes = ((int) duration.toMinutes());
        double seconds = duration.toSeconds() - (minutes * 60);
        timeEnd.setText(String.valueOf(minutes) + ":" + String.format("%02d", (int)seconds));
        progressBar.setMax(duration.toMillis());
        progressBar.setValue(0);
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(50);
        renderer.setVolume(volumeSlider.getValue());
    }


    @FXML
    private void initialize()
    {
        isPlaying = true;
        MediaMainController.setBottomController(this);
    }


    @FXML
    private void onPlayPause() {
        isPlaying = !isPlaying;
        playPauseBtn.setText(isPlaying ? "⏸" : "▶" );
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
        renderer.setMute(isMute);
    }

    private void setRendererPlay(boolean isPlaying)
    {
        if (isPlaying)
        {
            renderer.play();
        }
        else
        {
            renderer.pause();
        }
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
