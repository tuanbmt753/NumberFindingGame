package com.example.numberfindinggame.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.android.material.card.MaterialCardView;

public class TrangChuActivity extends AppCompatActivity {
    private MaterialCardView cardThoat;
    private ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        kiemTraThietBiDangNhap(SessionManager.getUserId(this), DeviceHelper.getDeviceId(TrangChuActivity.this));

        getControl();
        getView();

    }

    private void getView() {

        cardThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TrangChuActivity.this,
                        DangNhapActivity.class
                );
                intent.putExtra(IntentKey.TEXT, "Đăng xuất tài khoản thành công!"); // nếu cần

                startActivity(intent);
                SessionManager.logout(TrangChuActivity.this);
                finish();
            }
        });
    }

    private void getControl() {
        cardThoat = findViewById(R.id.cardThoat);

    }

    private void kiemTraThietBiDangNhap(String maNguoiDung, String maThietBi) {
        thietBiDangNhapRepository.kiemTraDangHoatDong(
                maNguoiDung,
                maThietBi,
                dangHoatDong -> {

                    if (dangHoatDong == null) {

                        Log.d(
                                "THIETBI",
                                "Không tìm thấy thiết bị" + maThietBi
                        );

                        Intent intent = new Intent(
                                TrangChuActivity.this,
                                DangNhapActivity.class
                        );

                        //intent.putExtra(IntentKey.TEXT, "Thiết bị đã bị vô hiệu hóa!"); // nếu cần
                        intent.putExtra(IntentKey.FALSE, "Không tìm thấy thiết bị!"); // nếu cần

                        startActivity(intent);
                        //SessionManager.logout(TrangChuActivity.this);
                        finish();

                    } else if (dangHoatDong) {

                        Log.d(
                                "THIETBI",
                                "Thiết bị đang hoạt động"
                        );

                        if (!SessionManager.getUserId(this).isEmpty()) {
                            MessageHelper.success(
                                    TrangChuActivity.this,
                                    "Đăng nhập thành công"
                            );
                        }

                    } else {

                        Log.d(
                                "THIETBI",
                                "Thiết bị đã bị vô hiệu hóa"
                        );

                        Intent intent = new Intent(
                                TrangChuActivity.this,
                                DangNhapActivity.class
                        );

                        //intent.putExtra(IntentKey.TEXT, "Thiết bị đã bị vô hiệu hóa!"); // nếu cần
                        intent.putExtra(IntentKey.FALSE, "Thiết bị đã bị vô hiệu hóa!"); // nếu cần

                        startActivity(intent);
                        SessionManager.logout(TrangChuActivity.this);
                        finish();

                    }

                }
        );
    }
}