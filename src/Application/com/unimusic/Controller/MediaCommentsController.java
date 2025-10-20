package com.unimusic.Controller;

import com.unimusic.Main;
import com.unimusic.Model.DataManager;
import com.unimusic.Model.UniController;
import com.unimusic.Model.UniData;
import com.unimusic.Model.UniMedia;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class MediaCommentsController extends UniController {

    private UniMedia currentMedia;
    private DataManager dataManager;

    @FXML private TextField commentField;
    @FXML
    public void initialize() {
        populateSection();
        dataManager = Main.getDataManager();
    }

    private void populateSection()
    {

    }

    @FXML
    private void sendComment()
    {

    }

    @Override
    public void setContent(Node content, int Idx) {

    }

    @Override
    public void setData(UniData data) {
        currentMedia = (UniMedia) data;
    }
}
