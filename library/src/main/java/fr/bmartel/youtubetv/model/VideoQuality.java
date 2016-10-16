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

    SMALL(0, "small"),
    MEDIUM(1, "medium"),
    LARGE(2, "large"),
    HD_720(3, "hd720"),
    HD_1080(4, "hd1080"),
    HIGH_RES(5, "highres"),
    DEFAULT(6, "default");

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
        return DEFAULT;
    }

}
