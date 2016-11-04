package fr.bmartel.youtubetv.example.samples;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import fr.bmartel.youtubetv.YoutubeTvFragment;
import fr.bmartel.youtubetv.example.R;

/**
 * Created by akinaru on 03/11/16.
 */
public class YoutubeActivityFragment extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        // Begin the transaction
        FragmentTransaction fTransaction = getFragmentManager().beginTransaction();
        //youtubeTvFragment.setPlayerBackgroundColor(ContextCompat.getColor(this, R.color.player_background_color));
        //youtubeTvFragment.setPlayerProgressColor(ContextCompat.getColor(this, R.color.player_background_color));
        fTransaction.replace(R.id.youtube_fragment, YoutubeTvFragment.newInstance("SynzKC4fWp0"));
        fTransaction.commit();
        requestVisibleBehind(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestVisibleBehind(true);
    }
}
