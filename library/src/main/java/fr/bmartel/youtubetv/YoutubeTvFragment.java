package fr.bmartel.youtubetv;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.bmartel.youtubetv.media.VideoPlayerFragment;
import fr.bmartel.youtubetv.media.VideoSurfaceFragment;

/**
 * Created by akinaru on 03/11/16.
 */
public class YoutubeTvFragment extends Fragment {

    private VideoPlayerFragment mVideoFragment;

    private String mVideoId;

    public static YoutubeTvFragment newInstance(String videoId) {
        YoutubeTvFragment fragment = new YoutubeTvFragment();

        Bundle args = new Bundle();
        args.putString("videoId", videoId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoId = getArguments().getString("videoId", "");
    }

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
        mVideoFragment = VideoPlayerFragment.newInstance(mVideoId);
        ft2.add(R.id.videoFragment, mVideoFragment, VideoPlayerFragment.TAG);
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
