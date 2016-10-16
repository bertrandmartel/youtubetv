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

import android.graphics.Color;

import fr.bmartel.youtubetv.model.ThumbnailQuality;
import fr.bmartel.youtubetv.model.UserAgents;
import fr.bmartel.youtubetv.model.VideoAutoHide;
import fr.bmartel.youtubetv.model.VideoControls;
import fr.bmartel.youtubetv.model.VideoQuality;

/**
 * Constants for YoutubeTv view.
 *
 * @author Bertrand Martel
 */
public class YoutubeTvConst {

    /**
     * Default webview background color.
     */
    public final static int DEFAULT_LOADING_BG = 0x00000000;

    /**
     * Default video id to be played.
     */
    public final static String DEFAULT_VIDEO_ID = "";

    /**
     * Default video quality.
     */
    public final static VideoQuality DEFAULT_VIDEO_QUALITY = VideoQuality.HD_1080;

    /**
     * Define default value for showing related video at the end.
     */
    public final static boolean DEFAULT_SHOW_RELATED_VIDEOS = false;

    /**
     * Define default value for showing video information at the beginning.
     */
    public final static boolean DEFAULT_SHOW_VIDEO_INFO = false;

    /**
     * Default value for showing controls in video.
     */
    public final static VideoControls DEFAULT_SHOW_CONTROLS = VideoControls.NONE;

    /**
     * Default value for autohide feature.
     */
    public final static VideoAutoHide DEFAULT_AUTOHIDE = VideoAutoHide.DEFAULT;

    /**
     * Show closed caption by default.
     */
    public final static boolean DEFAULT_CLOSED_CAPTION = false;

    /**
     * Show video annotation by default.
     */
    public final static boolean DEFAULT_VIDEO_ANNOTATION = false;

    /**
     * Default debug mode value.
     */
    public final static boolean DEFAULT_DEBUG_MODE = false;

    /**
     * Default value for autoplay.
     */
    public final static boolean DEFAULT_AUTOPLAY = true;

    /**
     * Default user agent string used.
     */
    public final static UserAgents DEFAULT_USER_AGENT = UserAgents.CHROME_IPHONE;

    /**
     * Show border around webview.
     */
    public final static boolean DEFAULT_SHOW_BORDER = false;

    /**
     * Default border width in dp.
     */
    public final static int DEFAULT_BORDER_WIDTH = 2;

    /**
     * Default border color.
     */
    public final static int DEFAULT_BORDER_COLOR = Color.BLUE;

    /**
     * Default thumbnail quality.
     */
    public final static ThumbnailQuality DEFAULT_THUMBNAIL_QUALITY = ThumbnailQuality.MAXRES_DEFAULT;

}
