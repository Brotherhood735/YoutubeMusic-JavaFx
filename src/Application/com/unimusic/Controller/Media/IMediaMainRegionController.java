package com.unimusic.Controller.Media;

import com.unimusic.View.Components.PlayableMedia;
import javafx.util.Callback;

public interface IMediaMainRegionController
{
    PlayableMedia getRenderer();

    void loadView(String fxml, int idx, Callback<Class<?>, Object> controllerFactory);
}
