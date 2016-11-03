/*
 * Copyright (C) 2014 The Android Open Source Project
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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.List;

import fr.bmartel.youtubetv.R;
import fr.bmartel.youtubetv.listener.IVideoActivity;
import fr.bmartel.youtubetv.model.VideoQuality;

/**
 * TODO: Javadoc
 */
public class VideoExampleActivity extends Activity implements IVideoActivity {

    public static final String TAG = "VideoExampleActivity";

    private VideoConsumptionExampleFragment videoFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_example);

        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.replace(R.id.videoFragment, new VideoSurfaceFragment(), VideoSurfaceFragment.TAG);
        ft1.commit();

        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
        videoFragment = new VideoConsumptionExampleFragment();
        ft2.add(R.id.videoFragment, videoFragment, VideoConsumptionExampleFragment.TAG);
        ft2.commit();

        requestVisibleBehind(true);
    }

    @Override
    public void displayQualityFragment(List<VideoQuality> availableQualities) {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        //VideoQualityExport qualityExport = new VideoQualityExport(availableQualities);
        Fragment fragment = QualityStepFragment.newInstance(availableQualities);
        ft3.add(android.R.id.content, fragment, QualityStepFragment.TAG).addToBackStack(null);
        ft3.commit();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    public void setVideoQuality(long index) {
        videoFragment.setPlaybackQuality(VideoQuality.getVideoQuality((int) index));
    }


    @Override
    protected void onResume() {
        super.onResume();
        requestVisibleBehind(true);
    }

}
