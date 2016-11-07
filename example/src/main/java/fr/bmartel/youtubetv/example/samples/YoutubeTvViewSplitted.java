package fr.bmartel.youtubetv.example.samples;

import android.app.Activity;
import android.os.Bundle;

import fr.bmartel.youtubetv.YoutubeTvView;
import fr.bmartel.youtubetv.example.R;

/**
 * Created by akinaru on 04/11/16.
 */
public class YoutubeTvViewSplitted extends Activity {

    private YoutubeTvView mYoutubeView1;
    private YoutubeTvView mYoutubeView2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splitted);

        mYoutubeView1 = (YoutubeTvView) findViewById(R.id.youtube_video1);
        mYoutubeView2 = (YoutubeTvView) findViewById(R.id.youtube_video2);

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
        mYoutubeView1.closePlayer();
        mYoutubeView2.closePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
