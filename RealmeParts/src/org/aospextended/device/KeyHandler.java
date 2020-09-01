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

package org.aospextended.device;

import android.database.ContentObserver;
import android.content.BroadcastReceiver;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.util.Slog;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;
import android.service.notification.ZenModeConfig;
import org.aospextended.device.gestures.TouchGestures;
import android.os.UserHandle;
import com.android.internal.os.DeviceKeyHandler;
import com.android.internal.util.ArrayUtils;
import org.aospextended.device.util.Action;
import org.aospextended.device.util.Action;
import org.aospextended.device.util.Utils;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

public class KeyHandler implements DeviceKeyHandler {

    private static final String TAG = Utils.TAG;
    private static final boolean DEBUG = Utils.DEBUG;

    private static final int GESTURE_REQUEST = 1;
    private static final int GESTURE_WAKELOCK_DURATION = 2000;

    private static final int GESTURE_DOUBLE_TAP_SCANCODE = 248;
    private static final int GESTURE_W_SCANCODE = 246;
    private static final int GESTURE_M_SCANCODE = 247;
    private static final int GESTURE_CIRCLE_SCANCODE = 249;
    private static final int GESTURE_TWO_SWIPE_SCANCODE = 250;
    private static final int GESTURE_UP_ARROW_SCANCODE = 252;
    private static final int GESTURE_DOWN_ARROW_SCANCODE = 251;
    private static final int GESTURE_LEFT_ARROW_SCANCODE = 254;
    private static final int GESTURE_RIGHT_ARROW_SCANCODE = 253;
    private static final int GESTURE_SWIPE_UP_SCANCODE = 256;
    private static final int GESTURE_SWIPE_DOWN_SCANCODE = 255;
    private static final int GESTURE_SWIPE_LEFT_SCANCODE = 258;
    private static final int GESTURE_SWIPE_RIGHT_SCANCODE = 257;

    private final Context mContext;
    private Context mAppContext = null;

    private EventHandler mEventHandler;

    private Vibrator mVibrator;

    public KeyHandler(Context context) {
        mContext = context;
        mEventHandler = new EventHandler();

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        try {
            mAppContext = mContext.createPackageContext(
                    "org.aospextended.device", Context.CONTEXT_IGNORE_SECURITY);
        } catch (NameNotFoundException e) {
        }
    }

    private class EventHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            KeyEvent event = (KeyEvent) msg.obj;
            String action = null;
            SharedPreferences mPref = Utils.getSharedPreferences(mAppContext);
            switch(event.getScanCode()) {
            case GESTURE_DOUBLE_TAP_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_DOUBLE_TAP,
                        Action.ACTION_WAKE_DEVICE);
                        doHapticFeedback();
                break;
            case GESTURE_W_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_W,
                        Action.ACTION_CAMERA);
                        doHapticFeedback();
                break;
            case GESTURE_M_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_M,
                        Action.ACTION_MEDIA_PLAY_PAUSE);
                        doHapticFeedback();
                break;
            case GESTURE_CIRCLE_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_CIRCLE,
                        Action.ACTION_TORCH);
                        doHapticFeedback();
                break;
            case GESTURE_TWO_SWIPE_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_TWO_SWIPE,
                        Action.ACTION_MEDIA_PREVIOUS);
                        doHapticFeedback();
                break;
            case GESTURE_UP_ARROW_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_UP_ARROW,
                        Action.ACTION_WAKE_DEVICE);
                        doHapticFeedback();
                break;
            case GESTURE_DOWN_ARROW_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_DOWN_ARROW,
                        Action.ACTION_VIB_SILENT);
                        doHapticFeedback();
                break;
            case GESTURE_LEFT_ARROW_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_LEFT_ARROW,
                        Action.ACTION_MEDIA_PREVIOUS);
                        doHapticFeedback();
                break;
            case GESTURE_RIGHT_ARROW_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_RIGHT_ARROW,
                        Action.ACTION_MEDIA_NEXT);
                        doHapticFeedback();
                break;
            case GESTURE_SWIPE_UP_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_SWIPE_UP,
                        Action.ACTION_WAKE_DEVICE);
                        doHapticFeedback();
                break;
            case GESTURE_SWIPE_DOWN_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_SWIPE_DOWN,
                        Action.ACTION_VIB_SILENT);
                        doHapticFeedback();
                break;
            case GESTURE_SWIPE_LEFT_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_SWIPE_LEFT,
                        Action.ACTION_MEDIA_PREVIOUS);
                        doHapticFeedback();
                break;
            case GESTURE_SWIPE_RIGHT_SCANCODE:
                action = mPref.getString(TouchGestures.PREF_GESTURE_SWIPE_RIGHT,
                        Action.ACTION_MEDIA_NEXT);
                        doHapticFeedback();
                break;
            }

            if (action == null || action.equals(Action.ACTION_NULL)) return;

            if (DEBUG) Slog.d(TAG, "scancode: " + event.getScanCode() + "action: " + action);

            if (action.equals(Action.ACTION_CAMERA)) {
                Action.processAction(mContext, Action.ACTION_WAKE_DEVICE, false);
            }
            Action.processAction(mContext, action, false);
        }
    }

    private void doHapticFeedback() {
        boolean enabled = Utils.getInt(mAppContext, Utils.TOUCHSCREEN_GESTURE_HAPTIC_FEEDBACK, 1) != 0;
        if (enabled && mVibrator != null && mVibrator.hasVibrator()) {
            mVibrator.vibrate(50);
        }
    }

    public KeyEvent handleKeyEvent(KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return event;
        }
        int scanCode = event.getScanCode();
        if (!mEventHandler.hasMessages(GESTURE_REQUEST)) {
            Message msg = getMessageForKeyEvent(event);
            mEventHandler.sendMessage(msg);
        }

        return event;
    }

    private Message getMessageForKeyEvent(KeyEvent keyEvent) {
        Message msg = mEventHandler.obtainMessage(GESTURE_REQUEST);
        msg.obj = keyEvent;
        return msg;
    }
}
