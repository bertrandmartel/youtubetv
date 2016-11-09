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
 * Youtube video thumbnail (from https://gist.github.com/protrolium/8831763)
 *
 * @author Bertrand Martel
 */
public enum ThumbnailQuality {
    /**
     * default thumbnail image
     */
    DEFAULT(0, "default"),
    /**
     * high quality
     */
    HQ_DEFAULT(1, "hqdefault"),
    /**
     * medium quality
     */
    MQ_DEFAULT(2, "mqdefault"),
    /**
     * standard definition quality
     */
    SD_DEFAULT(3, "sddefault"),
    /**
     * maximum resolution quality
     */
    MAXRES_DEFAULT(4, "maxresdefault");

    private String mValue;
    private int mIndex;

    ThumbnailQuality(final int index, final String value) {
        this.mIndex = index;
        this.mValue = value;
    }

    public int getIndex() {
        return mIndex;
    }

    public String getValue() {
        return mValue;
    }

    public static ThumbnailQuality getThumbnail(int value) {

        for (ThumbnailQuality thumbnail : ThumbnailQuality.values()) {
            if (value == thumbnail.getIndex())
                return thumbnail;
        }
        return DEFAULT;
    }

}
