package com.unimusic.Model.Repository;

import com.unimusic.Model.Media.UniDataType;

public class UniData {
    protected int dataId;
    protected UniDataType type;
    public UniData(int id, UniDataType type) {
        this.dataId = id;
    }
    public int getId() {
        return dataId;
    }
}

