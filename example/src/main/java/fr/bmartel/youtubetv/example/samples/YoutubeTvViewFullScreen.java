package fr.bmartel.youtubetv.example.samples;

import android.app.Activity;
import android.os.Bundle;

import fr.bmartel.youtubetv.example.R;

/**
 * Created by akinaru on 04/11/16.
 */
public class YoutubeTvViewFullScreen extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfullscreen);

        requestVisibleBehind(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestVisibleBehind(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
