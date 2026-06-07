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
    public abstract void setContent(Node content, int Idx);
}
