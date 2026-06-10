package com.example.numberfindinggame.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.firebase.database.ValueEventListener;

public class ThietBiDangNhapHelper {

    private static final ThietBiDangNhapRepository repository =
            new ThietBiDangNhapRepository();

    public static void kiemTraThietBiDangNhap(
            Activity activity,
            String maNguoiDung,
            String maThietBi
    ) {

        repository.kiemTraDangHoatDong(
                maNguoiDung,
                maThietBi,
                dangHoatDong -> {

                    if (dangHoatDong == null) {

                        Log.d(
                                "THIETBI",
                                "Không tìm thấy thiết bị " + maThietBi
                        );

                        SessionManager.logout(activity);

                        Intent intent = new Intent(
                                activity,
                                DangNhapActivity.class
                        );

                        intent.putExtra(
                                IntentKey.FALSE,
                                "Không tìm thấy thiết bị!"
                        );

                        activity.startActivity(intent);
                        activity.finish();

                    } else if (dangHoatDong) {

                        Log.d(
                                "THIETBI",
                                "Thiết bị đang hoạt động"
                        );
                        //MessageHelper.success(activity,"Thiết bị đang hoạt động");

                    } else {

                        Log.d(
                                "THIETBI",
                                "Thiết bị đã bị vô hiệu hóa"
                        );

                        SessionManager.logout(activity);

                        Intent intent = new Intent(
                                activity,
                                DangNhapActivity.class
                        );

                        intent.putExtra(
                                IntentKey.FALSE,
                                "Thiết bị đã bị vô hiệu hóa!"
                        );

                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
        );
    }

    public static ValueEventListener kiemTraHoatDongThietBi(
            Activity activity,
            String maNguoiDung,
            String maThietBi
    ) {

        return repository.theoDoiDangHoatDong(
                maNguoiDung,
                maThietBi,
                dangHoatDong -> {

                    if (dangHoatDong == null
                            || Boolean.FALSE.equals(dangHoatDong)) {

                        SessionManager.logout(activity);

                        Intent intent = new Intent(
                                activity,
                                DangNhapActivity.class
                        );

                        intent.putExtra(
                                IntentKey.FALSE,
                                dangHoatDong == null
                                        ? "Không tìm thấy thiết bị!"
                                        : "Thiết bị đã bị vô hiệu hóa!"
                        );

                        activity.startActivity(intent);
                        activity.finish();
                    }
                    else{

                    }
                }
        );
    }

    public static void stopTheoDoi(
            String maNguoiDung,
            String maThietBi,
            ValueEventListener listener
    ) {

        if (listener != null) {

            FirebaseManager.ThietBiDangNhap()
                    .child(maNguoiDung)
                    .child(maThietBi)
                    .removeEventListener(listener);
        }
    }
}