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

package fr.bmartel.youtubetv.utils;

import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.bmartel.youtubetv.YoutubeTvConst;

/**
 * Utility functions for Webview.
 *
 * @author Bertrand Martel
 */
public class WebviewUtils {

    /**
     * Call javascript functions in webview.
     *
     * @param webView    webview object
     * @param methodName function name
     * @param params     function parameters
     */
    public static void callJavaScript(final WebView webView, String methodName, Object... params) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:try{");
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        String separator = "";
        for (Object param : params) {
            stringBuilder.append(separator);
            separator = ",";
            if (param instanceof String) {
                stringBuilder.append("'");
            }
            stringBuilder.append(param);
            if (param instanceof String) {
                stringBuilder.append("'");
            }

        }
        stringBuilder.append(")}catch(error){console.error(error.message);}");
        final String call = stringBuilder.toString();

        webView.loadUrl(call);
    }

    /**
     * Get youtube thumbnail URL.
     *
     * @param videoId youtube video id
     * @param quality youtube thumbnail quality
     * @return thumbnail URL
     */
    public static String getThumbnailURL(final String videoId, final String quality) {
        return "http://img.youtube.com/vi/" + videoId + "/" + quality + ".jpg";
    }

    /**
     * Get best thumbnail quality from responding thumbnail URL.
     *
     * @param videoId          youtube video id
     * @param suggestedQuality best quality that should match
     * @return the best quality found
     * @throws IOException
     */
    public static String getThumbnailQuality(final String videoId, final String suggestedQuality) throws IOException {

        boolean check = false;
        for (int i = 0; i < YoutubeTvConst.THUMBNAIL_QUALITY_LIST.size(); i++) {

            if (suggestedQuality.equals(YoutubeTvConst.THUMBNAIL_QUALITY_LIST.get(i))) {
                check = true;
            }

            if (check) {
                if (isUrlExists(getThumbnailURL(videoId, YoutubeTvConst.THUMBNAIL_QUALITY_LIST.get(i)))) {
                    return getThumbnailURL(videoId, YoutubeTvConst.THUMBNAIL_QUALITY_LIST.get(i));
                }
            }
        }

        return YoutubeTvConst.DEFAULT_THUMBNAIL_URL;
    }

    /**
     * Check if url exists.
     *
     * @param url URL to check
     * @return
     * @throws IOException
     */
    public static boolean isUrlExists(final String url) throws IOException {
        HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        int code = huc.getResponseCode();
        huc.disconnect();
        return (code != 404);
    }

}
