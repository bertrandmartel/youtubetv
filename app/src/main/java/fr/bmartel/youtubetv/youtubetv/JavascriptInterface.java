package fr.bmartel.youtubetv.youtubetv;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by akinaru on 22/07/16.
 */
public class JavascriptInterface {

    private Activity activity;

    public JavascriptInterface(Activity activiy) {
        this.activity = activiy;
    }

    @android.webkit.JavascriptInterface
    public void log(String header, String message) {
        Log.i(header, message);
    }

    @android.webkit.JavascriptInterface
    public void onError(String error){
        throw new Error(error);
    }

    @android.webkit.JavascriptInterface
    public void startVideo() {
        Log.i("start", "start video");
        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 1000f;
        float y = 500f;

        // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );

        // Dispatch touch event to view
        activity.dispatchTouchEvent(motionEvent);


        downTime = SystemClock.uptimeMillis();
        eventTime = SystemClock.uptimeMillis() + 100;
        motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        activity.dispatchTouchEvent(motionEvent);
    }
}
