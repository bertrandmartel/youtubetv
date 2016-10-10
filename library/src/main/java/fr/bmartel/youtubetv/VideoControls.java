package fr.bmartel.youtubetv;

/**
 * Video controls enum.
 * Check : https://developers.google.com/youtube/player_parameters?playerVersion=HTML5#controls
 *
 * @author  Bertrand Martel
 */
public enum VideoControls {

    DEFAULT(1),
    ALWAYS(2),
    NONE(0);

    private int mIndex;

    VideoControls(final int index) {
        this.mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }

    public static VideoControls getVideoControls(int value) {

        for (VideoControls controls : VideoControls.values()) {
            if (value == controls.getIndex())
                return controls;
        }
        return DEFAULT;
    }
}
