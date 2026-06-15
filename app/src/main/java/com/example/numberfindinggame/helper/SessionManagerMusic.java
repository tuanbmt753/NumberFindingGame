package com.example.numberfindinggame.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerMusic {

    private static final String PREF_NAME = "NumberFindingGame";

    private static final String KEY_MUSIC =
            "music";

    private static final String KEY_MUSIC_VOLUME =
            "music_volume";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManagerMusic(Context context) {

        pref = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE);

        editor = pref.edit();
    }

    public void setMusicEnabled(boolean enabled) {

        editor.putBoolean(
                KEY_MUSIC,
                enabled);

        editor.apply();
    }

    public boolean isMusicEnabled() {

        return pref.getBoolean(
                KEY_MUSIC,
                true);
    }

    public void setMusicVolume(int volume) {

        if (volume < 0)
            volume = 0;

        if (volume > 100)
            volume = 100;

        editor.putInt(
                KEY_MUSIC_VOLUME,
                volume);

        editor.apply();
    }

    public int getMusicVolume() {

        return pref.getInt(
                KEY_MUSIC_VOLUME,
                100);
    }

}