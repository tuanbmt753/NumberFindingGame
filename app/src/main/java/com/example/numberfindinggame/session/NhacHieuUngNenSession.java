package com.example.numberfindinggame.session;

import android.content.Context;
import android.content.SharedPreferences;

public class NhacHieuUngNenSession {

    private static final String PREF_NAME = "NhacHieuUngNenSession";
    private static final String KEY_NHAC_HIEU_UNG = "nhac_hieu_ung";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public NhacHieuUngNenSession(Context context) {
        sharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
    }

    // Lưu trạng thái bật/tắt nhạc hiệu ứng
    public void setNhacHieuUng(boolean trangThai) {
        editor.putBoolean(KEY_NHAC_HIEU_UNG, trangThai);
        editor.apply();
    }

    // Lấy trạng thái
    public boolean isNhacHieuUng() {
        return sharedPreferences.getBoolean(
                KEY_NHAC_HIEU_UNG,
                true // Mặc định lần đầu là bật
        );
    }
}