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
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import fr.bmartel.youtubetv.model.VideoAutoHide;
import fr.bmartel.youtubetv.model.VideoControls;
import fr.bmartel.youtubetv.model.VideoQuality;
import fr.bmartel.youtubetv.utils.WebviewUtils;

/**
 * Youtube TV custom view.
 *
 * @author Bertrand Martel
 */
public class YoutubeTvView extends FrameLayout {

    private final static String TAG = YoutubeTvView.class.getSimpleName();

    private final static String USER_AGENT_CHROME_DESKTOP = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

    private final static String USER_AGENT_IPHONE = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_1_4 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B350 Safari/8536.25";

    private String mVideoId;

    //https://developers.google.com/youtube/iframe_api_reference#Playback_quality
    private VideoQuality mVideoQuality;

    //check https://developers.google.com/youtube/player_parameters?playerVersion=HTML5
    private int mShowRelatedVideos;

    private int mShowVideoInfo;

    private VideoControls mShowControls;

    private VideoAutoHide mAutohide;

    private int mClosedCaptions;

    private int mVideoAnnotation;

    private int mDebug;

    private int mViewWidth;

    private int mViewHeight;

    private int mLoadBackgroundColor = YoutubeTvConst.DEFAULT_LOADING_BG;

    private JavascriptInterface mJavascriptInterface;

    private WebView mWebView;

    private RelativeLayout mLoadingProgress;

    private Handler mHandler;

    private int mAutoPlay;

    /**
     * Build Custom view.
     *
     * @param context android context
     */
    public YoutubeTvView(Context context) {
        super(context);
        init(context);
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
        init(context);
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
        init(context);
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
    private void init(final Context context) {

        inflate(getContext(), R.layout.youtube_view, this);

        mWebView = (WebView) findViewById(R.id.youtube_view);
        mLoadingProgress = (RelativeLayout) findViewById(R.id.loading_progressbar);
        mHandler = new Handler();

        mWebView.setBackgroundColor(mLoadBackgroundColor);

        //Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setPadding(0, 0, 0, 0);
        //mWebView.setInitialScale(WebviewUtils.getWebviewScale(display));
        mWebView.setScrollbarFadingEnabled(true);

        mJavascriptInterface = new JavascriptInterface(mHandler, mLoadingProgress, mWebView);
        mWebView.addJavascriptInterface(mJavascriptInterface, "JSInterface");

        mWebView.getSettings().setUserAgentString(USER_AGENT_IPHONE);

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
                "&debug=" + mDebug;

        Log.v(TAG, "videoUrl : " + videoUrl);

        mWebView.loadUrl(videoUrl);
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

        Log.v(TAG, "dispatchKeyEvent : " + event.getAction() + " et " + event.getKeyCode());

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
                    WebviewUtils.callJavaScript(mWebView, "pauseVideo");
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    WebviewUtils.callJavaScript(mWebView, "nextVideo");
                    break;
            }
        }
        return dispatchFirst;
    }
}
