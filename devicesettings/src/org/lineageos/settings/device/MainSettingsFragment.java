/*
 * Copyright (C) 2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.device;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;

import org.lineageos.settings.device.R;
import org.lineageos.settings.device.utils.DisplayUtils;

public class MainSettingsFragment extends PreferenceFragment {

    private Preference mPrefRefreshRateInfo;
    private ListPreference mPrefRefreshRateConfig;
    private final Preference.OnPreferenceChangeListener PrefListener = (preference, value) -> {
        final String key = preference.getKey();

        switch (key) {
            case Constants.KEY_REFRESH_RATE_CONFIG:
                setHzConfig();
                break;
            case Constants.KEY_DC_DIMMING:
                DisplayUtils.setDcDimmingStatus((boolean) value);
                break;
        }

        return true;
    };
    private SwitchPreference mPrefDcDimming;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_settings);
        mPrefRefreshRateConfig = findPreference(Constants.KEY_REFRESH_RATE_CONFIG);
        mPrefRefreshRateConfig.setOnPreferenceChangeListener(PrefListener);
        mPrefRefreshRateInfo = findPreference(Constants.KEY_REFRESH_RATE_INFO);
        mPrefDcDimming = findPreference(Constants.KEY_DC_DIMMING);
        mPrefDcDimming.setOnPreferenceChangeListener(PrefListener);
        updateSummary();
    }

    private float getCurrentMinHz() {
        return Settings.System.getFloat(getContext().getContentResolver(),
                Settings.System.MIN_REFRESH_RATE, Constants.DEFAULT_REFRESH_RATE);
    }

    private float getCurrentMaxHz() {
        return Settings.System.getFloat(getContext().getContentResolver(),
                Settings.System.PEAK_REFRESH_RATE, Constants.DEFAULT_REFRESH_RATE);
    }

    private void setHzConfig() {
        DisplayUtils.updateRefreshRateSettings(getContext());
        updateSummary();
    }

    private void updateSummary() {
        if (mPrefRefreshRateConfig.getEntry() == null) {
            mPrefRefreshRateConfig.setValueIndex(2);
        }
        Handler.getMain().post(() -> {
            mPrefRefreshRateInfo.setSummary(
                    String.format(getString(R.string.current_refresh_rate_info),
                            Math.round(getCurrentMaxHz()), Math.round(getCurrentMinHz())));
        });
    }
}
