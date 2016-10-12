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

    private MotionEvent mEventDown;

    private MotionEvent mEventUp;

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

        float x = 1000f;
        float y = 500f;

        int metaState = 0;
        mEventDown = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );

        // Dispatch touch event to view
        mViewGroup.dispatchTouchEvent(mEventDown);

        mEventUp = MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        mViewGroup.dispatchTouchEvent(mEventUp);
        Log.i("start", "end of touch event");
    }
}
