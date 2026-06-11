package com.example.numberfindinggame.helper;

import android.app.Activity;
import android.content.Context;

import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;

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
        ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();
        thietBiDangNhapRepository.voHieuHoaThietBi(
                SessionManager.getUserId(context),
                DeviceHelper.getDeviceId(context),
                task -> {

                    if (task.isSuccessful()) {
                        MessageHelper.success((Activity) context, "Đã đăng xuất thiết bị");
                    }


                }
        );

        context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        ).edit().clear().apply();
    }
}
