package fr.bmartel.youtubetv;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import fr.bmartel.youtubetv.utils.WebviewUtils;

/**
 * Youtuve TV custom view.
 *
 * @author Bertrand Martel
 */
public class YoutubeTvView extends FrameLayout {

    private final static String USER_AGENT_CHROME_DESKTOP = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";

    private final static String USER_AGENT_IPHONE = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_1_4 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B350 Safari/8536.25";

    private String mVideoId = "EZcJEvXmjfY";

    private final static String TAG = YoutubeTvView.class.getSimpleName();

    private final static int DEFAULT_LOADING_BG = 0x00000000;

    //https://developers.google.com/youtube/iframe_api_reference#Playback_quality
    private VideoQuality mVideoQuality = VideoQuality.HD_1080;

    private int mPlayerHeight = 1080;

    private int mPlayerWidth = 1920;

    //check https://developers.google.com/youtube/player_parameters?playerVersion=HTML5

    private int mShowRelatedVideos = 0;

    private int mShowVideoInfo = 0;

    private VideoControls mShowControls = VideoControls.NONE;

    private VideoAutoHide mAutohide = VideoAutoHide.DEFAULT;

    private int mClosedCaptions = 1;

    private int mVideoAnnotation = 1;

    private int mDebug = 0;

    private int mViewWidth = 0;
    private int mViewHeight = 0;

    private int mLoadBackgroundColor = DEFAULT_LOADING_BG;

    private JavascriptInterface mJavascriptInterface;

    private WebView mWebView;
    private RelativeLayout mLoadingProgress;
    private Handler mHandler;

    public YoutubeTvView(Context context) {
        super(context);
        init(context);
    }

    public YoutubeTvView(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttr(context, attrs);
        init(context);
    }

    public YoutubeTvView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttr(context, attrs);
        init(context);
    }

    private void processAttr(final Context context, AttributeSet attrs) {

        TypedArray styledAttr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.YoutubeTvView,
                0, 0);

        try {
            mVideoId = styledAttr.getString(R.styleable.YoutubeTvView_videoId);
            mVideoQuality = VideoQuality.getVideoQuality(styledAttr.getInteger(R.styleable.YoutubeTvView_videoQuality, VideoQuality.HD_720.getIndex()));
            mShowRelatedVideos = styledAttr.getBoolean(R.styleable.YoutubeTvView_showRelatedVideos, false) ? 1 : 0;
            mShowVideoInfo = styledAttr.getBoolean(R.styleable.YoutubeTvView_showVideoInfo, false) ? 1 : 0;
            mShowControls = VideoControls.getVideoControls(styledAttr.getInteger(R.styleable.YoutubeTvView_showControls, VideoControls.NONE.getIndex()));
            mClosedCaptions = styledAttr.getBoolean(R.styleable.YoutubeTvView_closedCaptions, false) ? 1 : 0;
            mVideoAnnotation = styledAttr.getBoolean(R.styleable.YoutubeTvView_videoAnnotation, false) ? 1 : 3;
            mAutohide = VideoAutoHide.getVideoControls(styledAttr.getInteger(R.styleable.YoutubeTvView_autoHide, VideoAutoHide.DEFAULT.getIndex()));
            mDebug = styledAttr.getBoolean(R.styleable.YoutubeTvView_debug, false) ? 1 : 0;
            mLoadBackgroundColor = styledAttr.getInteger(R.styleable.YoutubeTvView_loadingBackgroundColor, DEFAULT_LOADING_BG);
        } finally {
            styledAttr.recycle();
        }
    }

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

    private void init(final Context context) {

        inflate(getContext(), R.layout.youtube_view, this);

        mWebView = (WebView) findViewById(R.id.youtube_view);
        mLoadingProgress = (RelativeLayout) findViewById(R.id.loading_progressbar);
        mHandler = new Handler();

        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                mLoadingProgress.setVisibility(GONE);
            }
        });

        mWebView.setBackgroundColor(mLoadBackgroundColor);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setPadding(0, 0, 0, 0);
        mWebView.setInitialScale(WebviewUtils.getWebviewScale(display));
        mWebView.setScrollbarFadingEnabled(true);

        mJavascriptInterface = new JavascriptInterface(mHandler,mLoadingProgress, mWebView);
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
                "&debug=" + mDebug;

        Log.v(TAG, "videoUrl : " + videoUrl);

        mWebView.loadUrl(videoUrl);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatchFirst = super.dispatchKeyEvent(event);

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (event.getKeyCode()) {
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
