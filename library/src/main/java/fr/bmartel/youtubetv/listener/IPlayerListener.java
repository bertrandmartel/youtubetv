package fr.bmartel.youtubetv.listener;

import fr.bmartel.youtubetv.model.VideoState;

/**
 * Youtube Player listener.
 *
 * @author Bertrand Martel
 */
public interface IPlayerListener {

    /**
     * Called when player is ready.
     */
    void onPlayerReady();

    /**
     * Called when player state change.
     *
     * @param state player state
     */
    void onPlayerStateChange(VideoState state, long position, float speed, String title);

}
