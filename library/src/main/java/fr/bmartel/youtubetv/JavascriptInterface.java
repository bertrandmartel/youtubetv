package fr.bmartel.youtubetv;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Interface called from Javascript.
 *
 * @author Bertrand Martel
 */
public class JavascriptInterface {

    private ViewGroup mViewGroup;

    public JavascriptInterface(final ViewGroup viewGroup) {
        this.mViewGroup = viewGroup;
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
        mViewGroup.dispatchTouchEvent(motionEvent);


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
        mViewGroup.dispatchTouchEvent(motionEvent);
    }
}
