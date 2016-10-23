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

import fr.bmartel.youtubetv.listener.IPlayerListener;
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
     * List of player listener.
     */
    private List<IPlayerListener> mPlayerListenerList = new ArrayList<>();
    private ConditionVariable block;

    /**
     * Build JS interface.
     *
     * @param handler    Webview handler
     * @param loadingBar layout containing the progress bar
     * @param webView    Webview object
     */
    public JavascriptInterface(final List<IPlayerListener> playerListenerList,
                               final Handler handler,
                               final ProgressBar loadingBar,
                               final ImageView playIcon,
                               final WebView webView) {
        this.mPlayerListenerList = playerListenerList;
        this.mWebview = webView;
        this.mLoadingProgress = loadingBar;
        this.mHandler = handler;
        this.mPlayIcon = playIcon;
    }

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

    private String mVideoUrl;

    private String mEmbedCode;

    private int mPlaylistIndex;

    private List<VideoQuality> mAvailableQualityLevels = new ArrayList<>();

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
    public void onPlayerReady() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (IPlayerListener listener : mPlayerListenerList) {
                    listener.onPlayerReady();
                }
            }
        }).start();

    }

    @android.webkit.JavascriptInterface
    public void onPlayerStateChange(final int state) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (IPlayerListener listener : mPlayerListenerList) {
                    listener.onPlayerStateChange(VideoState.getPlayerState(state));
                }
            }
        }).start();
    }

    @android.webkit.JavascriptInterface
    public void onMuteReceived(final boolean muted) {
        Log.i(TAG, "onMuteReceived");
        mMuted = muted;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onVolumeReceived(final int volume) {
        Log.i(TAG, "onVolumeReceived");
        mVolume = volume;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onVideoLoadedFractionReceived(final float fraction) {
        Log.i(TAG, "onVideoLoadedFractionReceived");
        mVideoLoadedFraction = fraction;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onPlaybackRateReceived(final int playbackRate) {
        Log.i(TAG, "onPlaybackRateReceived");
        mPlaybackRate = playbackRate;
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onPlayerStateReceived(final int playerState) {
        Log.i(TAG, "onPlayerStateReceived");
        mPlayerState = VideoState.getPlayerState(playerState);
        if (mLock != null) {
            mLock.open();
        }
    }

    @android.webkit.JavascriptInterface
    public void onCurrentTimeReceived(final float currentTime) {
        Log.i(TAG, "onCurrentTimeReceived");
        mCurrentTime = currentTime;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onPlaybackQualityReceived(final String videoQuality) {
        Log.i(TAG, "onPlaybackQualityReceived");
        mPlaybackQuality = VideoQuality.getVideoQuality(videoQuality);
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onPlaylistIndexReceived(final int playListIndex) {
        Log.i(TAG, "onPlaylistIndexReceived");
        mPlaylistIndex = playListIndex;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onPlaylistReceived(final String playlist) {
        Log.i(TAG, "onPlaylistReceived");
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
        Log.i(TAG, "onDurationReceived");
        mDuration = duration;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onVideoUrlReceived(final String videoUrl) {
        Log.i(TAG, "onVideoUrlReceived");
        mVideoUrl = videoUrl;
        mLock.open();
    }

    @android.webkit.JavascriptInterface
    public void onVideoEmbedCodeReceived(final String embedCode) {
        Log.i(TAG, "onVideoEmbedCodeReceived");
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

    public float getVideoLoadedFraction() {
        return mVideoLoadedFraction;
    }

    public VideoState getPlayerState() {
        return mPlayerState;
    }

    public float getCurrentTime() {
        return mCurrentTime;
    }

    public void setBlock(final ConditionVariable lock) {
        this.mLock = lock;
    }

    public VideoQuality getPlaybackQuality() {
        return mPlaybackQuality;
    }

    public List<VideoQuality> getAvailableQualityLevels() {
        return mAvailableQualityLevels;
    }

    public float getDuration() {
        return mDuration;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getVideoEmbedCode() {
        return mEmbedCode;
    }

    public int getPlaylistIndex() {
        return mPlaylistIndex;
    }

    public List<String> getPlaylist() {
        return mPlaylist;
    }
}
