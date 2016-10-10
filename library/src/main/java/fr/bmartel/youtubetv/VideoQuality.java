package fr.bmartel.youtubetv;

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
