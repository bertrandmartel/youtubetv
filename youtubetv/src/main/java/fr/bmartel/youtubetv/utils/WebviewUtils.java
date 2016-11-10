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

import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.util.Log;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.bmartel.youtubetv.YoutubeTvConst;
import fr.bmartel.youtubetv.model.VideoQuality;

/**
 * Utility functions for Webview.
 *
 * @author Bertrand Martel
 */
public class WebviewUtils {

    private final static String TAG = WebviewUtils.class.getSimpleName();

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

            if (check && isUrlExists(getThumbnailURL(videoId, YoutubeTvConst.THUMBNAIL_QUALITY_LIST.get(i)))) {
                return getThumbnailURL(videoId, YoutubeTvConst.THUMBNAIL_QUALITY_LIST.get(i));
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

    /**
     * Call javascript functions in webview thread.
     *
     * @param webView    webview object
     * @param methodName function name
     * @param params     function parameters
     */
    public static void callOnWebviewThread(final WebView webView, final String methodName, final Object... params) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                WebviewUtils.callJavaScript(webView, methodName, params);
            }
        });
    }

    public static List<Integer> parsePlaybackRates(String playbackRates) {
        List<Integer> playBackList = new ArrayList<>();

        if (playbackRates != null && !playbackRates.isEmpty()) {
            try {
                JSONArray playBackArr = new JSONArray(playbackRates);

                for (int i = 0; i < playBackArr.length(); i++) {
                    playBackList.add(playBackArr.getInt(i));
                }
            } catch (JSONException e) {
                Log.e(TAG, "parse playback rates", e);
            }
        }
        return playBackList;
    }

    public static List<VideoQuality> parseQualityLevels(String qualityLevels) {
        List<VideoQuality> qualityLevelList = new ArrayList<>();

        if (qualityLevels != null && !qualityLevels.isEmpty()) {
            try {
                JSONArray playBackArr = new JSONArray(qualityLevels);

                for (int i = 0; i < playBackArr.length(); i++) {
                    qualityLevelList.add(VideoQuality.getVideoQuality(playBackArr.getString(i)));
                }
            } catch (JSONException e) {
                Log.e(TAG, "parse quality levels", e);
            }
        }
        return qualityLevelList;
    }

    public static List<String> parsePlaylist(String playlist) {

        List<String> playlistRet = new ArrayList<>();

        if (playlist != null && !playlist.isEmpty() && !playlist.equals("null")) {

            try {
                JSONArray playBackArr = new JSONArray(playlist);

                for (int i = 0; i < playBackArr.length(); i++) {
                    playlistRet.add(playBackArr.getString(i));
                }
            } catch (JSONException e) {
                Log.e(TAG, "parse playlist", e);
            }
        }
        return playlistRet;
    }

    public static void updateMediaSession(boolean updateMetadata,
                                          MediaMetadata.Builder mediaBuilder,
                                          final MediaSession mediaSession,
                                          final int playbackState,
                                          final long position,
                                          final float speed) {

        if (mediaSession.isActive()) {

            if (updateMetadata) {
                mediaSession.setMetadata(mediaBuilder.build());
            }
            PlaybackState.Builder stateBuilder = new PlaybackState.Builder();
            stateBuilder.setState(playbackState,
                    position,
                    speed);
            mediaSession.setPlaybackState(stateBuilder.build());
        }
    }
}
