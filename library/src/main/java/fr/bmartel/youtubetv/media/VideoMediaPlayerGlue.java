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
 *
 */

package fr.bmartel.youtubetv.media;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.app.PlaybackOverlayFragment;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;

import java.util.ArrayList;

import fr.bmartel.youtubetv.IYoutubeApi;
import fr.bmartel.youtubetv.listener.IVideoActivity;
import fr.bmartel.youtubetv.listener.IVideoInfoListener;
import fr.bmartel.youtubetv.model.VideoInfo;
import fr.bmartel.youtubetv.model.VideoQuality;

public abstract class VideoMediaPlayerGlue extends MediaPlayerGlue implements IVideoInfoListener {

    //private final PlaybackControlsRow.ClosedCaptioningAction mClosedCaptioningAction;

    private final PlaybackControlsRow.HighQualityAction mHighQualityAction;

    private IVideoActivity mVideoActivity;

    private IYoutubeApi mYoutubePlayer;

    private VideoInfo mVideoInfo = new VideoInfo("", "", "", new ArrayList<VideoQuality>());

    public VideoMediaPlayerGlue(Context context,
                                PlaybackOverlayFragment fragment,
                                IYoutubeApi youtubePlayer) {
        super(context, fragment, youtubePlayer);
        setVideoInfoListener(this);
        this.mYoutubePlayer = youtubePlayer;

        /*
        JSONArray jsonData = null;
        try {
            jsonData = new JSONArray("");

            JSONObject jsonResponse = jsonData.getJSONObject(0);
            JSONArray jsonArray = jsonResponse.getJSONArray("trends");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        // Instantiate secondary actions
        //mClosedCaptioningAction = new PlaybackControlsRow.ClosedCaptioningAction(context);
        mHighQualityAction = new PlaybackControlsRow.HighQualityAction(context);
        mHighQualityAction.setLabel1("Quality");

        setFadingEnabled(true);
    }

    @Override
    protected void addSecondaryActions(ArrayObjectAdapter secondaryActionsAdapter) {
        //secondaryActionsAdapter.add(mClosedCaptioningAction);
        //secondaryActionsAdapter.add(mHighQualityAction);
        /*
        secondaryActionsAdapter.add(mThumbsDownAction);
        secondaryActionsAdapter.add(mThumbsUpAction);
        */
    }

    @Override
    public void onActionClicked(Action action) {
        super.onActionClicked(action);
        if (action == mHighQualityAction) {
            //mHighQualityAction.nextIndex();
            //mYoutubePlayer.pause();
            //mVideoActivity.displayQualityFragment(mVideoInfo.getAvailableQualityList());
        }
    }

    public void setupControlsRowPresenter(PlaybackControlsRowPresenter presenter) {
        // TODO: hahnr@ move into resources
        presenter.setProgressColor(Color.parseColor("#e3e3e3"));
        presenter.setBackgroundColor(Color.parseColor("#e52d27"));
    }

    @Override
    public void onQualityReceived(VideoInfo videoInfo) {
        mVideoInfo = videoInfo;
    }
}
