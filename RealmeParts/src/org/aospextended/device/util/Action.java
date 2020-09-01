/*
* Copyright (C) 2020 The AospExtended Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.aospextended.device.util;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraAccessException;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.media.session.MediaSessionLegacyHelper;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.Settings;
import android.provider.MediaStore;
import android.util.Slog;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;

import java.net.URISyntaxException;

public class Action {
    public static final String TAG = Utils.TAG;

    public static final String ACTION_HOME                 = "home";
    public static final String ACTION_BACK                 = "back";
    public static final String ACTION_SEARCH               = "search";
    public static final String ACTION_VOICE_SEARCH         = "voice_search";
    public static final String ACTION_MENU                 = "menu";
    public static final String ACTION_MENU_BIG             = "menu_big";
    public static final String ACTION_POWER                = "power";
    public static final String ACTION_NOTIFICATIONS        = "notifications";
    public static final String ACTION_RECENTS              = "recents";
    public static final String ACTION_SCREENSHOT           = "screenshot";
    public static final String ACTION_IME                  = "ime";
    public static final String ACTION_LAST_APP             = "lastapp";
    public static final String ACTION_KILL                 = "kill";
    public static final String ACTION_ASSIST               = "assist";
    public static final String ACTION_VIB                  = "ring_vib";
    public static final String ACTION_SILENT               = "ring_silent";
    public static final String ACTION_VIB_SILENT           = "ring_vib_silent";
    public static final String ACTION_POWER_MENU           = "power_menu";
    public static final String ACTION_TORCH                = "torch";
    public static final String ACTION_EXPANDED_DESKTOP     = "expanded_desktop";
    public static final String ACTION_THEME_SWITCH         = "theme_switch";
    public static final String ACTION_KEYGUARD_SEARCH      = "keyguard_search";
    public static final String ACTION_PIE                  = "pie";
    public static final String ACTION_NAVBAR               = "nav_bar";
    public static final String ACTION_IME_NAVIGATION_LEFT  = "ime_nav_left";
    public static final String ACTION_IME_NAVIGATION_RIGHT = "ime_nav_right";
    public static final String ACTION_IME_NAVIGATION_UP    = "ime_nav_up";
    public static final String ACTION_IME_NAVIGATION_DOWN  = "ime_nav_down";
    public static final String ACTION_CAMERA               = "camera";
    public static final String ACTION_MEDIA_PREVIOUS       = "media_previous";
    public static final String ACTION_MEDIA_NEXT           = "media_next";
    public static final String ACTION_MEDIA_PLAY_PAUSE     = "media_play_pause";
    public static final String ACTION_WAKE_DEVICE          = "wake_device";
    public static final String ACTION_NULL            = "null";
    public static final String ACTION_APP          = "app";
    public static final String ICON_EMPTY = "empty";
    public static final String SYSTEM_ICON_IDENTIFIER = "system_shortcut=";
    public static final String ACTION_DELIMITER = "|";
    private static boolean sTorchEnabled = false;

    public static void processAction(Context context, String action, boolean isLongpress) {
            if (action == null || action.equals(ACTION_NULL)) {
                Slog.w(TAG, "action is null");
                return;
            }

            boolean isKeyguardShowing = false;
            try {
                isKeyguardShowing =
                        WindowManagerGlobal.getWindowManagerService().isKeyguardLocked();
            } catch (RemoteException e) {
                Slog.w(TAG, "Error getting window manager service", e);
            }

            // process the actions
            if (action.equals(ACTION_HOME)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_HOME, isLongpress);
                return;
            } else if (action.equals(ACTION_BACK)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_BACK, isLongpress);
                return;
            } else if (action.equals(ACTION_SEARCH)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_SEARCH, isLongpress);
                return;
            } else if (action.equals(ACTION_MENU)
                    || action.equals(ACTION_MENU_BIG)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_MENU, isLongpress);
                return;
            } else if (action.equals(ACTION_IME_NAVIGATION_LEFT)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_DPAD_LEFT, isLongpress);
                return;
            } else if (action.equals(ACTION_IME_NAVIGATION_RIGHT)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_DPAD_RIGHT, isLongpress);
                return;
            } else if (action.equals(ACTION_IME_NAVIGATION_UP)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_DPAD_UP, isLongpress);
                return;
            } else if (action.equals(ACTION_IME_NAVIGATION_DOWN)) {
                triggerVirtualKeypress(KeyEvent.KEYCODE_DPAD_DOWN, isLongpress);
                return;
            } else if (action.equals(ACTION_TORCH)) {
                try {
                    CameraManager cameraManager = (CameraManager)
                            context.getSystemService(Context.CAMERA_SERVICE);
                    for (final String cameraId : cameraManager.getCameraIdList()) {
                        CameraCharacteristics characteristics =
                            cameraManager.getCameraCharacteristics(cameraId);
                        Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                        int orient = characteristics.get(CameraCharacteristics.LENS_FACING);
                        if (flashAvailable != null && flashAvailable && orient == CameraCharacteristics.LENS_FACING_BACK) {
                            cameraManager.setTorchMode(cameraId, !sTorchEnabled);
                            sTorchEnabled = !sTorchEnabled;
                            break;
                        }
                    }
                } catch (CameraAccessException e) {
                }
                return;
            } else if (action.equals(ACTION_POWER)) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                pm.goToSleep(SystemClock.uptimeMillis());
                return;
            } else if (action.equals(ACTION_IME)) {
                if (isKeyguardShowing) {
                    return;
                }
                context.sendBroadcastAsUser(
                        new Intent("android.settings.SHOW_INPUT_METHOD_PICKER"),
                        new UserHandle(UserHandle.USER_CURRENT));
                return;
            } else if (action.equals(ACTION_VOICE_SEARCH)) {
                // launch the search activity
                Intent intent = new Intent(Intent.ACTION_SEARCH_LONG_PRESS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    // TODO: This only stops the factory-installed search manager.
                    // Need to formalize an API to handle others
                    SearchManager searchManager =
                            (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
                    if (searchManager != null) {
                        searchManager.stopSearch();
                    }
                    startActivity(context, intent);
                } catch (ActivityNotFoundException e) {
                    Slog.e(TAG, "No activity to handle assist long press action.", e);
                }
                return;
            } else if (action.equals(ACTION_VIB)) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if(am != null && ActivityManagerNative.isSystemReady()) {
                    if(am.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if(vib != null){
                            vib.vibrate(50);
                        }
                    }else{
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        ToneGenerator tg = new ToneGenerator(
                                AudioManager.STREAM_NOTIFICATION,
                                (int)(ToneGenerator.MAX_VOLUME * 0.85));
                        if(tg != null){
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                }
                return;
            } else if (action.equals(ACTION_SILENT)) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (am != null && ActivityManagerNative.isSystemReady()) {
                    if (am.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    } else {
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        ToneGenerator tg = new ToneGenerator(
                                AudioManager.STREAM_NOTIFICATION,
                                (int)(ToneGenerator.MAX_VOLUME * 0.85));
                        if (tg != null) {
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                }
                return;
            } else if (action.equals(ACTION_VIB_SILENT)) {
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (am != null && ActivityManagerNative.isSystemReady()) {
                    if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        if (vib != null) {
                            vib.vibrate(50);
                        }
                    } else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    } else {
                        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        ToneGenerator tg = new ToneGenerator(
                                AudioManager.STREAM_NOTIFICATION,
                                (int)(ToneGenerator.MAX_VOLUME * 0.85));
                        if (tg != null) {
                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                }
                return;
            } else if (action.equals(ACTION_CAMERA)) {
                // ToDo: Send for secure keyguard secure camera intent.
                // We need to add support for it first.
                Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, null);
                startActivity(context, intent);
                return;
            } else if (action.equals(ACTION_MEDIA_PREVIOUS)) {
                dispatchMediaKeyWithWakeLock(KeyEvent.KEYCODE_MEDIA_PREVIOUS, context);
                return;
            } else if (action.equals(ACTION_MEDIA_NEXT)) {
                dispatchMediaKeyWithWakeLock(KeyEvent.KEYCODE_MEDIA_NEXT, context);
                return;
            } else if (action.equals(ACTION_MEDIA_PLAY_PAUSE)) {
                dispatchMediaKeyWithWakeLock(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, context);
                return;
            } else if (action.equals(ACTION_WAKE_DEVICE)) {
                PowerManager powerManager =
                        (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                if (!powerManager.isScreenOn()) {
                    powerManager.wakeUp(SystemClock.uptimeMillis());
                }
                return;
            } else {
                // we must have a custom uri
                Intent intent = null;
                try {
                    intent = Intent.parseUri(action, 0);
                } catch (URISyntaxException e) {
                    Slog.e(TAG, "URISyntaxException: [" + action + "]");
                    return;
                }
                startActivity(context, intent);
                return;
            }

    }

    public static boolean isActionKeyEvent(String action) {
        if (action.equals(ACTION_HOME)
                || action.equals(ACTION_BACK)
                || action.equals(ACTION_SEARCH)
                || action.equals(ACTION_MENU)
                || action.equals(ACTION_MENU_BIG)
                || action.equals(ACTION_NULL)) {
            return true;
        }
        return false;
    }

    private static void startActivity(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivityAsUser(intent,
                new UserHandle(UserHandle.USER_CURRENT));
    }

    private static void dispatchMediaKeyWithWakeLock(int keycode, Context context) {
        if (ActivityManagerNative.isSystemReady()) {
            KeyEvent event = new KeyEvent(SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0);
            MediaSessionLegacyHelper.getHelper(context).sendMediaButtonEvent(event, true);
            event = KeyEvent.changeAction(event, KeyEvent.ACTION_UP);
            MediaSessionLegacyHelper.getHelper(context).sendMediaButtonEvent(event, true);
        }
    }

    public static void triggerVirtualKeypress(final int keyCode, boolean longpress) {
        InputManager im = InputManager.getInstance();
        long now = SystemClock.uptimeMillis();
        int downflags = 0;
        int upflags = 0;
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
            || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
            || keyCode == KeyEvent.KEYCODE_DPAD_UP
            || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            downflags = upflags = KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE;
        } else {
            downflags = upflags = KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY;
        }
        if (longpress) {
            downflags |= KeyEvent.FLAG_LONG_PRESS;
        }

        final KeyEvent downEvent = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
                keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                downflags,
                InputDevice.SOURCE_KEYBOARD);
        im.injectInputEvent(downEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);

        final KeyEvent upEvent = new KeyEvent(now, now, KeyEvent.ACTION_UP,
                keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                upflags,
                InputDevice.SOURCE_KEYBOARD);
        im.injectInputEvent(upEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    }

}
