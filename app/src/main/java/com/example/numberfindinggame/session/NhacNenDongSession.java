package com.example.numberfindinggame.session;

import android.content.Context;
import android.content.SharedPreferences;

public class NhacNenDongSession {

    private static final String PREF_NAME = "NhacNenDongSession";
    private static final String KEY_AM_LUONG = "amLuong";

    private final SharedPreferences preferences;

    public NhacNenDongSession(Context context) {
        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    // Lấy âm lượng
    // Nếu chưa có thì mặc định 100
    public int getAmLuong() {
        return preferences.getInt(KEY_AM_LUONG, 100);
    }

    // Lưu âm lượng
    public void setAmLuong(int amLuong) {
        // Đảm bảo giá trị nằm trong khoảng 0 - 100
        if (amLuong < 0) {
            amLuong = 0;
        }

        if (amLuong > 100) {
            amLuong = 100;
        }

        preferences.edit()
                .putInt(KEY_AM_LUONG, amLuong)
                .apply();
    }
}