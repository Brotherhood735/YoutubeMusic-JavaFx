package com.unimusic.Controller.Media;

import com.unimusic.Controller.UniController;
import com.unimusic.Model.Media.UniMediaSegmentInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class MediaMetaInfoController extends UniController {
    private UniMediaSegmentInfo mediaMetaInfo;
    @FXML private Hyperlink startSectionLabel;
    @FXML private Hyperlink endSectionLabel;
    @FXML private Label esecutoriLabel;
    @FXML private Label strumentiLabel;
    @FXML private Label extraInfoLabel;
    @FXML private Label titleLabel;
    private MediaMain parentController;

    public MediaMetaInfoController(MediaMain parentController, UniMediaSegmentInfo mediaMetaInfo)
    {
        this.parentController = parentController;
        this.mediaMetaInfo = mediaMetaInfo;
    }

    private void setData() {
        titleLabel.setText(mediaMetaInfo.getTitle());
        esecutoriLabel.setText(mediaMetaInfo.getArtists());
        strumentiLabel.setText(mediaMetaInfo.getInstruments());
        extraInfoLabel.setText(mediaMetaInfo.getExtraInfo());
        startSectionLabel.setText(convertDurationToString(mediaMetaInfo.getStartTime()));
        endSectionLabel.setText(convertDurationToString(mediaMetaInfo.getEndTime()));
    }

    private String convertDurationToString(double millis) {
        int totalSeconds = (int) (millis / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @FXML
    public void initialize()
    {
        setData();
    }


    @Override
    public void setContent(Node content, int Idx) {

    }

    @FXML
    public void startSectionRequested(MouseEvent mouseEvent) {
        parentController.getRenderer().seek(Duration.millis(mediaMetaInfo.getStartTime()));

    }
    @FXML
    public void endSectionRequested(MouseEvent mouseEvent) {
        parentController.getRenderer().seek(Duration.millis(mediaMetaInfo.getEndTime()));
    }
}
