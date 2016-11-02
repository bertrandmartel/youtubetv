package fr.bmartel.youtubetv.media;

import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.bmartel.youtubetv.R;

/**
 * A base class which provides all it's implementations with a method #getWizardActivity(). It also
 * makes sure that the wizard is using the correct theme.
 */
public abstract class WizardExampleBaseStepFragment extends GuidedStepFragment {

    @Override
    public int onProvideTheme() {
        return R.style.Theme_Example_LeanbackWizard;
    }

    VideoExampleActivity getWizardActivity() {
        if (!(getActivity() instanceof VideoExampleActivity)) {
            throw new IllegalStateException(VideoExampleActivity.class.getName() + " expected.");
        }
        return (VideoExampleActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //mMovie = (Movie) getArguments().getSerializable("movie");
        super.onCreate(savedInstanceState);
    }


}
