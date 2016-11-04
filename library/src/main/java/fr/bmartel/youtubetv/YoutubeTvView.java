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

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.session.MediaSession;
import android.os.ConditionVariable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.youtubetv.listener.IBufferStateListener;
import fr.bmartel.youtubetv.listener.IPlayerListener;
import fr.bmartel.youtubetv.listener.IProgressUpdateListener;
import fr.bmartel.youtubetv.model.ThumbnailQuality;
import fr.bmartel.youtubetv.model.UserAgents;
import fr.bmartel.youtubetv.model.VideoAutoHide;
import fr.bmartel.youtubetv.model.VideoControls;
import fr.bmartel.youtubetv.model.VideoInfo;
import fr.bmartel.youtubetv.model.VideoQuality;
import fr.bmartel.youtubetv.model.VideoState;
import fr.bmartel.youtubetv.utils.WebviewUtils;

/**
 * Youtube TV custom view.
 *
 * @author Bertrand Martel
 */
public class YoutubeTvView extends FrameLayout implements IYoutubeApi {

    private final static String TAG = YoutubeTvView.class.getSimpleName();

    /**
     * User agent string used.
     */
    private UserAgents mUserAgent;

    /**
     * Video id.
     */
    private String mVideoId;

    /**
     * set suggested video quality. (https://developers.google.com/youtube/iframe_api_reference#Playback_quality)
     */
    private VideoQuality mVideoQuality;

    /**
     * show/dont show related video at the end of the video. (check https://developers.google.com/youtube/player_parameters?playerVersion=HTML5)
     */
    private int mShowRelatedVideos;

    /**
     * show/hide video info at the beginning of the video.
     */
    private int mShowVideoInfo;

    /**
     * show/hide youtube player controls.
     */
    private VideoControls mShowControls;

    /**
     * autohide mode for youtube player controls.
     */
    private VideoAutoHide mAutohide;

    /**
     * show/hide closed caption.
     */
    private int mClosedCaptions;

    /**
     * show/hide video annotation.
     */
    private int mVideoAnnotation;

    /**
     * debug mode.
     */
    private int mDebug;

    /**
     * actual view width.
     */
    private int mViewWidth;

    /**
     * actual view height.
     */
    private int mViewHeight;

    /**
     * webview background color.
     */
    private int mLoadBackgroundColor = YoutubeTvConst.DEFAULT_LOADING_BG;

    /**
     * Javascript interface.
     */
    private JavascriptInterface mJavascriptInterface;

    /**
     * Youtube Webview.
     */
    private WebView mWebView;

    /**
     * handler to be used to interact with UI when called from JS.
     */
    private Handler mHandler;

    /**
     * autoplay mode.
     */
    private int mAutoPlay;

    /**
     * define if border is shown when selecting the frame.
     */
    private boolean mShowBorder;

    /**
     * Border width.
     */
    private int mBorderWidth;

    /**
     * Border color.
     */
    private int mBorderColor;

    /**
     * Closed caption language preference.
     */
    private String mClosedCaptionLangPref;

    /**
     * Playlist ID for the loaded video.
     */
    private String mPlaylistId;

    /**
     * youtube player languge (http://www.loc.gov/standards/iso639-2/php/code_list.php)
     */
    private String mPlayerLanguage;

    /**
     * Thumbnail quality.
     */
    private ThumbnailQuality mThumbnailQuality;

    /**
     * condition variable used to lock JS call that return data.
     */
    private ConditionVariable mBlock = new ConditionVariable();

    /**
     * Javascript call timeout in milliseconds.
     */
    private int mJavascriptTimeout = YoutubeTvConst.DEFAULT_JAVASCRIPT_TIMEOUT;

    /**
     * List of player listener.
     */
    private List<IPlayerListener> mPlayerListenerList = new ArrayList<>();

    /**
     * Lock object used to synchronize Javascript calls.
     */
    private final Object mLock = new Object();

    /**
     * Media session used to manage now playing card.
     */
    private MediaSession mMediaSession;

    /**
     * Media session tag used for now playing card.
     */
    private final static String MEDIA_SESSION_TAG = "fr.bmartel.youtubetv.MediaSession";

    /**
     * define if now playing card is shown or not.
     */
    private boolean mShowNowPlayingCard;

    /**
     * Build Custom view.
     *
     * @param context android context
     */
    public YoutubeTvView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * Build Custom view.
     *
     * @param context android context
     * @param attrs   attributes
     */
    public YoutubeTvView(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttr(context, attrs);
        initView(context);
    }

    /**
     * Build Custom view.
     *
     * @param context  android context
     * @param attrs    attributes
     * @param defStyle
     */
    public YoutubeTvView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttr(context, attrs);
        initView(context);
    }

    /**
     * Process view attributes.
     *
     * @param context view context
     * @param attrs   attributes
     */
    private void processAttr(final Context context, AttributeSet attrs) {

        TypedArray styledAttr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.YoutubeTvView,
                0, 0);

        try {
            mVideoId = styledAttr.getString(R.styleable.YoutubeTvView_videoId);
            mVideoQuality = VideoQuality.getVideoQuality(styledAttr.getInteger(R.styleable.YoutubeTvView_videoQuality, YoutubeTvConst.DEFAULT_VIDEO_QUALITY.getIndex()));
            mShowRelatedVideos = styledAttr.getBoolean(R.styleable.YoutubeTvView_showRelatedVideos, YoutubeTvConst.DEFAULT_SHOW_RELATED_VIDEOS) ? 1 : 0;
            mShowVideoInfo = styledAttr.getBoolean(R.styleable.YoutubeTvView_showVideoInfo, YoutubeTvConst.DEFAULT_SHOW_VIDEO_INFO) ? 1 : 0;
            mShowControls = VideoControls.getVideoControls(styledAttr.getInteger(R.styleable.YoutubeTvView_showControls, YoutubeTvConst.DEFAULT_SHOW_CONTROLS.getIndex()));
            mClosedCaptions = styledAttr.getBoolean(R.styleable.YoutubeTvView_closedCaptions, YoutubeTvConst.DEFAULT_CLOSED_CAPTION) ? 1 : 0;
            mVideoAnnotation = styledAttr.getBoolean(R.styleable.YoutubeTvView_videoAnnotation, YoutubeTvConst.DEFAULT_VIDEO_ANNOTATION) ? 1 : 3;
            mAutohide = VideoAutoHide.getVideoControls(styledAttr.getInteger(R.styleable.YoutubeTvView_autoHide, YoutubeTvConst.DEFAULT_AUTOHIDE.getIndex()));
            mDebug = styledAttr.getBoolean(R.styleable.YoutubeTvView_debug, YoutubeTvConst.DEFAULT_DEBUG_MODE) ? 1 : 0;
            mLoadBackgroundColor = styledAttr.getInteger(R.styleable.YoutubeTvView_loadingBackgroundColor, YoutubeTvConst.DEFAULT_LOADING_BG);
            mAutoPlay = styledAttr.getBoolean(R.styleable.YoutubeTvView_autoplay, YoutubeTvConst.DEFAULT_AUTOPLAY) ? 1 : 0;
            mUserAgent = UserAgents.getUserAgent(styledAttr.getInteger(R.styleable.YoutubeTvView_userAgentString, YoutubeTvConst.DEFAULT_USER_AGENT.getIndex()));
            mShowBorder = styledAttr.getBoolean(R.styleable.YoutubeTvView_showBorder, YoutubeTvConst.DEFAULT_SHOW_BORDER);
            mBorderWidth = styledAttr.getInteger(R.styleable.YoutubeTvView_borderWidth, YoutubeTvConst.DEFAULT_BORDER_WIDTH);
            mBorderColor = styledAttr.getColor(R.styleable.YoutubeTvView_borderColor, YoutubeTvConst.DEFAULT_BORDER_COLOR);
            mThumbnailQuality = ThumbnailQuality.getThumbnail(styledAttr.getInteger(R.styleable.YoutubeTvView_thumbnailQuality, YoutubeTvConst.DEFAULT_THUMBNAIL_QUALITY.getIndex()));
            mClosedCaptionLangPref = styledAttr.getString(R.styleable.YoutubeTvView_closedCaptionLangPref);
            mPlayerLanguage = styledAttr.getString(R.styleable.YoutubeTvView_playerLanguage);
            mJavascriptTimeout = styledAttr.getInteger(R.styleable.YoutubeTvView_javascriptTimeout, YoutubeTvConst.DEFAULT_JAVASCRIPT_TIMEOUT);
            mPlaylistId = styledAttr.getString(R.styleable.YoutubeTvView_playlistId);
            mShowNowPlayingCard = styledAttr.getBoolean(R.styleable.YoutubeTvView_showNowPlayingCard, YoutubeTvConst.DEFAULT_SHOW_NOWPLAYINGCARD);
        } finally {
            styledAttr.recycle();
        }
    }

    /**
     * This must be override to get actual width & height of the webview and pass it to Javascript to resize player & viewport.
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        if (mJavascriptInterface != null && mJavascriptInterface.isPageLoaded()) {
            WebviewUtils.callJavaScript(mWebView, "setSize", mViewWidth, mViewHeight);
        } else {
            mJavascriptInterface.setSizeOnLoad(mViewWidth, mViewHeight);
        }
    }

    /**
     * Initialize Webview.
     *
     * @param context view context
     */
    private void initView(final Context context) {

        inflate(getContext(), R.layout.youtube_view, this);

        init();
    }

    /**
     * Init.
     */
    private void init() {

        setBorder();

        mWebView = (WebView) findViewById(R.id.youtube_view);
        ImageView playIcon = (ImageView) findViewById(R.id.play_icon);

        ProgressBar loadingProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mHandler = new Handler();

        mWebView.setBackgroundColor(mLoadBackgroundColor);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                result.cancel();
                return true;
            }
        });

        mWebView.setPadding(0, 0, 0, 0);
        mWebView.setScrollbarFadingEnabled(true);

        mJavascriptInterface = new JavascriptInterface(mPlayerListenerList,
                mHandler,
                loadingProgress,
                playIcon,
                mWebView,
                this,
                mThumbnailQuality.getValue());

        mWebView.addJavascriptInterface(mJavascriptInterface, "JSInterface");

        mWebView.getSettings().setUserAgentString(mUserAgent.getValue());

        if (mShowNowPlayingCard) {
            mMediaSession = new MediaSession(getContext(), MEDIA_SESSION_TAG);
            mMediaSession.setCallback(new MediaSession.Callback() {
                @Override
                public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
                    // Consume the media button event here. Should not send it to other apps.
                    return true;
                }
            });
            mMediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

            if (!mMediaSession.isActive()) {
                mMediaSession.setActive(true);
            }
        }

        final String videoUrl = "file:///android_asset/youtube.html" +
                "?videoId=" + mVideoId +
                "&videoQuality=" + mVideoQuality.getValue() +
                "&playerHeight=" + height +
                "&playerWidth=" + width +
                "&rel=" + mShowRelatedVideos +
                "&showinfo=" + mShowVideoInfo +
                "&controls=" + mShowControls.getIndex() +
                "&autohide=" + mAutohide.getIndex() +
                "&cc_load_policy=" + mClosedCaptions +
                "&iv_load_policy=" + mVideoAnnotation +
                "&autoplay=" + mAutoPlay +
                "&thumbnailQuality=" + mThumbnailQuality.getValue() +
                "&cc_lang_pref=" + mClosedCaptionLangPref +
                "&hl=" + mPlayerLanguage +
                "&playlist_id=" + mPlaylistId +
                "&debug=" + mDebug;


        Log.v(TAG, "videoUrl : " + videoUrl);

        mWebView.loadUrl(videoUrl);
    }

    private void setBorder() {

        if (mShowBorder) {
            FrameLayout layout = (FrameLayout) findViewById(R.id.youtube_frame);
            layout.setPadding(mBorderWidth, mBorderWidth, mBorderWidth, mBorderWidth);
            layout.setBackground(getResources().getDrawable(R.drawable.webview_selector));
            StateListDrawable drawable = (StateListDrawable) layout.getBackground();
            DrawableContainer.DrawableContainerState drawableContainerState = (DrawableContainer.DrawableContainerState) drawable.getConstantState();
            Drawable[] children = drawableContainerState.getChildren();
            GradientDrawable focusedItem = (GradientDrawable) children[0];
            focusedItem.setStroke(mBorderWidth, mBorderColor);
        }
    }

    /**
     * Dispatch key events to be interpreted in the webview via API (since webview is non focusable).
     *
     * @param event key event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatchFirst = super.dispatchKeyEvent(event);

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    WebviewUtils.callJavaScript(mWebView, "playPause");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    WebviewUtils.callJavaScript(mWebView, "playVideo");
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    WebviewUtils.callJavaScript(mWebView, "playPause");
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    WebviewUtils.callJavaScript(mWebView, "nextVideo");
                    break;
            }
        }
        return dispatchFirst;
    }

    @Override
    public void start() {
        WebviewUtils.callOnWebviewThread(mWebView, "playVideo");
    }

    @Override
    public void pause() {
        WebviewUtils.callOnWebviewThread(mWebView, "pauseVideo");
    }

    @Override
    public void playPause() {
        WebviewUtils.callOnWebviewThread(mWebView, "playPause");
    }

    @Override
    public void stopVideo() {
        WebviewUtils.callOnWebviewThread(mWebView, "stopVideo");
    }

    @Override
    public void seekTo(int seconds, boolean allowSeekAhead) {
        WebviewUtils.callOnWebviewThread(mWebView, "seekTo", seconds, allowSeekAhead);
    }

    @Override
    public void seekTo(int seconds) {
        seekTo(seconds, true);
    }

    @Override
    public void nextVideo() {
        WebviewUtils.callOnWebviewThread(mWebView, "nextVideo");
    }

    @Override
    public void previousVideo() {
        WebviewUtils.callOnWebviewThread(mWebView, "previousVideo");
    }

    @Override
    public void playVideoAt(int index) {
        WebviewUtils.callOnWebviewThread(mWebView, "playVideoAt", index);
    }

    @Override
    public void mute() {
        WebviewUtils.callOnWebviewThread(mWebView, "mute");
    }

    @Override
    public void unMute() {
        WebviewUtils.callOnWebviewThread(mWebView, "unMute");
    }

    @Override
    public void moveForward(int seconds) {
        WebviewUtils.callOnWebviewThread(mWebView, "moveForward", seconds);
    }

    @Override
    public void moveBackward(int seconds) {
        WebviewUtils.callOnWebviewThread(mWebView, "moveBackward", seconds);
    }

    @Override
    public boolean isPlaying() {
        return getPlayerState() == VideoState.PLAYING ? true : false;
    }

    @Override
    public boolean isMuted() throws InterruptedException {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "isMuted");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.isMuted();
    }

    @Override
    public void setVolume(int volume) {
        WebviewUtils.callOnWebviewThread(mWebView, "setVolume", volume);
    }

    @Override
    public int getVolume() throws InterruptedException {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getVolume");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getVolume();
    }

    @Override
    public void setSize(int width, int height) {
        WebviewUtils.callOnWebviewThread(mWebView, "setSize", width, height);
    }

    @Override
    public int getPlaybackRate() throws InterruptedException {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getPlaybackRate");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getPlaybackRate();
    }

    @Override
    public void setPlaybackRate(int suggestedRate) {
        WebviewUtils.callOnWebviewThread(mWebView, "setPlaybackRate", suggestedRate);
    }

    @Override
    public List<Integer> getAvailablePlaybackRates() throws InterruptedException {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getAvailablePlaybackRateList");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getAvailablePlaybackRates();
    }

    @Override
    public void setLoop(boolean loopPlaylists) {
        WebviewUtils.callOnWebviewThread(mWebView, "setLoop", loopPlaylists);
    }

    @Override
    public void setShuffle(boolean shufflePlaylist) {
        WebviewUtils.callOnWebviewThread(mWebView, "setShuffle", shufflePlaylist);
    }

    @Override
    public float getVideoLoadedFraction() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getVideoLoadedFraction");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getVideoLoadedFraction();
    }

    @Override
    public VideoState getPlayerState() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getPlayerState");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getPlayerState();
    }

    @Override
    public float getCurrentPosition() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getCurrentTime");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getCurrentTime();
    }

    @Override
    public void setPlaybackQuality(VideoQuality suggestedQuality) {
        WebviewUtils.callOnWebviewThread(mWebView, "setPlaybackQuality", suggestedQuality.getValue());
    }

    @Override
    public VideoQuality getPlaybackQuality() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getPlaybackQuality");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getPlaybackQuality();
    }

    @Override
    public List<VideoQuality> getAvailableQualityLevels() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getAvailableQualityLevels");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getAvailableQualityLevels();
    }

    @Override
    public float getDuration() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getDuration");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getDuration();
    }

    @Override
    public String getVideoUrl() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getVideoUrl");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getVideoUrl();
    }

    @Override
    public String getVideoEmbedCode() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getVideoEmbedCode");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getVideoEmbedCode();
    }

    @Override
    public List<String> getPlaylist() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getPlaylist");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getPlaylist();
    }

    @Override
    public int getPlaylistIndex() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getPlaylistIndex");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getPlaylistIndex();
    }

    @Override
    public String getVideoId() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getVideoId");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getVideoId();
    }

    @Override
    public String getVideoTitle() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getVideoTitle");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getVideoTitle();
    }

    @Override
    public VideoInfo getVideoInfo() {
        synchronized (mLock) {
            mBlock = new ConditionVariable();
            mJavascriptInterface.setBlock(mBlock);
            WebviewUtils.callOnWebviewThread(mWebView, "getVideoInfo");
            mBlock.block(mJavascriptTimeout);
        }
        return mJavascriptInterface.getVideoInfo();
    }

    @Override
    public void playVideo(String videoId) {
        mVideoId = videoId;
        init();
    }

    /**
     * Add a player listener.
     *
     * @param listener
     */
    @Override
    public void addPlayerListener(final IPlayerListener listener) {
        mPlayerListenerList.add(listener);
    }

    /**
     * Remove player listener.
     *
     * @param listener
     */
    @Override
    public void removePlayerListener(final IPlayerListener listener) {
        mPlayerListenerList.remove(listener);
    }

    @Override
    public void setOnBufferingUpdateListener(IBufferStateListener listener) {
        mJavascriptInterface.setOnBufferingUpdateListener(listener);
    }

    /**
     * Get media session (for now playing card management).
     *
     * @return
     */
    public MediaSession getMediaSession() {
        return mMediaSession;
    }

    /**
     * Check if now playing card is to be shown.
     *
     * @return
     */
    public boolean isShowingNowPlayingCard() {
        return mShowNowPlayingCard;
    }

    /**
     * Set progress update listener.
     *
     * @param listener
     */
    @Override
    public void setOnProgressUpdateListener(IProgressUpdateListener listener) {
        mJavascriptInterface.setOnProgressUpdateListener(listener);
    }
}
