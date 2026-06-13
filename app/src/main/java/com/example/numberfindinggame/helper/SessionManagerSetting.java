package com.example.numberfindinggame.helper;

import android.content.Context;

public class SessionManagerSetting {
    private static final String PREF_NAME = "SETTING_SESSION";

    private static final String KEY_SETTING = "SETTING";
    private static final String KEY_EXPIRE_TIME = "EXPIRE_TIME";

    // 10 phút
    private static final long EXPIRE_DURATION = 10 * 60 * 1000;

    public static void saveSetting(
            Context context,
            String setting
    ) {

        long expireTime =
                System.currentTimeMillis()
                        + EXPIRE_DURATION;

        context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                )
                .edit()
                .putString(KEY_SETTING, setting)
                .putLong(KEY_EXPIRE_TIME, expireTime)
                .apply();
    }

    public static String getSetting(Context context) {

        long expireTime =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                ).getLong(
                        KEY_EXPIRE_TIME,
                        0
                );

        // Hết hạn
        if (System.currentTimeMillis() > expireTime) {

            clear(context);

            return null;
        }

        return context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        ).getString(
                KEY_SETTING,
                null
        );
    }

    public static boolean isExpired(Context context) {

        long expireTime =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                ).getLong(
                        KEY_EXPIRE_TIME,
                        0
                );

        return System.currentTimeMillis() > expireTime;
    }

    public static void clear(Context context) {

        context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                )
                .edit()
                .clear()
                .apply();

    }

}
