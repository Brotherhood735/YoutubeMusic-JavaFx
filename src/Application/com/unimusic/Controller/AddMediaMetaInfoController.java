package com.unimusic.Controller;

import com.unimusic.Model.Media.UniMedia;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.RangeSlider;

public class AddMediaMetaInfoController extends UniController
{
    @FXML private TextField titoloInput;
    @FXML private TextField esecutoriInput;
    @FXML private TextField strumentiInput;
    @FXML private RangeSlider rangeSlider;
    @FXML private Label startSectionLabel;
    @FXML private Label endSectionLabel;

    public AddMediaMetaInfoController(UniMedia media)
    {

    }

    @Override
    public void setContent(Node content, int Idx) {

    }
    @FXML
    public void onAddMetaInfo()
    {

    }
}
