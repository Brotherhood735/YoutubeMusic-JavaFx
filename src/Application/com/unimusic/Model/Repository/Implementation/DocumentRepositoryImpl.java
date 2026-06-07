package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Media.DocumentType;
import com.unimusic.Model.Media.UniDocument;
import com.unimusic.Model.Repository.CsvBaseRepository;
import com.unimusic.Model.Repository.Interface.DocumentRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DocumentRepositoryImpl extends CsvBaseRepository<UniDocument> implements DocumentRepository {

    public DocumentRepositoryImpl() {
        super(AppConfig.DOCUMENTS_CSV_PATH, AppConfig.DOCUMENTS_CSV_HEADER);
        loadAll();
    }

    @Override
    protected UniDocument parseLine(String line) {
        String[] parts = line.split(",");
        // Controllo base sulla lunghezza
        if (parts.length >= 4) {
            try {
                int id = Integer.parseInt(parts[0]);
                int mediaId = Integer.parseInt(parts[1]);
                DocumentType type = DocumentType.valueOf(parts[2]);
                String path = parts[3];
                return new UniDocument(id, mediaId, type, path);
            } catch (Exception e) {
                System.err.println("Errore parsing documento: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    protected String serialize(UniDocument item) {
        return item.getId() + "," +
                item.getMediaId() + "," +
                item.getType().name() + "," +
                item.getFileName();
    }

    @Override
    public void saveDocument(UniDocument document) {
        memoryCache.removeIf(d -> d.getId() == document.getId());
        memoryCache.add(document);
        saveAll();
    }

    @Override
    public ArrayList<UniDocument> getDocumentsByMediaId(int mediaId) {
        return memoryCache.stream()
                .filter(d -> d.getMediaId() == mediaId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int generateId() {
        if (memoryCache.isEmpty()) return 1;
        return memoryCache.stream().mapToInt(UniDocument::getId).max().orElse(0) + 1;
    }

    @Override
    public void removeDocument(UniDocument document) {
        memoryCache.removeIf(d -> d.getId() == document.getId());
        saveAll();
    }
}