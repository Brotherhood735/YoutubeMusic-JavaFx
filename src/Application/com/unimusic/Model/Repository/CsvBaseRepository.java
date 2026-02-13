package com.unimusic.Model.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Classe astratta che definisce il "Template" per leggere/scrivere CSV
public abstract class CsvBaseRepository<T> {
    protected final String csvPath;
    protected final String csvHeader;
    protected final List<T> memoryCache = new ArrayList<>();

    public CsvBaseRepository(String csvPath, String csvHeader) {
        this.csvPath = csvPath;
        this.csvHeader = csvHeader;
        ensureFileExists();
    }

    protected void loadAll() {
        memoryCache.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                T item = parseLine(line);
                if (item != null) {
                    memoryCache.add(item);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }
    }

    protected void saveAll() {
        try (FileWriter writer = new FileWriter(csvPath)) {
            writer.write(csvHeader + "\n"); // Scrive header
            for (T item : memoryCache) {
                writer.write(serialize(item) + "\n"); // Delega la formattazione
            }
        } catch (IOException e) {
            System.err.println("Error saving CSV: " + e.getMessage());
        }
    }

    private void ensureFileExists() {
        File file = new File(csvPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(csvHeader + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract T parseLine(String line);
    protected abstract String serialize(T item);
}