package fr.bmartel.youtubetv.youtubetv;

/**
 * Video Quality enum.
 *
 * @author Bertrand Martel
 */
public enum VideoQuality {

    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    HD_720("hd720"),
    HD_1080("hd1080"),
    HIGH_RES("highres"),
    DEFAULT("default");

    private String mValue;

    VideoQuality(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
