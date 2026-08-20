package com.example.numberfindinggame.session;

import android.content.Context;

public class SessionManager {

    private static final String PREF_NAME = "USER_SESSION";
    private static final String KEY_USER_ID = "USER_ID";


    public static void saveUser(
            Context context,
            String maNguoiDung
    ) {
        context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                )
                .edit()
                .putString(KEY_USER_ID, maNguoiDung)
                .apply();
    }

    public static String getUserId(Context context) {
        return context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        ).getString(KEY_USER_ID, "");
    }

    public static void logout(Context context) {
        context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        ).edit().clear().apply();
    }
}
