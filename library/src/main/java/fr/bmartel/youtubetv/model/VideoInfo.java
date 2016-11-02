package fr.bmartel.youtubetv.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akinaru on 28/10/16.
 */
public class VideoInfo {

    private String mVideoId = "";

    private String mAuthor = "";

    private String mTitle = "";

    private List<VideoQuality> mAvailableQualityList = new ArrayList<>();

    public VideoInfo(String videoId, String author, String title, List<VideoQuality> availableQualityList) {
        mVideoId = videoId;
        mAuthor = author;
        mTitle = title;
        mAvailableQualityList = availableQualityList;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<VideoQuality> getAvailableQualityList() {
        return mAvailableQualityList;
    }
}
