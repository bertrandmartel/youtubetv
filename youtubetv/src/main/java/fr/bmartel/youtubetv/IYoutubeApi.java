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
package fr.bmartel.youtubetv;

import java.util.List;

import fr.bmartel.youtubetv.listener.IBufferStateListener;
import fr.bmartel.youtubetv.listener.IPlayerListener;
import fr.bmartel.youtubetv.listener.IProgressUpdateListener;
import fr.bmartel.youtubetv.model.VideoInfo;
import fr.bmartel.youtubetv.model.VideoQuality;
import fr.bmartel.youtubetv.model.VideoState;

/**
 * Youtube API interface.
 *
 * @author Bertrand Martel
 */
public interface IYoutubeApi {

    /**
     * Plays the currently cued/loaded video.
     */
    void start();

    /**
     * Pauses the currently playing video.
     */
    void pause();

    /**
     * Play the video if not playing, pause the video if playing.
     */
    void playPause();

    /**
     * Stops and cancels loading of the current video.
     */
    void stopVideo();

    /**
     * Seeks to a specified time in the video.
     *
     * @param seconds        identifies the time to which the player should advance
     * @param allowSeekAhead determines whether the player will make a new request to the server if the seconds parameter specifies a time outside of the currently buffered video data
     */
    void seekTo(int seconds, boolean allowSeekAhead);

    /**
     * Seeks to a specified time in the video with bufferization if time outside of the loaded bufferized data.
     *
     * @param seconds identifies the time to which the player should advance
     */
    void seekTo(int seconds);


    /**
     * Loads and plays the next video in the playlist.
     */
    void nextVideo();

    /**
     * Loads and plays the previous video in the playlist.
     */
    void previousVideo();

    /**
     * Loads and plays the specified video in the playlist.
     *
     * @param index playlist index
     */
    void playVideoAt(int index);

    /**
     * Mute player.
     */
    void mute();

    /**
     * Unmute player.
     */
    void unMute();

    /**
     * Check if player is muted.
     *
     * @return true if the player is muted, false if not
     */
    boolean isMuted() throws InterruptedException;

    /**
     * Set volume
     *
     * @param volume volume value between 0 and 100
     */
    void setVolume(int volume);

    /**
     * Returns the player's current volume, an integer between 0 and 100.
     *
     * @return {number} volume between 0 and 100
     */
    int getVolume() throws InterruptedException;

    /**
     * Sets the size in pixels of the <iframe> that contains the player.
     *
     * @param width  width valuedrawImage
     * @param height height value
     */
    void setSize(int width, int height);

    /**
     * Retrieves the playback rate of the currently playing video.
     *
     * @return playback rate
     */
    int getPlaybackRate() throws InterruptedException;

    /**
     * Set the playback rate.
     *
     * @param suggestedRate the suggested playback rate for the current video
     */
    void setPlaybackRate(int suggestedRate);

    /**
     * List of playback rates in which the current video is available.
     *
     * @return the set of playback rates in which the current video is available. The default value is 1, which indicates that the video is playing in normal speed
     */
    List<Integer> getAvailablePlaybackRates() throws InterruptedException;

    /**
     * Loop the playlist or not.
     *
     * @param loopPlaylists indicates whether the video player should continuously play a playlist or if it should stop playing after the last video in the playlist ends
     */
    void setLoop(boolean loopPlaylists);

    /**
     * Shuffle the playlist or not.
     *
     * @param shufflePlaylist indicates whether a playlist's videos should be shuffled so that they play back in an order different from the one that the playlist creator designated.
     */
    void setShuffle(boolean shufflePlaylist);

    /**
     * Get video loaded fraction.
     *
     * @return a number between 0 and 1 that specifies the percentage of the video that the player shows as buffered
     */
    float getVideoLoadedFraction();

    /**
     * Get current player state.
     *
     * @return state of the player
     */
    VideoState getPlayerState();

    /**
     * Get time since video is playing.
     *
     * @return elapsed time in seconds since the video started playing
     */
    float getCurrentPosition();

    /**
     * Set the suggested video quality for the current video.
     *
     * @param suggestedQuality video quality
     */
    void setPlaybackQuality(VideoQuality suggestedQuality);

    /**
     * Get current video quality.
     *
     * @return the actual video quality of the current video
     */
    VideoQuality getPlaybackQuality();

    /**
     * Get all available quality levels.
     *
     * @return the set of quality formats in which the current video is available
     */
    List<VideoQuality> getAvailableQualityLevels();

    /**
     * Get duration.
     *
     * @return the duration in seconds of the currently playing video
     */
    float getDuration();

    /**
     * Get video URL.
     *
     * @return {string}  the YouTube.com URL for the currently loaded/playing video
     */
    String getVideoUrl();

    /**
     * Get video embed code.
     *
     * @return {string} the embed code for the currently loaded/playing video
     */
    String getVideoEmbedCode();

    /**
     * Get playlist.
     *
     * @return an array of the video IDs in the playlist as they are currently ordered
     */
    List<String> getPlaylist();

    /**
     * Get playlist index.
     *
     * @return returns the index of the playlist video that is currently playing
     */
    int getPlaylistIndex();

    /**
     * Get Youtube current video ID.
     *
     * @return
     */
    String getVideoId();

    /**
     * Get Youtube current video title.
     *
     * @return
     */
    String getVideoTitle();

    /**
     * Move forward to current video time + seconds.
     *
     * @param seconds number of additionnal seconds
     */
    void moveForward(int seconds);

    /**
     * Move backward from current video time - seconds.
     *
     * @param seconds number of soustracted seconds
     */
    void moveBackward(int seconds);

    /**
     * Check if video is playing or not.
     *
     * @return
     */
    boolean isPlaying();

    /**
     * Add a player listener.
     *
     * @param listener
     */
    void addPlayerListener(IPlayerListener listener);

    /**
     * Remove player listener.
     *
     * @param listener
     */
    void removePlayerListener(IPlayerListener listener);

    /**
     * set buffer state listener.
     *
     * @param listener buffer state listener
     */
    void setOnBufferingUpdateListener(IBufferStateListener listener);

    /**
     * Set progress update listener.
     *
     * @param listener
     */
    void setOnProgressUpdateListener(IProgressUpdateListener listener);

    /**
     * Get videoInfo.
     *
     * @return
     */
    VideoInfo getVideoInfo();

    /**
     * load a new video (reload the page to play a new video).
     *
     * @param videoId
     */
    void playVideo(String videoId);

    /**
     * Stop loading webview, stop video & Release MediaSession.
     */
    void closePlayer();

}