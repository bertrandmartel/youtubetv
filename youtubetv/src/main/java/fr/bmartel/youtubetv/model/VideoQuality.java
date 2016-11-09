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

/**
 * Video Quality enum.
 * Check : https://developers.google.com/youtube/js_api_reference#Playback_quality
 *
 * @author Bertrand Martel
 */
public enum VideoQuality {

    AUTO(0, "auto"),
    TINY(1, "tiny"),
    SMALL(2, "small"),
    MEDIUM(3, "medium"),
    LARGE(4, "large"),
    HD_720(5, "hd720"),
    HD_1080(6, "hd1080"),
    HIGH_RES(7, "highres"),
    HD_1440(8, "hd1440"),
    HD_2160(9, "hd2160");

    private String mValue;
    private int mIndex;

    VideoQuality(final int index, final String value) {
        this.mIndex = index;
        this.mValue = value;
    }

    public int getIndex() {
        return mIndex;
    }

    public String getValue() {
        return mValue;
    }

    public static VideoQuality getVideoQuality(int value) {

        for (VideoQuality quality : VideoQuality.values()) {
            if (value == quality.getIndex())
                return quality;
        }
        return AUTO;
    }

    public static VideoQuality getVideoQuality(String value) {

        for (VideoQuality quality : VideoQuality.values()) {
            if (value.equals(quality.getValue()))
                return quality;
        }
        return AUTO;
    }

}
