package fr.bmartel.youtubetv;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.bmartel.youtubetv.listener.IBufferStateListener;
import fr.bmartel.youtubetv.listener.IPlayerListener;
import fr.bmartel.youtubetv.listener.IProgressUpdateListener;
import fr.bmartel.youtubetv.media.VideoPlayerFragment;
import fr.bmartel.youtubetv.media.VideoSurfaceFragment;
import fr.bmartel.youtubetv.model.VideoInfo;
import fr.bmartel.youtubetv.model.VideoQuality;
import fr.bmartel.youtubetv.model.VideoState;

/**
 * Created by akinaru on 03/11/16.
 */
public class YoutubeTvFragment extends Fragment {

    private VideoPlayerFragment mVideoFragment;

    private Bundle mArgs;

    public static YoutubeTvFragment newInstance(Bundle arguments) {
        YoutubeTvFragment fragment = new YoutubeTvFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();
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
        mVideoFragment = VideoPlayerFragment.newInstance(mArgs);
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

    public void closePlayer() {
        mVideoFragment.closePlayer();
    }
}
