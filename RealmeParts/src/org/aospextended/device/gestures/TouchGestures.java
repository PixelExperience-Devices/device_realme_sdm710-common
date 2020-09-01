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

package org.aospextended.device.gestures;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import org.aospextended.device.util.Action;
import org.aospextended.device.util.Utils;

import org.aospextended.device.R;
import org.aospextended.device.util.ShortcutPickerHelper;

public class TouchGestures extends PreferenceFragment implements
        OnPreferenceChangeListener, OnPreferenceClickListener,
        ShortcutPickerHelper.OnPickListener {

    private static String GESTURE_PATH = "/proc/touchpanel/gesture_enable";
    private static String DT2W_PATH = "/proc/touchpanel/double_tap_enable";

    private static final String SETTINGS_METADATA_NAME = "com.android.settings";

    public static final String PREF_DT2W_ENABLE = "enable_dt2w";
    public static final String PREF_GESTURE_ENABLE = "enable_gestures";

    public static final String PREF_GESTURE_DOUBLE_TAP = "gesture_double_tap";
    public static final String PREF_GESTURE_W = "gesture_w";
    public static final String PREF_GESTURE_M = "gesture_m";
    public static final String PREF_GESTURE_CIRCLE = "gesture_circle";
    public static final String PREF_GESTURE_TWO_SWIPE = "gesture_two_swipe";
    public static final String PREF_GESTURE_UP_ARROW = "gesture_up_arrow";
    public static final String PREF_GESTURE_DOWN_ARROW = "gesture_down_arrow";
    public static final String PREF_GESTURE_LEFT_ARROW = "gesture_left_arrow";
    public static final String PREF_GESTURE_RIGHT_ARROW = "gesture_right_arrow";
    public static final String PREF_GESTURE_SWIPE_UP = "gesture_swipe_up";
    public static final String PREF_GESTURE_SWIPE_DOWN = "gesture_swipe_down";
    public static final String PREF_GESTURE_SWIPE_LEFT = "gesture_swipe_left";
    public static final String PREF_GESTURE_SWIPE_RIGHT = "gesture_swipe_right";

    private static final String KEY_GESTURE_HAPTIC_FEEDBACK = "gesture_haptic_feedback";

    private static final int DLG_SHOW_ACTION_DIALOG  = 0;
    private static final int DLG_RESET_TO_DEFAULT    = 1;

    private static final int MENU_RESET = Menu.FIRST;

    private Preference mGestureDoubleTap;
    private Preference mGestureW;
    private Preference mGestureM;
    private Preference mGestureCircle;
    private Preference mGestureTwoSwipe;
    private Preference mGestureUpArrow;
    private Preference mGestureDownArrow;
    private Preference mGestureLeftArrow;
    private Preference mGestureRightArrow;
    private Preference mGestureSwipeUp;
    private Preference mGestureSwipeDown;
    private Preference mGestureSwipeLeft;
    private Preference mGestureSwipeRight;

    private SwitchPreference mEnableDt2w;
    private SwitchPreference mEnableGestures;
    private SwitchPreference mHapticFeedback;

    private boolean mCheckPreferences;
    private SharedPreferences mPrefs;

    private ShortcutPickerHelper mPicker;
    private String mPendingkey;

    private String[] mActionEntries;
    private String[] mActionValues;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        mPicker = new ShortcutPickerHelper(getActivity(), this);

        mPrefs = Utils.getSharedPreferences(getActivity());

        mActionValues = getResources().getStringArray(R.array.action_screen_off_values);
        mActionEntries = getResources().getStringArray(R.array.action_screen_off_entries);

        initPrefs();

        setHasOptionsMenu(true);
    }

    public static void enableGestures(boolean enable) {
            if (Utils.fileExists(GESTURE_PATH)) {
                Utils.writeLine(GESTURE_PATH, enable ? "1" : "0");
            }
    }

    public static void enableDt2w(boolean enable) {
            if (Utils.fileExists(GESTURE_PATH)) {
                Utils.writeLine(DT2W_PATH, enable ? "1" : "0");
            }
    }

    private PreferenceScreen initPrefs() {
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        addPreferencesFromResource(R.xml.screen_off_gesture);

        prefs = getPreferenceScreen();

        mEnableDt2w = (SwitchPreference) prefs.findPreference(PREF_DT2W_ENABLE);

        mEnableGestures = (SwitchPreference) prefs.findPreference(PREF_GESTURE_ENABLE);

        mHapticFeedback = (SwitchPreference) findPreference(KEY_GESTURE_HAPTIC_FEEDBACK);
        mHapticFeedback.setChecked(mPrefs.getInt(Utils.TOUCHSCREEN_GESTURE_HAPTIC_FEEDBACK, 1) != 0);
        mHapticFeedback.setOnPreferenceChangeListener(this);

        mGestureDoubleTap = (Preference) prefs.findPreference(PREF_GESTURE_DOUBLE_TAP);
        mGestureW = (Preference) prefs.findPreference(PREF_GESTURE_W);
        mGestureM = (Preference) prefs.findPreference(PREF_GESTURE_M);
        mGestureCircle = (Preference) prefs.findPreference(PREF_GESTURE_CIRCLE);
        mGestureTwoSwipe = (Preference) prefs.findPreference(PREF_GESTURE_TWO_SWIPE);
        mGestureUpArrow = (Preference) prefs.findPreference(PREF_GESTURE_UP_ARROW);
        mGestureDownArrow = (Preference) prefs.findPreference(PREF_GESTURE_DOWN_ARROW);
        mGestureLeftArrow = (Preference) prefs.findPreference(PREF_GESTURE_LEFT_ARROW);
        mGestureRightArrow = (Preference) prefs.findPreference(PREF_GESTURE_RIGHT_ARROW);
        mGestureSwipeUp = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_UP);
        mGestureSwipeDown = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_DOWN);
        mGestureSwipeLeft = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_LEFT);
        mGestureSwipeRight = (Preference) prefs.findPreference(PREF_GESTURE_SWIPE_RIGHT);

        setPref(mGestureDoubleTap, mPrefs.getString(PREF_GESTURE_DOUBLE_TAP,
                Action.ACTION_WAKE_DEVICE));
        setPref(mGestureW, mPrefs.getString(PREF_GESTURE_W,
                Action.ACTION_CAMERA));
        setPref(mGestureM, mPrefs.getString(PREF_GESTURE_M,
                Action.ACTION_MEDIA_PLAY_PAUSE));
        setPref(mGestureCircle, mPrefs.getString(PREF_GESTURE_CIRCLE,
                Action.ACTION_VIB_SILENT));
        setPref(mGestureTwoSwipe, mPrefs.getString(PREF_GESTURE_TWO_SWIPE,
                Action.ACTION_MEDIA_PREVIOUS));
        setPref(mGestureUpArrow, mPrefs.getString(PREF_GESTURE_UP_ARROW,
                Action.ACTION_MEDIA_NEXT));
        setPref(mGestureDownArrow, mPrefs.getString(PREF_GESTURE_DOWN_ARROW,
                Action.ACTION_MEDIA_NEXT));
        setPref(mGestureLeftArrow, mPrefs.getString(PREF_GESTURE_LEFT_ARROW,
                Action.ACTION_MEDIA_NEXT));
        setPref(mGestureRightArrow, mPrefs.getString(PREF_GESTURE_RIGHT_ARROW,
                Action.ACTION_MEDIA_NEXT));
        setPref(mGestureSwipeUp, mPrefs.getString(PREF_GESTURE_SWIPE_UP,
                Action.ACTION_WAKE_DEVICE));
        setPref(mGestureSwipeDown, mPrefs.getString(PREF_GESTURE_SWIPE_DOWN,
                Action.ACTION_VIB_SILENT));
        setPref(mGestureSwipeLeft, mPrefs.getString(PREF_GESTURE_SWIPE_LEFT,
                Action.ACTION_MEDIA_PREVIOUS));
        setPref(mGestureSwipeRight, mPrefs.getString(PREF_GESTURE_SWIPE_RIGHT,
                Action.ACTION_MEDIA_NEXT));

        boolean enableDt2w =
                mPrefs.getBoolean(PREF_DT2W_ENABLE, true);
        mEnableDt2w.setChecked(enableDt2w);
        mEnableDt2w.setOnPreferenceChangeListener(this);

        boolean enableGestures =
                mPrefs.getBoolean(PREF_GESTURE_ENABLE, true);
        mEnableGestures.setChecked(enableGestures);
        mEnableGestures.setOnPreferenceChangeListener(this);

        return prefs;
    }

    private void setPref(Preference preference, String action) {
        if (preference == null || action == null) {
            return;
        }
        preference.setSummary(getDescription(action));
        preference.setOnPreferenceClickListener(this);
    }

    private String getDescription(String action) {
        if (action == null) {
            return null;
        }
        int i = 0;
        for (String val : mActionValues) {
            if (action.equals(val)) {
                return mActionEntries[i];
            }
            i++;
        }
        return null;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = null;
        int title = 0;
	    if (preference == mGestureDoubleTap) {
            key = PREF_GESTURE_DOUBLE_TAP;
            title = R.string.gesture_double_tap_title;
        } else if (preference == mGestureW) {
            key = PREF_GESTURE_W;
            title = R.string.gesture_w_title;
        } else if (preference == mGestureM) {
            key = PREF_GESTURE_M;
            title = R.string.gesture_m_title;
        } else if (preference == mGestureCircle) {
            key = PREF_GESTURE_CIRCLE;
            title = R.string.gesture_circle_title;
        } else if (preference == mGestureTwoSwipe) {
            key = PREF_GESTURE_TWO_SWIPE;
            title = R.string.gesture_two_swipe_title;
        } else if (preference == mGestureUpArrow) {
            key = PREF_GESTURE_UP_ARROW;
            title = R.string.gesture_up_arrow_title;
        } else if (preference == mGestureDownArrow) {
            key = PREF_GESTURE_DOWN_ARROW;
            title = R.string.gesture_down_arrow_title;
        } else if (preference == mGestureLeftArrow) {
            key = PREF_GESTURE_LEFT_ARROW;
            title = R.string.gesture_left_arrow_title;
        } else if (preference == mGestureRightArrow) {
            key = PREF_GESTURE_RIGHT_ARROW;
            title = R.string.gesture_right_arrow_title;
        } else if (preference == mGestureSwipeUp) {
            key = PREF_GESTURE_SWIPE_UP;
            title = R.string.gesture_swipe_up_title;
        } else if (preference == mGestureSwipeDown) {
            key = PREF_GESTURE_SWIPE_DOWN;
            title = R.string.gesture_swipe_down_title;
        } else if (preference == mGestureSwipeLeft) {
            key = PREF_GESTURE_SWIPE_LEFT;
            title = R.string.gesture_swipe_left_title;
        } else if (preference == mGestureSwipeRight) {
            key = PREF_GESTURE_SWIPE_RIGHT;
            title = R.string.gesture_swipe_right_title;
        }
        if (key != null) {
            showDialogInner(DLG_SHOW_ACTION_DIALOG, key, title);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mEnableDt2w) {
            mPrefs.edit()
                    .putBoolean(PREF_DT2W_ENABLE, (Boolean) newValue).commit();
            enableDt2w((Boolean) newValue);
            return true;
        }
        if (preference == mEnableGestures) {
            mPrefs.edit()
                    .putBoolean(PREF_GESTURE_ENABLE, (Boolean) newValue).commit();
            enableGestures((Boolean) newValue);
            return true;
        }
        final String key = preference.getKey();
        if (KEY_GESTURE_HAPTIC_FEEDBACK.equals(key)) {
                final boolean value = (boolean) newValue;
                mPrefs.edit().putInt(Utils.TOUCHSCREEN_GESTURE_HAPTIC_FEEDBACK, value ? 1 : 0).commit();
                return true;
        }
        return false;
    }

    // Reset all entries to default.
    private void resetToDefault() {
        SharedPreferences.Editor editor = mPrefs.edit();

        mPrefs.edit()
                .putBoolean(PREF_DT2W_ENABLE, true).commit();
        mPrefs.edit()
                .putBoolean(PREF_GESTURE_ENABLE, true).commit();

        editor.putString(PREF_GESTURE_DOUBLE_TAP,
                Action.ACTION_WAKE_DEVICE).commit();
        editor.putString(PREF_GESTURE_W,
                Action.ACTION_CAMERA).commit();
        editor.putString(PREF_GESTURE_M,
                Action.ACTION_MEDIA_PLAY_PAUSE).commit();
        editor.putString(PREF_GESTURE_CIRCLE,
                Action.ACTION_VIB_SILENT).commit();
        editor.putString(PREF_GESTURE_TWO_SWIPE,
                Action.ACTION_MEDIA_PREVIOUS).commit();
        editor.putString(PREF_GESTURE_UP_ARROW,
                Action.ACTION_WAKE_DEVICE).commit();
        editor.putString(PREF_GESTURE_DOWN_ARROW,
                Action.ACTION_VIB_SILENT).commit();
        editor.putString(PREF_GESTURE_LEFT_ARROW,
                Action.ACTION_MEDIA_PREVIOUS).commit();
        editor.putString(PREF_GESTURE_RIGHT_ARROW,
                Action.ACTION_MEDIA_NEXT).commit();
		editor.putString(PREF_GESTURE_SWIPE_UP,
                Action.ACTION_WAKE_DEVICE).commit();
        editor.putString(PREF_GESTURE_SWIPE_DOWN,
                Action.ACTION_VIB_SILENT).commit();
        editor.putString(PREF_GESTURE_SWIPE_LEFT,
                Action.ACTION_MEDIA_PREVIOUS).commit();
        editor.putString(PREF_GESTURE_SWIPE_RIGHT,
                Action.ACTION_MEDIA_NEXT).commit();
        mHapticFeedback.setChecked(true);
        editor.commit();
        enableDt2w(true);
        enableGestures(true);
        initPrefs();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void shortcutPicked(String action,
                String description, Bitmap bmp, boolean isApplication) {
        if (mPendingkey == null || action == null) {
            return;
        }
        mPrefs.edit().putString(mPendingkey, action).commit();
        initPrefs();
        mPendingkey = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ShortcutPickerHelper.REQUEST_PICK_SHORTCUT
                    || requestCode == ShortcutPickerHelper.REQUEST_PICK_APPLICATION
                    || requestCode == ShortcutPickerHelper.REQUEST_CREATE_SHORTCUT) {
                mPicker.onActivityResult(requestCode, resultCode, data);

            }
        } else {
            mPendingkey = null;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                    showDialogInner(DLG_RESET_TO_DEFAULT, null, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_settings_reset)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void showDialogInner(int id, String key, int title) {
        DialogFragment newFragment =
                MyAlertDialogFragment.newInstance(id, key, title);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(
                int id, String key, int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            args.putString("key", key);
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        TouchGestures getOwner() {
            return (TouchGestures) getTargetFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            final String key = getArguments().getString("key");
            int title = getArguments().getInt("title");
            switch (id) {
                case DLG_SHOW_ACTION_DIALOG:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setNegativeButton(R.string.cancel, null)
                    .setItems(getOwner().mActionEntries,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            if (getOwner().mActionValues[item]
                                    .equals(Action.ACTION_APP)) {
                                if (getOwner().mPicker != null) {
                                    getOwner().mPendingkey = key;
                                    getOwner().mPicker.pickShortcut(getOwner().getId());
                                }
                            } else {
                                getOwner().mPrefs.edit()
                                        .putString(key,
                                        getOwner().mActionValues[item]).commit();
                                getOwner().initPrefs();
                            }
                        }
                    })
                    .create();
                case DLG_RESET_TO_DEFAULT:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.reset)
                    .setMessage(R.string.reset_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getOwner().resetToDefault();
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
        }
    }

}
