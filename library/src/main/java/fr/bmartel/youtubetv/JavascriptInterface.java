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

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import fr.bmartel.youtubetv.inter.IVolumeListener;
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
     * List of volume listener that are waiting for the volume value to be returned from JS.
     */
    private List<IVolumeListener> volumeListenerList;

    /**
     * Build JS interface.
     *
     * @param handler    Webview handler
     * @param loadingBar layout containing the progress bar
     * @param webView    Webview object
     */
    public JavascriptInterface(final Handler handler, final ProgressBar loadingBar, final ImageView playIcon, final WebView webView) {
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
    public void onVolumeReceived(final int volume) {

        Iterator<IVolumeListener> iterator = volumeListenerList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onVolumeReceived(volume);
            iterator.remove();
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
            Log.v(TAG, "best thumbnail quality is : " + quality);
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

        Log.v(TAG, "start video");

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
     * Add a new volume listener to volume listener list.
     *
     * @param volumeListener volume listener
     */
    public void addVolumeListener(IVolumeListener volumeListener) {
        volumeListenerList.add(volumeListener);
    }
}
