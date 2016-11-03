/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bmartel.youtubetv.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Video information object.
 */
public class VideoInfo {

    /**
     * video id.
     */
    private String mVideoId = "";

    /**
     * video author (the field may be empty before the playing state).
     */
    private String mAuthor = "";

    /**
     * video title (the field may be empty before the playing state).
     */
    private String mTitle = "";

    /**
     * list of available quality for this video (the field may be empty before the playing state).
     */
    private List<VideoQuality> mAvailableQualityList = new ArrayList<>();

    /**
     * Build video information object.
     *
     * @param videoId
     * @param author
     * @param title
     * @param availableQualityList
     */
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
