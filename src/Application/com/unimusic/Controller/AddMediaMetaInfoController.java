package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.Media.UniDataType;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Media.UniMediaSegmentInfo;
import com.unimusic.Model.Repository.MediaMetaInfoRepository;
import com.unimusic.View.Components.PlayableMedia;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.RangeSlider;

public class AddMediaMetaInfoController extends UniController
{
    private UniMedia media;
    private PlayableMedia renderer;
    @FXML private TextField titoloInput;
    @FXML private TextField esecutoriInput;
    @FXML private TextField strumentiInput;
    @FXML private TextField extraInfoInput;
    @FXML private RangeSlider rangeSlider;
    @FXML private Label startSectionLabel;
    @FXML private Label endSectionLabel;
    private double startSectionTimeInMillis;
    private double endSectionTimeInMillis;
    private MediaMetaInfoRepository metaInfoRepo;

    @FXML
    public void initialize()
    {
        metaInfoRepo = Main.getDataRepository();
        setupRangeSlider();
    }
    public AddMediaMetaInfoController(UniMedia media, PlayableMedia renderer)
    {
        this.media = media;
        this.renderer = renderer;
    }
    protected void setupRangeSlider() {
        double maxInMillis = renderer.getMaxDuration().toMillis();
        rangeSlider.setMax(maxInMillis);
        rangeSlider.setHighValue(maxInMillis);
        rangeSlider.setLowValue(0);
        startSectionLabel.setText(convertMillisToString(0));
        endSectionLabel.setText(convertMillisToString(maxInMillis));
        startSectionTimeInMillis = 0;
        endSectionTimeInMillis = maxInMillis;
        rangeSlider.highValueProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    endSectionTimeInMillis = newValue.doubleValue();
                    endSectionLabel.setText(convertMillisToString(endSectionTimeInMillis));
                });
        rangeSlider.lowValueProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    startSectionTimeInMillis = newValue.doubleValue();
                    startSectionLabel.setText(convertMillisToString(startSectionTimeInMillis));
                });
    }



    @Override
    public void setContent(Node content, int Idx) {

    }
    @FXML
    protected void onAddMetaInfo()
    {
        UniMediaSegmentInfo metaInfo = new UniMediaSegmentInfo(metaInfoRepo.generateMetaInfoId(), generateMetaInfoClass(), UniDataType.MetaInfo);
        metaInfoRepo.saveMetaInfo(metaInfo);
    }

    protected UniMediaSegmentInfo generateMetaInfoClass() {
        String title = titoloInput.getText();
        String artists = esecutoriInput.getText();
        String instruments = strumentiInput.getText();
        String extraInfo = extraInfoInput.getText();
        String startTime = startSectionLabel.getText();
        String endTime = endSectionLabel.getText();
        UniMediaSegmentInfo newMetaInfo = new UniMediaSegmentInfo(0, media.getId(), UniDataType.MetaInfo);
        newMetaInfo.setTitle(title);
        newMetaInfo.setArtists(artists);
        newMetaInfo.setInstruments(instruments);
        newMetaInfo.setExtraInfo(extraInfo);
        newMetaInfo.setStartTime(startSectionTimeInMillis);
        newMetaInfo.setEndTime(endSectionTimeInMillis);
        return newMetaInfo;
    }
    private String convertMillisToString(double millis) {
        if (millis < 0) return "00:00";
        int totalSeconds = (int) (millis / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}