/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2016 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.bmartel.youtubetv.media;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.bmartel.youtubetv.R;
import fr.bmartel.youtubetv.model.VideoQuality;

/**
 * Select available video quality step fragment.
 */
public class QualityStepFragment extends GuidedStepFragment {

    private final static String QUALITY_LIST_KEY = "qualityList";

    private List<VideoQuality> mVideoQualities = new ArrayList<>();

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        GuidanceStylist.Guidance guidance = new GuidanceStylist.Guidance(getResources().getString(R.string.video_quality_title), "", "", null);
        return guidance;
    }

    @Override
    public int onProvideTheme() {
        return R.style.Theme_Example_LeanbackWizard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //mMovie = (Movie) getArguments().getSerializable("movie");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        mVideoQualities = (List<VideoQuality>) getArguments().getSerializable(QUALITY_LIST_KEY);

        for (VideoQuality quality : mVideoQualities) {
            GuidedAction action = new GuidedAction.Builder(getActivity())
                    .id(quality.getIndex())
                    .title(quality.getValue())
                    .editable(false)
                    .description("")
                    .build();
            actions.add(action);
        }
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {

    }

    public static Fragment newInstance(List<VideoQuality> availableQualities) {
        QualityStepFragment fragment = new QualityStepFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(QUALITY_LIST_KEY, (Serializable) availableQualities);
        fragment.setArguments(bundle);
        return fragment;
    }
}
