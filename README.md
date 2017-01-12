# YoutubeTV library

[![CircleCI](https://img.shields.io/circleci/project/bertrandmartel/youtubetv.svg?maxAge=2592000?style=plastic)](https://circleci.com/gh/bertrandmartel/youtubetv) 
[![Download](https://api.bintray.com/packages/bertrandmartel/maven/youtubetv/images/download.svg) ](https://bintray.com/bertrandmartel/maven/youtubetv/_latestVersion)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.bmartel/youtubetv/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.bmartel/youtubetv)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6dda219f0bf549058b6ba464311ec738)](https://www.codacy.com/app/bertrandmartel/youtubetv?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=bertrandmartel/youtubetv&amp;utm_campaign=Badge_Grade)
[![Javadoc](http://javadoc-badge.appspot.com/fr.bmartel/youtubetv.svg?label=javadoc)](http://javadoc-badge.appspot.com/fr.bmartel/youtubetv)
[![License](http://img.shields.io/:license-mit-blue.svg)](LICENSE.md)

YouTube embedded player library for Android TV

## Purpose

* to embed Youtube videos in your own Android TV application with [Youtube Player API for iframe Embeds](https://developers.google.com/youtube/iframe_api_reference)
* to provide an alternative to [the lack of Youtube Android Player API on Android TV platform](https://code.google.com/p/gdata-issues/issues/detail?id=6998)

Download YoutubeTv library Showcase from Google Play :
 
[![Download YoutubeTv library Showcase from Google Play](http://www.android.com/images/brand/android_app_on_play_large.png)](https://play.google.com/store/apps/details?id=fr.bmartel.youtubetv.showcase)

## Features

* a custom view `YoutubeTvView` that embeds a Webview with Youtube iframe
* a custom fragment `YoutubeTvFragment` that holds a `YoutubeTvView` with a media control bar (`PlaybackOverlayFragment`)
* all Javascript API for [iframe Embeds](https://developers.google.com/youtube/iframe_api_reference) are available from `YoutubeTvView`
* video autoplay
* a video thumbnail is shown at the beginning waiting for the player to be ready
* a loading progress bar is shown waiting for the `PLAY` status

## Include in your project

with Gradle, from jcenter :
```
compile 'fr.bmartel:youtubetv:1.0'
```

Minimum SDK : 21

## YoutubeTvView

`YoutubeTvView` is the custom view that holds Youtube iframe inside a `Webview`.

#### Layout

```
<fr.bmartel.youtubetv.YoutubeTvView
    android:id="@+id/youtube_video"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:videoId="4a5m4qF1e6Q" />
```

#### Attribute list

| attribute name |  format     | default value |  description   |
|----------------|-------------|---------------|----------------|
| videoId        | string      |               | Youtube video Id |
| playlistId     | string      |               | playlist Id (if video is part of a playlist) |
| videoQuality   | enum        |    hd1080     | suggested quality |
| showNowPlayingCard | boolean |     true      | define if now playing card must be shown or not |
| showRelatedVideos | boolean  |     false     | display related video when video ends |
| showVideoInfo    | boolean  |      false     | show video info before playing video |
| showControls   | enum |            none      | define if iframe control bar is shown (auto/always/none) |
| autoHide  | enum | alwaysVisible | control iframe control bar visibility (auto/always/none) |
| closedCaptions | boolean | false  | display closed captions |
| closedCaptionLangPref | string |  | closed captions preference language |
| playerLanguage | string |  | youtube player language |
| videoAnnotation | boolean | false | display video annotations |
| debug | boolean | false | active/disactive debug mode |
| loadingBackgroundColor | integer | #00000000 | set loading background color |
| autoplay | boolean | true | autoplay the video or initiate on click |
| userAgentString | string | iphone(*) | user agent string used for the Webview | 
| showBorder | boolean | false | define if a thin border is shown when the View is selected |
| borderWidth | integer | 2 | selection border width in dp (no effect if showBorder not set) |
| borderColor | color | Color.BLUE | selection border color (no effect if showBorder not set)
| thumbnailQuality | enum |  maxresdefault | suggested quality for the thumbnail displayed before the video plays | 
| javascriptTimeout | integer | 1500 | timeout value in ms for Javascript API that return values from JS side |

(*) There is currently a bug on regular `android` user agent string that restricts video quality to `large` and below. The bug doesn't affect `desktop`, `iphone` or `ipad` user agent string. So, in order to have quality `hd720` or `hd1080` we must change user agent string.

* suggested `videoQuality`

 * auto
 * tiny
 * small
 * medium
 * large
 * hd720
 * hd1080
 * highres
 * hd1440
 * hd2160

* suggested `thumbnailQuality`

 * auto
 * hqdefault
 * mqdefault
 * sddefault
 * maxresdefault

## YoutubeTvFragment

`YoutubeTvFragment` holds a `YoutubeTvView` with a `PlaybackOverlayFragment` that is used to display a media control bar.

#### Usage

* in your `Activity`'s `onCreate` : 

```
FragmentTransaction fTransaction = getFragmentManager().beginTransaction();

Bundle args = new Bundle();
args.putString("videoId", "gdgHZi347hU");
args.putString("videoQuality", "hd1080");

YoutubeTvFragment mYtFragment = YoutubeTvFragment.newInstance(args);
fTransaction.replace(R.id.youtube_fragment, mYtFragment);
fTransaction.commit();
```

## Proguard

The following is required to keep Javascript interface from obfuscation :
```
-keep class fr.bmartel.youtubetv.JavascriptInterface { *; }

-keepclassmembers,allowobfuscation class fr.bmartel.youtubetv.JavascriptInterface.** {
    <methods>;
}
```

## External libraries

* YoutubeTv library
 * [leanback-v17](https://developer.android.com/reference/android/support/v17/leanback/package-summary.html)

* Showcase application
 * [leanback-v17](https://developer.android.com/reference/android/support/v17/leanback/package-summary.html)
 * [gson](https://github.com/google/gson)
 * [picasso](https://github.com/square/picasso)

## License

The MIT License (MIT) Copyright (c) 2016 Bertrand Martel
