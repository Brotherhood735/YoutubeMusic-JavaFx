package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Authentication.User;
import com.unimusic.Model.Media.UniMedia;
import com.unimusic.Model.Repository.CsvBaseRepository;
import com.unimusic.Model.Repository.Interface.MediaRepository;
import com.unimusic.Model.Repository.Interface.UserRepository;
import com.unimusic.Model.Repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MediaRepositoryImpl extends CsvBaseRepository<UniMedia> implements MediaRepository {

    private UserRepository userRepository;

    public MediaRepositoryImpl(UserRepository userRepository) {
        super(AppConfig.UNI_MEDIA_CSV_PATH, AppConfig.UNI_MEDIA_CSV_HEADER);
        this.userRepository = userRepository;
        loadAll();
    }

    @Override
    protected UniMedia parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 13) {
            try {
                int id = Integer.parseInt(parts[0]);
                String title = parseStr(parts[1]);
                String videoUrl = parseStr(parts[2]);
                String audioUrl = parseStr(parts[3]);
                String webUrl = parseStr(parts[4]);
                String artist = parseStr(parts[5]);
                String imageUrl = parseStr(parts[6]);
                String type = parseStr(parts[7]);
                String instruments = parseStr(parts[8]);
                String date = parseStr(parts[9]);
                String place = parseStr(parts[10]);
                String username = parseStr(parts[11]);
                String genre = parseStr(parts[12]);

                User user = userRepository.getRegistredUser(username);

                return new UniMedia(id, title, videoUrl, audioUrl, webUrl, artist, imageUrl, type,
                        instruments, date, place, user, genre);

            } catch (Exception e) {
                System.err.println("Error parsing media line: " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    protected String serialize(UniMedia medium) {
        String username = (medium.getUser() != null) ? medium.getUser().getUsername() : "null";

        return String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                medium.getId(),
                formatStr(medium.getTitle()),
                formatStr(medium.getVideoName()),
                formatStr(medium.getAudioName()),
                formatStr(medium.getWebUrl()),
                formatStr(medium.getArtist()),
                formatStr(medium.getImageName()),
                formatStr(medium.getType()),
                formatStr(medium.getInstruments()),
                formatStr(medium.getDate()),
                formatStr(medium.getPlace()),
                formatStr(username),
                formatStr(medium.getGenre())
        );
    }

    private String parseStr(String str) {
        return "null".equals(str) ? null : str;
    }

    private String formatStr(String str) {
        return (str == null || str.isEmpty()) ? "null" : str;
    }

    @Override
    public void saveMedia(UniMedia uniMedia) {
        memoryCache.add(uniMedia);
        saveAll();
    }

    @Override
    public ArrayList<UniMedia> loadMediaWithInterval(int start, int end) {
        return loadSubList(memoryCache, start, end);
    }

    @Override
    public ArrayList<UniMedia> loadMediaWithInterval(ArrayList<UniMedia> medium, int start, int end) {
        return loadSubList(medium, start, end);
    }

    private ArrayList<UniMedia> loadSubList(List<UniMedia> sourceList, int start, int end) {
        if (sourceList == null || sourceList.isEmpty()) return null;
        if (start >= sourceList.size()) return new ArrayList<>(sourceList);

        int safeEnd = Math.min(end, sourceList.size());
        if (start > safeEnd) return new ArrayList<>();

        return new ArrayList<>(sourceList.subList(start, safeEnd));
    }

    @Override
    public int generateMediaId() {
        return memoryCache.size();
    }

    @Override
    public ArrayList<UniMedia> getSongs() {
        return filterByType("Song");
    }

    @Override
    public ArrayList<UniMedia> getConcerts() {
        return filterByType("Concert");
    }

    private ArrayList<UniMedia> filterByType(String type) {
        return memoryCache.stream()
                .filter(m -> type.equals(m.getType()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}