package com.unimusic.Model.Repository.Interface;

import com.unimusic.Model.Media.UniDocument;
import java.util.ArrayList;

public interface DocumentRepository {
    void saveDocument(UniDocument document);
    ArrayList<UniDocument> getDocumentsByMediaId(int mediaId);
    int generateId();

    void removeDocument(UniDocument document);
}