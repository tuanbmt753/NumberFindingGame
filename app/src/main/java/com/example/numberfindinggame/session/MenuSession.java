package com.example.numberfindinggame.session;

import android.content.Context;
import android.content.SharedPreferences;

public class MenuSession {

    private static final String PREF_NAME = "MenuSession";

    private static final String KEY_MENU_MO = "menu_mo";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public MenuSession(Context context) {
        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );

        editor = preferences.edit();
    }

    // Lưu trạng thái menu
    public void setMenuMo(boolean isMo) {
        editor.putBoolean(KEY_MENU_MO, isMo);
        editor.apply();
    }

    // Lấy trạng thái menu
    public boolean isMenuMo() {
        return preferences.getBoolean(KEY_MENU_MO, true);
    }
}