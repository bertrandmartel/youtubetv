package fr.bmartel.youtubetv;

/**
 * Autohide video enum.
 * Check : https://developers.google.com/youtube/player_parameters?playerVersion=HTML5#autohide
 *
 * @author  Bertrand Martel
 */
public enum VideoAutoHide {

    DEFAULT(2),
    DISPLAY_AUTO(1),
    ALWAYS_VISIBLE(0);

    private int mIndex;

    VideoAutoHide(final int index) {
        this.mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }

    public static VideoAutoHide getVideoControls(int value) {

        for (VideoAutoHide controls : VideoAutoHide.values()) {
            if (value == controls.getIndex())
                return controls;
        }
        return DEFAULT;
    }
}
