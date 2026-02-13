package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Media.UniDataType;
import com.unimusic.Model.Media.UniMediaUserNote;
import com.unimusic.Model.Repository.CsvBaseRepository;
import com.unimusic.Model.Repository.Interface.MediaNoteRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MediaNoteRepositoryImpl extends CsvBaseRepository<UniMediaUserNote> implements MediaNoteRepository {

    public MediaNoteRepositoryImpl() {
        super(AppConfig.UNI_MEDIA_NOTES_CSV_PATH, AppConfig.UNI_MEDIA_META_INFO_CSV_HEADER);
        loadAll();
    }

    @Override
    protected UniMediaUserNote parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 9) {
            try {
                int id = Integer.parseInt(parts[0]);
                int mediaId = Integer.parseInt(parts[1]);
                String username = parseStr(parts[2]);
                String title = parseStr(parts[3]);
                String artists = parseStr(parts[4]);
                String instruments = parseStr(parts[5]);
                String extraInfo = parseStr(parts[6]);
                double startTime = Double.parseDouble(parts[7]);
                double endTime = Double.parseDouble(parts[8]);

                return new UniMediaUserNote(id, username, mediaId, title, artists, instruments,
                        extraInfo, startTime, endTime, UniDataType.Note);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected String serialize(UniMediaUserNote note) {
        return String.format("%d,%d,%s,%s,%s,%s,%s,%s,%s",
                note.getId(),
                note.getMediaId(),
                formatStr(note.getUsername()),
                formatStr(note.getTitle()),
                formatStr(note.getArtists()),
                formatStr(note.getInstruments()),
                formatStr(note.getExtraInfo()),
                note.getStartTime(),
                note.getEndTime()
        );
    }

    private String parseStr(String str) {
        return "null".equals(str) ? null : str;
    }

    private String formatStr(String str) {
        return (str == null || str.isEmpty()) ? "null" : str;
    }

    @Override
    public void saveNote(UniMediaUserNote note) {
        memoryCache.add(note);
        saveAll();
    }

    @Override
    public ArrayList<UniMediaUserNote> getNotes(int mediaId) {
        if (mediaId < 0) return new ArrayList<>();
        return memoryCache.stream()
                .filter(n -> n.getMediaId() == mediaId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int getNoteLength(int mediaId) {
        return (int) memoryCache.stream()
                .filter(n -> n.getMediaId() == mediaId)
                .count();
    }

    @Override
    public int generateNoteId() {
        return memoryCache.size();
    }
}