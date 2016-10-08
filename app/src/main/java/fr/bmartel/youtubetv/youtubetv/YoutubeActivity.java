package fr.bmartel.youtubetv.youtubetv;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * YoutubeActivity
 *
 * @author Bertrand Martel
 */
public class YoutubeActivity extends Activity implements View.OnTouchListener {

    private WebView webview;

    private String videoId = "C5zzVemi3m4";

    private final static String TAG = YoutubeActivity.class.getSimpleName();

    //https://developers.google.com/youtube/iframe_api_reference#Playback_quality
    private VideoQuality videoQuality = VideoQuality.HD_720;

    private int playerHeight = 1080;

    private int playerWidth = 1920;

    //check https://developers.google.com/youtube/player_parameters?playerVersion=HTML5

    private int showRelatedVideos = 0;

    private int showVideoInfo = 0;

    private int showControls = 0;

    private int autohide = 2;

    private int closedCaptions = 0;

    private int videoAnnotation = 3;

    @Override
    protected void onPause() {
        super.onPause();
        webview.onPause();
    }

    @Override
    protected void onResume() {
        webview.onResume();
        super.onResume();
    }

    private int getScale() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(1920);
        val = val * 100d;
        return val.intValue();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        webview = new WebView(this);
        setContentView(webview);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        Log.i("width", "display width : " + width + " | height : " + height);

        final WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webview.setWebChromeClient(new WebChromeClient());
        webview.setPadding(0, 0, 0, 0);
        webview.setInitialScale(getScale());

        JavascriptInterface jsInterface = new JavascriptInterface(this);
        webview.addJavascriptInterface(jsInterface, "JSInterface");

        System.out.println("videoQuality.getValue() : " + videoQuality.getValue());

        webview.loadUrl("file:///android_asset/youtube.html" +
                "?videoId=" + videoId +
                "&videoQuality=" + videoQuality.getValue() +
                "&playerHeight=" + playerHeight +
                "&playerWidth=" + playerWidth +
                "&rel=" + showRelatedVideos +
                "&showinfo=" + showVideoInfo +
                "&controls=" + showControls +
                "&autohide=" + autohide +
                "&cc_load_policy=" + closedCaptions +
                "&iv_load_policy=" + videoAnnotation);
    }

    private void callJavaScript(String methodName, Object... params) {

        Log.i(TAG, "calling : " + methodName);

        StringBuilder stringBuilder = new StringBuilder();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            stringBuilder.append("javascript:try{");
        }
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                stringBuilder.append("'");
                stringBuilder.append(param);
                stringBuilder.append("'");
            }
            if (i < params.length - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")}catch(error){Android.onError(error.message);}");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webview.evaluateJavascript(stringBuilder.toString(), new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String result) {
                    Log.i(TAG, "received : " + result);
                }
            });
        } else {
            webview.loadUrl(stringBuilder.toString());
        }

        webview.loadUrl(stringBuilder.toString());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                "View touched",
                Toast.LENGTH_LONG
        );
        toast.show();

        return true;
    }
}