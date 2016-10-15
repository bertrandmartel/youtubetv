package fr.bmartel.youtubetv;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import fr.bmartel.youtubetv.utils.WebviewUtils;

/**
 * Interface called from Javascript.
 *
 * @author Bertrand Martel
 */
public class JavascriptInterface {

    private WebView mWebview;

    private boolean mLoaded;

    private RelativeLayout mLoadingProgress;

    private Handler mHandler;

    public JavascriptInterface(final Handler handler, final RelativeLayout loadingBar, final WebView webView) {
        this.mWebview = webView;
        this.mLoadingProgress = loadingBar;
        this.mHandler = handler;
    }

    private MotionEvent mEventDown;

    private MotionEvent mEventUp;

    private boolean mWaitLoaded;

    private int mViewWidth;
    private int mViewHeight;

    public boolean isPageLoaded() {
        return mLoaded;
    }

    @android.webkit.JavascriptInterface
    public void log(String header, String message) {
        Log.i(header, message);
    }

    @android.webkit.JavascriptInterface
    public void onError(String error) {
        throw new Error(error);
    }

    @android.webkit.JavascriptInterface
    public void hideLoading() {
        if (mLoadingProgress != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLoadingProgress.setVisibility(View.GONE);
                }
            });
        }
    }

    @android.webkit.JavascriptInterface
    public void onPageLoaded() {
        mLoaded = true;
        if (mWaitLoaded) {
            mWebview.post(new Runnable() {
                @Override
                public void run() {
                    Log.v("test", "before setSize");
                    WebviewUtils.callJavaScript(mWebview, "setSize", mViewWidth, mViewHeight);
                }
            });
        }
    }

    @android.webkit.JavascriptInterface
    public void startVideo() {

        Log.i("start", "start video");

        mWebview.post(new Runnable() {
            @Override
            public void run() {
                WebviewUtils.callJavaScript(mWebview, "playVideo");
            }
        });
    }

    public void setSizeOnLoad(int viewWidth, int viewHeight) {
        mWaitLoaded = true;
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
    }
}
