package com.unimusic.Model.Repository;

import com.unimusic.Model.Media.UniMediaMetaInfo;

import java.util.ArrayList;

public interface MediaMetaInfoRepository {
    void saveMetaInfo(UniMediaMetaInfo metaInfo);
    ArrayList<UniMediaMetaInfo> getMetaInfo(int mediaId);
    int getMetaInfoLength(int mediaId);
    int generateMetaInfoId();

}
