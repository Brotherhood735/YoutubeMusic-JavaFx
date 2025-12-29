package com.unimusic.Model.Repository;

import com.unimusic.Model.Media.UniMediaSegmentInfo;

import java.util.ArrayList;

public interface MediaMetaInfoRepository {
    void saveMetaInfo(UniMediaSegmentInfo metaInfo);
    ArrayList<UniMediaSegmentInfo> getMetaInfo(int mediaId);
    int getMetaInfoLength(int mediaId);
    int generateMetaInfoId();

}
