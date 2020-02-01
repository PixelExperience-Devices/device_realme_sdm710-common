/*
 * Copyright (C) 2020 The AospExtended Project
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

package org.aospextended.device.vibration;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import android.database.ContentObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;
import android.os.Vibrator;
import org.aospextended.device.util.Utils;
import org.aospextended.device.R;

public class VibratorStrengthPreference extends Preference implements
        SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
    private int mOldStrength;
    private int mMinValue;
    private int mMaxValue;
    private Vibrator mVibrator;

    private static final String FILE_LEVEL = "/sys/class/leds/vibrator/vmax_mv";
    private static final long testVibrationPattern[] = {0,250};

    public static final String KEY_VIBSTRENGTH = "vib_strength";

    public VibratorStrengthPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // from drivers/platform/msm/qpnp-haptic.c
        // #define QPNP_HAP_VMAX_MIN_MV		116
        // #define QPNP_HAP_VMAX_MAX_MV		3596
        mMinValue = 116;
        mMaxValue = 3596;

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        setLayoutResource(R.layout.preference_seek_bar);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mOldStrength = Integer.parseInt(getValue(getContext()));
        mSeekBar = (SeekBar) holder.findViewById(R.id.seekbar);
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setProgress(mOldStrength - mMinValue);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public static boolean isSupported() {
        return Utils.fileWritable(FILE_LEVEL);
    }

    public static String getValue(Context context) {
	return Utils.getFileValue(FILE_LEVEL, "3596");
    }

    private void setValue(String newValue, boolean withFeedback) {
        Utils.writeValue(FILE_LEVEL, newValue);
        SharedPreferences.Editor editor = Utils.getSharedPreferences(getContext()).edit();
        editor.putString(KEY_VIBSTRENGTH, newValue);
        editor.commit();
        if (withFeedback) {
            mVibrator.vibrate(testVibrationPattern, -1);
        }
    }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        String storedValue = Utils.getSharedPreferences(context).getString(KEY_VIBSTRENGTH, "2700"); 
        Utils.writeValue(FILE_LEVEL, storedValue);
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromTouch) {
        setValue(String.valueOf(progress + mMinValue), true);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // NA
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // NA
    }
}

