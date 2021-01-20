package com.clj.blesample.sessionmanager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtil {

    //Shared Preference Name For Whole App
    private static final String SHARED_PREF_NAME = "scsk";
    public static final String KNOB_ANGLE = "knob_angle";
    public static final String NO_VALUE = "no_value";
    public static final String RECEIVED_STATUS = "received_status";
    public static final String BLE_MAC_ADDRESS = "ble_mac";
    public static final String USER_ID = "user_id";

    public static final String BURNER = "burner";
    public static final String TIMER_IN_MINUTE = "timer_in_minute";
    public static final String WHISTLE_IN_COUNT = "whistle_in_count";
    public static final String FLAME_MODE = "flame_mode";

    public static final String USER_NAME="user_name";


    //BLE Device Details
    public static final String BLE_NAME="ble_name";
    public static final String BLE_MAC="ble_mac";
    public static final String BLE_RSSI="ble_rssi";

    public static void setValueString(Context context, String key, String value) {

        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getValueString(Context context, String key) {

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE);
        return preferences.getString(key, NO_VALUE);


    }


    public static void setValueSInt(Context context, String key, int value) {

        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public static int getValueInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Activity.MODE_PRIVATE);
        return preferences.getInt(key, 0);

    }

    public static void remove(Context context, String key) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();


    }

    public static void clearAll(Context context) {

        SharedPreferences removeRewardID = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = removeRewardID.edit();
        editor.clear();
        editor.commit();


    }
}
