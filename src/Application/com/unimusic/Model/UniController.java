package com.unimusic.Model;

import com.unimusic.View.UniControllerNavigation;
import javafx.scene.Node;

public abstract class UniController {
    protected UniControllerNavigation navigationHandler;

    /**
     * @param Idx indicates which pane to set it on.
     */
    public abstract void setContent(Node content, int Idx);
    public abstract void setData(UniData data);
    public abstract void navigateTo();
}
