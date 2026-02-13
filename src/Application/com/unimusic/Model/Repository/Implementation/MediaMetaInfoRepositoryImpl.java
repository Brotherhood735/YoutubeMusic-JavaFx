package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Config.AppConfig;
import com.unimusic.Model.Media.UniDataType;
import com.unimusic.Model.Media.UniMediaSegmentInfo;
import com.unimusic.Model.Repository.CsvBaseRepository;
import com.unimusic.Model.Repository.Interface.MediaMetaInfoRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MediaMetaInfoRepositoryImpl extends CsvBaseRepository<UniMediaSegmentInfo> implements MediaMetaInfoRepository {

    public MediaMetaInfoRepositoryImpl() {
        super(AppConfig.UNI_MEDIA_META_INFO_CSV_PATH, AppConfig.UNI_MEDIA_META_INFO_CSV_HEADER);
        loadAll();
    }

    @Override
    protected UniMediaSegmentInfo parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 8) {
            try {
                int id = Integer.parseInt(parts[0]);
                int mediaId = Integer.parseInt(parts[1]);
                String title = parseStr(parts[2]);
                String artists = parseStr(parts[3]);
                String instruments = parseStr(parts[4]);
                String extraInfo = parseStr(parts[5]);
                double startTime = Double.parseDouble(parts[6]);
                double endTime = Double.parseDouble(parts[7]);

                return new UniMediaSegmentInfo(id, mediaId, title, artists, instruments,
                        extraInfo, startTime, endTime, UniDataType.MetaInfo);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected String serialize(UniMediaSegmentInfo metaInfo) {
        return String.format("%d,%d,%s,%s,%s,%s,%s,%s",
                metaInfo.getId(),
                metaInfo.getMediaId(),
                formatStr(metaInfo.getTitle()),
                formatStr(metaInfo.getArtists()),
                formatStr(metaInfo.getInstruments()),
                formatStr(metaInfo.getExtraInfo()),
                metaInfo.getStartTime(),
                metaInfo.getEndTime()
        );
    }

    private String parseStr(String str) {
        return "null".equals(str) ? null : str;
    }

    private String formatStr(String str) {
        return (str == null || str.isEmpty()) ? "null" : str;
    }

    @Override
    public void saveMetaInfo(UniMediaSegmentInfo metaInfo) {
        memoryCache.add(metaInfo);
        saveAll();
    }

    @Override
    public ArrayList<UniMediaSegmentInfo> getMetaInfo(int mediaId) {
        if (mediaId < 0) return new ArrayList<>();
        return memoryCache.stream()
                .filter(m -> m.getMediaId() == mediaId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int getMetaInfoLength(int mediaId) {
        return (int) memoryCache.stream()
                .filter(m -> m.getMediaId() == mediaId)
                .count();
    }

    @Override
    public int generateMetaInfoId() {
        return memoryCache.size();
    }
}