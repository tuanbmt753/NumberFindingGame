package com.example.numberfindinggame.session;

import android.content.Context;
import android.content.SharedPreferences;

public class HieuUngSession {

    private static final String PREF_NAME = "HieuUngSession";
    private static final String KEY_HIEU_UNG = "hieuUng";

    private final SharedPreferences sharedPreferences;

    public HieuUngSession(Context context) {
        sharedPreferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    // Kiểm tra đã có hiệu ứng được lưu chưa
    public boolean isHieuUngExists() {
        return sharedPreferences.contains(KEY_HIEU_UNG);
    }

    // Lưu hiệu ứng
    public void setHieuUng(int hieuUng) {
        sharedPreferences.edit()
                .putInt(KEY_HIEU_UNG, hieuUng)
                .apply();
    }

    // Lấy hiệu ứng
    public int getHieuUng() {
        return sharedPreferences.getInt(KEY_HIEU_UNG, 4);
    }
}