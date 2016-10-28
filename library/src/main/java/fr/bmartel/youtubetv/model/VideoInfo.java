package fr.bmartel.youtubetv.model;

/**
 * Created by akinaru on 28/10/16.
 */
public class VideoInfo {

    private String mVideoId = "";

    private String mAuthor = "";

    private String mTitle = "";

    public VideoInfo(String videoId, String author, String title) {
        mVideoId = videoId;
        mAuthor = author;
        mTitle = title;
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
}
