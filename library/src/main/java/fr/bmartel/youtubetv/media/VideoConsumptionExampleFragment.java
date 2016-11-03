/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package fr.bmartel.youtubetv.media;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v17.leanback.app.PlaybackOverlayFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;

import fr.bmartel.youtubetv.R;
import fr.bmartel.youtubetv.YoutubeTvView;
import fr.bmartel.youtubetv.listener.IVideoActivity;
import fr.bmartel.youtubetv.listener.IVideoInfoListener;
import fr.bmartel.youtubetv.model.VideoQuality;


public class VideoConsumptionExampleFragment extends PlaybackOverlayFragment implements
        OnItemViewClickedListener, MediaPlayerGlue.OnMediaFileFinishedPlayingListener {

    private static final String URL = "http://techslides.com/demos/sample-videos/small.mp4";
    public static final String TAG = "VideoConsumptionExampleFragment";
    private ArrayObjectAdapter mRowsAdapter;
    private MediaPlayerGlue mGlue;
    private IVideoInfoListener mQualityListener;
    private YoutubeTvView youtubeTvView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        youtubeTvView = (YoutubeTvView) getActivity().findViewById(R.id.youtubetv_view);

        mGlue = new VideoMediaPlayerGlue(getActivity(), this, youtubeTvView) {

            @Override
            protected void onRowChanged(PlaybackControlsRow row) {
                if (mRowsAdapter == null) return;
                mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
            }
        };
        mGlue.setOnMediaFileFinishedPlayingListener(this);

        //mGlue.setMediaSource(URL);
        mGlue.prepareMediaForPlaying();

        Fragment videoSurfaceFragment = getFragmentManager()
                .findFragmentByTag(VideoSurfaceFragment.TAG);

        //YoutubeTvView surface = (YoutubeTvView) videoSurfaceFragment.getView();
        /*
        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mGlue.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // Nothing to do
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
        */

        setBackgroundType(PlaybackOverlayFragment.BG_LIGHT);
        addPlaybackControlsRow();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGlue.enableProgressUpdating(mGlue.hasValidMedia() && mGlue.isMediaPlaying());
    }

    @Override
    public void onStop() {
        super.onStop();
        mGlue.enableProgressUpdating(false);
        mGlue.reset();
    }

    private void addPlaybackControlsRow() {
        final PlaybackControlsRowPresenter controlsPresenter = mGlue
                .createControlsRowAndPresenter();
        mRowsAdapter = new ArrayObjectAdapter(controlsPresenter);
        mRowsAdapter.add(mGlue.getControlsRow());
        setAdapter(mRowsAdapter);
        setOnItemViewClickedListener(this);
    }

    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                              RowPresenter.ViewHolder rowViewHolder, Row row) {
        if (!(item instanceof Action)) return;
        mGlue.onActionClicked((Action) item);
    }


    @Override
    public void onMediaFileFinishedPlaying(MediaPlayerGlue.MetaData metaData) {
        mGlue.startPlayback();
    }

    public void setPlaybackQuality(VideoQuality videoQuality) {
        youtubeTvView.setPlaybackQuality(videoQuality);
    }
}
