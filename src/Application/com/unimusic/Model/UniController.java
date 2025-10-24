package com.unimusic.Model;

import javafx.scene.Node;

public abstract class UniController {

    /**
     * @param Idx indicates which pane to set it on.
     */
    public abstract void setContent(Node content, int Idx);
}
