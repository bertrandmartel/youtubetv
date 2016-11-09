package fr.bmartel.youtubetv.showcase.samples;

import android.app.Activity;
import android.os.Bundle;

import fr.bmartel.youtubetv.YoutubeTvView;
import fr.bmartel.youtubetv.showcase.R;

/**
 * Created by akinaru on 04/11/16.
 */
public class YoutubeTvViewFullScreen extends Activity {

    private YoutubeTvView mYoutubeView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfullscreen);

        mYoutubeView = (YoutubeTvView) findViewById(R.id.youtube_video);

        requestVisibleBehind(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestVisibleBehind(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mYoutubeView.closePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
