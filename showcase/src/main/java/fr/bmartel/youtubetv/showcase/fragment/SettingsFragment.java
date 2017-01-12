package fr.bmartel.youtubetv.showcase.fragment;

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

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v17.preference.LeanbackPreferenceFragment;
import android.support.v17.preference.LeanbackSettingsFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Stack;

import fr.bmartel.youtubetv.showcase.BuildConfig;
import fr.bmartel.youtubetv.showcase.R;
import fr.bmartel.youtubetv.showcase.dialog.WebDialogFragment;

public class SettingsFragment extends LeanbackSettingsFragment {

    private final Stack<Fragment> fragments = new Stack<Fragment>();

    private PreferenceFragment frag;

    private SharedPreferences mSharedPref;

    private final static String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onPreferenceStartInitialScreen() {
        frag = buildPreferenceFragment(R.xml.prefs, null);
        mSharedPref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        startPreferenceFragment(frag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Preference infoPrefs = frag.findPreference("info_version");
        infoPrefs.setSummary(BuildConfig.VERSION_NAME);

        Preference license = frag.findPreference("license");

        license.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                WebDialogFragment dialog = WebDialogFragment.newInstance("file:///android_asset/license.html", getActivity().getString(R.string.dialog_title_license));
                dialog.show(getFragmentManager(), getActivity().getString(R.string.dialog_title_license));

                return false;
            }
        });
    }

    @Override
    public boolean onPreferenceStartScreen(final PreferenceFragment preferenceFragment,
                                           final PreferenceScreen preferenceScreen) {
        final PreferenceFragment frag = buildPreferenceFragment(R.xml.prefs, preferenceScreen.getKey());
        startPreferenceFragment(frag);
        return true;
    }

    private PreferenceFragment buildPreferenceFragment(int preferenceResId, String root) {
        PreferenceFragment fragment = new PrefFragment();
        Bundle args = new Bundle();
        args.putInt("preferenceResource", preferenceResId);
        args.putString("root", root);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragment caller, Preference pref) {
        return false;
    }

    public static class PrefFragment extends LeanbackPreferenceFragment {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            String root = getArguments().getString("root", null);
            int prefResId = getArguments().getInt("preferenceResource");

            if (root == null) {
                addPreferencesFromResource(prefResId);
            } else {
                setPreferencesFromResource(prefResId, root);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            final String[] keys = {"prefs_wifi_connect_wps", "prefs_date", "prefs_time",
                    "prefs_date_time_use_timezone", "app_banner_sample_app", "pref_force_stop",
                    "pref_uninstall", "pref_more_info"};
            if (Arrays.asList(keys).contains(preference.getKey())) {
                Toast.makeText(getActivity(), "Implement your own action handler.", Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.onPreferenceTreeClick(preference);
        }

    }
}
