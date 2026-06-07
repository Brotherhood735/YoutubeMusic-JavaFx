package com.unimusic.Model.Media;

import com.unimusic.Model.Repository.UniData;

public class UniDocument extends UniData {
    private int mediaId;
    private DocumentType type;
    private String fileName;

    public UniDocument(int id, int mediaId, DocumentType type, String fileName) {
        super(id, UniDataType.Document);
        this.mediaId = mediaId;
        this.type = type;
        this.fileName = fileName;
    }

    public int getMediaId() { return mediaId; }
    public DocumentType getType() { return type; }
    public String getFileName() { return fileName; }
}