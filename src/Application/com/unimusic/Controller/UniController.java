package com.unimusic.Controller;

import javafx.scene.Node;

public abstract class UniController {
    protected Node view;

    public Node getView()
    {
        return view;
    }
    public void setView(Node view)
    {
        this.view = view;
    }
    /**
     * @param Idx indicates which pane to set it on.
     */
    public abstract void setContent(Node content, int Idx);
}
