package com.example.numberfindinggame.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.numberfindinggame.R;

public class SessionManagerSound {

    private static final String PREF_NAME = "SoundSetting";

    private static final String KEY_SOUND = "sound";

    private static final String KEY_VOLUME = "volume";

    private static final String KEY_ENABLE = "enable";

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    public SessionManagerSound(Context context) {

        pref = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE);

        editor = pref.edit();

    }

    // Hiệu ứng hiện tại

    public void setCurrentSound(
            int soundResId) {

        editor.putInt(
                KEY_SOUND,
                soundResId);

        editor.apply();

    }

    public int getCurrentSound() {

        return pref.getInt(
                KEY_SOUND,
                R.raw.arcade_game_power_power_sound);

    }

    // Âm lượng

    public void setVolume(
            int volume) {

        editor.putInt(
                KEY_VOLUME,
                volume);

        editor.apply();

    }

    public int getVolume() {

        return pref.getInt(
                KEY_VOLUME,
                100);

    }

    // Bật tắt

    public void setEnable(
            boolean enable) {

        editor.putBoolean(
                KEY_ENABLE,
                enable);

        editor.apply();

    }

    public boolean isEnable() {

        return pref.getBoolean(
                KEY_ENABLE,
                true);

    }

}