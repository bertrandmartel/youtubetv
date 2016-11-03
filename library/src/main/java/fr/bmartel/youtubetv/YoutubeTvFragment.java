package fr.bmartel.youtubetv;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.bmartel.youtubetv.media.VideoConsumptionExampleFragment;
import fr.bmartel.youtubetv.media.VideoSurfaceFragment;

/**
 * Created by akinaru on 03/11/16.
 */
public class YoutubeTvFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.youtube_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FragmentTransaction ft1 = getChildFragmentManager().beginTransaction();
        ft1.replace(R.id.videoFragment, new VideoSurfaceFragment(), VideoSurfaceFragment.TAG);
        ft1.commit();

        FragmentTransaction ft2 = getChildFragmentManager().beginTransaction();
        VideoConsumptionExampleFragment videoFragment = new VideoConsumptionExampleFragment();
        ft2.add(R.id.videoFragment, videoFragment, VideoConsumptionExampleFragment.TAG);
        ft2.commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
