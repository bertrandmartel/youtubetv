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

import android.media.session.PlaybackState;
import android.os.ConditionVariable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.bmartel.youtubetv.listener.IBufferStateListener;
import fr.bmartel.youtubetv.listener.IPlayerListener;
import fr.bmartel.youtubetv.listener.IProgressUpdateListener;
import fr.bmartel.youtubetv.model.VideoInfo;
import fr.bmartel.youtubetv.model.VideoQuality;
import fr.bmartel.youtubetv.model.VideoState;
import fr.bmartel.youtubetv.utils.WebviewUtils;

/**
 * Interface called from Javascript.
 *
 * @author Bertrand Martel
 */
public class JavascriptInterface {

    private final static String TAG = JavascriptInterface.class.getSimpleName();

    /**
     * Webview object.
     */
    private WebView mWebview;

    /**
     * Check if page is loaded.
     */
    private boolean mLoaded;

    /**
     * Progress bar.
     */
    private ProgressBar mLoadingProgress;

    /**
     * Play icon.
     */
    private ImageView mPlayIcon;

    /**
     * Handler instanciated in Webview thread.
     */
    private Handler mHandler;


    /**
     * Lock used to wait for Javascript task completion.
     */
    private ConditionVariable mLock;

    /**
     * Player temporary volume value.
     */
    private int mVolume;

    /**
     * Player temporary muted state.
     */
    private boolean mMuted;

    /**
     * Player playback rate.
     */
    private int mPlaybackRate;

    /**
     * Player state.
     */
    private VideoState mPlayerState;

    /**
     * playback rates list.
     */
    private List<Integer> mAvailablePlaybackRates = new ArrayList<>();

    /**
     * playlist containing videoId.
     */
    private List<String> mPlaylist = new ArrayList<>();

    /**
     * video duration.
     */
    private float mDuration;

    /**
     * define if last state was pause or play.
     */
    private boolean previousPlayPause;

    /**
     * List of player listener.
     */
    private List<IPlayerListener> mPlayerListenerList = new ArrayList<>();

    /**
     * Youtube TV view.
     */
    private YoutubeTvView mYoutubeTvView;

    /**
     * Suggested thumbnail quality to be used for preview & for now playing card.
     */
    private String suggestedThumbnailQuality = YoutubeTvConst.DEFAULT_THUMBNAIL_QUALITY.getValue();

    /**
     * Variable to check if onWindowFocusChanged has been called.
     */
    private boolean mWaitLoaded;

    /**
     * Webview actual width.
     */
    private int mViewWidth;

    /**
     * Webview actual height.
     */
    private int mViewHeight;

    /**
     * video loaded fraction.
     */
    private float mVideoLoadedFraction;

    /**
     * video current time.
     */
    private float mCurrentTime;

    /**
     * video quality.
     */
    private VideoQuality mPlaybackQuality;

    /**
     * current video URL.
     */
    private String mVideoUrl;

    /**
     * current video ID.
     */
    private String mVideoId;

    /**
     * current video Title.
     */
    private String mVideoTitle;

    /**
     * video embed code.
     */
    private String mEmbedCode;

    /**
     * current playlist index.
     */
    private int mPlaylistIndex;

    /**
     * Buffer state listener.
     */
    private IBufferStateListener mBufferStateListener;

    /**
     * Progress update listener.
     */
    private IProgressUpdateListener mProgressUpdateListener;

    /**
     * list featuring available quality for the current video.
     */
    private List<VideoQuality> mAvailableQualityLevels = new ArrayList<>();

    /**
     * Video information.
     */
    private VideoInfo mVideoInfo = new VideoInfo("", "", "");

    /**
     * Build JS interface.
     *
     * @param playerListenerList        reference to list of player listener
     * @param handler                   Webview handler
     * @param loadingBar                layout containing the progress bar
     * @param playIcon                  overlay icon used to play the video when clicked
     * @param webView                   Webview object
     * @param youtubeTvView             YoutubeTv view
     * @param suggestedThumbnailQuality suggested quality for thumbnail
     */
    public JavascriptInterface(final List<IPlayerListener> playerListenerList,
                               final Handler handler,
                               final ProgressBar loadingBar,
                               final ImageView playIcon,
                               final WebView webView,
                               final YoutubeTvView youtubeTvView,
                               final String suggestedThumbnailQuality) {
        this.mPlayerListenerList = playerListenerList;
        this.mWebview = webView;
        this.mLoadingProgress = loadingBar;
        this.mHandler = handler;
        this.mPlayIcon = playIcon;
        mYoutubeTvView = youtubeTvView;
        this.suggestedThumbnailQuality = suggestedThumbnailQuality;
    }

    /**
     * check if pageLoad callback has been called.
     *
     * @return
     */
    public boolean isPageLoaded() {
        return mLoaded;
    }

    /**
     * Log a message from Javascript.
     *
     * @param header  message header
     * @param message message body
     */
    @android.webkit.JavascriptInterface
    public void log(String header, String message) {
        Log.v(header, message);
    }

    @android.webkit.JavascriptInterface
    public void onPlayerReady(final String title, final String author, final String videoId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final VideoInfo videoInfo = new VideoInfo(videoId, author, title);

                for (IPlayerListener listener : mPlayerListenerList) {
                    listener.onPlayerReady(videoInfo);
                }
            }
        }).start();

    }

    @android.webkit.JavascriptInterface
    public void onProgressUpdate(final float currentTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mProgressUpdateListener != null) {
                    mProgressUpdateListener.onProgressUpdate(currentTime);
                }
            }
        }).start();
    }

    @android.webkit.JavascriptInterface
    public void onPlayerStateChange(final int state,
                                    final long position,
                                    final float speed,
                                    final String title,
                                    final String videoId,
                                    final String videoAuthor,
                                    final float duration,
                                    final float loadedFraction) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                final VideoState videoState = VideoState.getPlayerState(state);

                if (mYoutubeTvView.isShowingNowPlayingCard()) {

                    int playbackState = PlaybackState.STATE_STOPPED;

                    boolean rebuildMedia = true;

                    switch (videoState) {
                        case UNSTARTED:
                            previousPlayPause = false;
                            playbackState = PlaybackState.STATE_STOPPED;
                            break;
                        case ENDED:
                            if (mYoutubeTvView.getPlaylistIndex() != -1) {
                                mYoutubeTvView.start();
                            }
                            previousPlayPause = false;
                            playbackState = PlaybackState.STATE_STOPPED;
                            break;
                        case PLAYING:
                            if (previousPlayPause) {
                                rebuildMedia = false;
                            }
                            previousPlayPause = true;
                            playbackState = PlaybackState.STATE_PLAYING;
                            break;
                        case PAUSED:
                            if (previousPlayPause) {
                                rebuildMedia = false;
                            }
                            previousPlayPause = true;
                            playbackState = PlaybackState.STATE_PAUSED;
                            break;
                        case BUFFERING:
                            previousPlayPause = false;
                            playbackState = PlaybackState.STATE_BUFFERING;
                            if (mBufferStateListener != null) {
                                mBufferStateListener.onBufferUpdate(duration, loadedFraction);
                            }
                            break;
                        case VIDEO_CUED:
                            previousPlayPause = false;
                            playbackState = PlaybackState.STATE_PLAYING;
                            break;
                    }

                    String thumbnailUrl = WebviewUtils.getThumbnailURL(videoId, suggestedThumbnailQuality);
                    try {
                        thumbnailUrl = WebviewUtils.getThumbnailQuality(videoId, suggestedThumbnailQuality);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    WebviewUtils.updateMediaSession(rebuildMedia, mYoutubeTvView.getMediaSession(), thumbnailUrl, playbackState, position, speed, title);
                }

                for (IPlayerListener listener : mPlayerListenerList) {
                    listener.onPlayerStateChange(videoState, position, speed, duration, new VideoInfo(videoId, videoAuthor, title));
                }
            }
        }).start();
    }

    @android.webkit.JavascriptInterface
    public void onMuteReceived(final boolean muted) {
        mMuted = muted;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onVolumeReceived(final int volume) {
        mVolume = volume;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onVideoLoadedFractionReceived(final float fraction) {
        mVideoLoadedFraction = fraction;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onPlaybackRateReceived(final int playbackRate) {
        mPlaybackRate = playbackRate;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onPlayerStateReceived(final int playerState) {
        mPlayerState = VideoState.getPlayerState(playerState);
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onVideoIdReceived(final String videoId) {
        mVideoId = videoId;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onVideoTitleReceived(final String videoTitle) {
        mVideoTitle = videoTitle;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onCurrentTimeReceived(final float currentTime) {
        mCurrentTime = currentTime;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onPlaybackQualityReceived(final String videoQuality) {
        mPlaybackQuality = VideoQuality.getVideoQuality(videoQuality);
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onPlaylistIndexReceived(final int playListIndex) {
        mPlaylistIndex = playListIndex;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onPlaylistReceived(final String playlist) {
        mPlaylist = WebviewUtils.parsePlaylist(playlist);
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onPlaybackRatesListReceived(final String playbackRates) {
        mAvailablePlaybackRates = WebviewUtils.parsePlaybackRates(playbackRates);
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onDurationReceived(final float duration) {
        mDuration = duration;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onVideoUrlReceived(final String videoUrl) {
        mVideoUrl = videoUrl;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onVideoInfoReceived(final String title, final String author, final String videoId) {
        mVideoInfo = new VideoInfo(videoId, author, title);
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onVideoEmbedCodeReceived(final String embedCode) {
        mEmbedCode = embedCode;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onAvailableQualityLevelsReceived(final String qualityLevels) {
        mAvailableQualityLevels = WebviewUtils.parseQualityLevels(qualityLevels);
        if (mLock != null) {
            mLock.open();
        }
    }


    /**
     * Hide progress bar.
     */
    @android.webkit.JavascriptInterface
    public void hideLoading(final boolean showPlayIcon) {
        if (mLoadingProgress != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLoadingProgress.setVisibility(View.GONE);
                    if (showPlayIcon) {
                        mPlayIcon.setVisibility(View.VISIBLE);
                    } else {
                        mPlayIcon.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    /**
     * Get thumbnail quality.
     */
    @android.webkit.JavascriptInterface
    public String getThumbnailQuality(final String videoId, final String thumbnailQuality) {
        String quality = "";
        try {
            quality = WebviewUtils.getThumbnailQuality(videoId, thumbnailQuality);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quality;
    }


    /**
     * Called when page is loaded.
     */
    @android.webkit.JavascriptInterface
    public void onPageLoaded() {
        mLoaded = true;
        if (mWaitLoaded) {
            WebviewUtils.callOnWebviewThread(mWebview, "setSize", mViewWidth, mViewHeight);
        }
    }

    /**
     * Start video. This must be called from Java to enable autoplay.
     */
    @android.webkit.JavascriptInterface
    public void startVideo() {
        mWebview.post(new Runnable() {
            @Override
            public void run() {
                WebviewUtils.callJavaScript(mWebview, "playVideo");
            }
        });
    }

    /**
     * Set size parameters from WebviewonWindowFocusChanged.
     *
     * @param viewWidth  webview actual width
     * @param viewHeight webview actual height
     */
    public void setSizeOnLoad(int viewWidth, int viewHeight) {
        mWaitLoaded = true;
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
    }

    /**
     * Get temporary player volume value.
     *
     * @return
     */
    public int getVolume() {
        return mVolume;
    }

    /**
     * Get player muted state.
     *
     * @return
     */
    public boolean isMuted() {
        return mMuted;
    }

    /**
     * Get player playback rate.
     *
     * @return
     */
    public int getPlaybackRate() {
        return mPlaybackRate;
    }

    /**
     * Get available playback rates list.
     *
     * @return
     */
    public List<Integer> getAvailablePlaybackRates() {
        return mAvailablePlaybackRates;
    }

    /**
     * Get video loaded fraction.
     *
     * @return a number between 0 and 1 that specifies the percentage of the video that the player shows as buffered
     */
    public float getVideoLoadedFraction() {
        return mVideoLoadedFraction;
    }

    /**
     * Get current player state.
     *
     * @return state of the player
     */
    public VideoState getPlayerState() {
        return mPlayerState;
    }

    /**
     * Get time since video is playing.
     *
     * @return elapsed time in seconds since the video started playing
     */
    public float getCurrentTime() {
        return mCurrentTime;
    }

    /**
     * Set blocking parameter used to be released when response is retrieved from JS side.
     *
     * @param lock
     */
    public void setBlock(final ConditionVariable lock) {
        this.mLock = lock;
    }

    /**
     * Get current video quality.
     *
     * @return the actual video quality of the current video
     */
    public VideoQuality getPlaybackQuality() {
        return mPlaybackQuality;
    }

    /**
     * Get all available quality levels.
     *
     * @return the set of quality formats in which the current video is available
     */
    public List<VideoQuality> getAvailableQualityLevels() {
        return mAvailableQualityLevels;
    }

    /**
     * Get duration.
     *
     * @return the duration in seconds of the currently playing video
     */
    public float getDuration() {
        return mDuration;
    }

    /**
     * Get video URL.
     *
     * @return {string}  the YouTube.com URL for the currently loaded/playing video
     */
    public String getVideoUrl() {
        return mVideoUrl;
    }

    /**
     * Get video embed code.
     *
     * @return {string} the embed code for the currently loaded/playing video
     */
    public String getVideoEmbedCode() {
        return mEmbedCode;
    }

    /**
     * Get playlist index.
     *
     * @return returns the index of the playlist video that is currently playing
     */
    public int getPlaylistIndex() {
        return mPlaylistIndex;
    }

    /**
     * Get playlist.
     *
     * @return an array of the video IDs in the playlist as they are currently ordered
     */
    public List<String> getPlaylist() {
        return mPlaylist;
    }

    /**
     * Get Youtube current video ID.
     *
     * @return
     */
    public String getVideoId() {
        return mVideoId;
    }

    /**
     * Get Youtube current video title.
     *
     * @return
     */
    public String getVideoTitle() {
        return mVideoTitle;
    }

    /**
     * Get video information.
     *
     * @return
     */
    public VideoInfo getVideoInfo() {
        return mVideoInfo;
    }

    /**
     * Set buffer update listener.
     *
     * @param listener
     */
    public void setOnBufferingUpdateListener(IBufferStateListener listener) {
        mBufferStateListener = listener;
    }

    /**
     * Set progress update listener.
     *
     * @param listener
     */
    public void setOnProgressUpdateListener(IProgressUpdateListener listener) {
        mProgressUpdateListener = listener;
    }
}
