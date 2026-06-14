package com.example.numberfindinggame.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.ValueEventListener;

public class TrangChuActivity extends AppCompatActivity {
    private MaterialCardView cardDangXuat, cardCaiDat, cardThoat;
    private ValueEventListener dangHoatDongListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        dangHoatDongListener =
                ThietBiDangNhapHelper.kiemTraHoatDongThietBi(
                        this,
                        SessionManager.getUserId(this),
                        DeviceHelper.getDeviceId(TrangChuActivity.this)

                );

        getControl();
        getView();

    }

    private void getView() {

        if (getIntent().hasExtra(IntentKey.TRUE)) {
            String text = getIntent().getStringExtra(IntentKey.TRUE);
            MessageHelper.success(
                    TrangChuActivity.this,
                    text
            );
        }

        if (getIntent().hasExtra(IntentKey.MA_KHOI_PHUC)) {

            new ConfirmDialog(
                    TrangChuActivity.this,
                    "Xác nhận",
                    "✅ Đăng nhập thành công! ⚠️ Mã khôi phục đã được làm mới, xem ở phần cài đặt !",
                    new ConfirmDialog.ConfirmCallback() {

                        @Override
                        public void onYes() {

                        }

                        @Override
                        public void onNo() {

                        }
                    }
            ).show();

        }

        cardDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TrangChuActivity.this,
                        DangNhapActivity.class
                );


                SessionManager.logout(TrangChuActivity.this);

                ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();
                thietBiDangNhapRepository.voHieuHoaThietBi(
                        SessionManager.getUserId(TrangChuActivity.this),
                        DeviceHelper.getDeviceId(TrangChuActivity.this),
                        task -> {

                            if (task.isSuccessful()) {

                            }


                        }
                );

                //intent.putExtra(IntentKey.TEXT, "Đăng xuất tài khoản thành công!"); // nếu cần
                intent.putExtra(IntentKey.TRUE, "Đăng xuất tài khoản thành công!");
                startActivity(intent);
                finish();
            }
        });

        cardCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TrangChuActivity.this,
                        SettingActivity.class
                );

                startActivity(intent);
                finish();
            }
        });

        cardThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConfirmDialog(
                        TrangChuActivity.this,
                        "Xác nhận",
                        "Bạn có muốn thoát khỏi ứng dụng này không?",
                        new ConfirmDialog.ConfirmCallback() {

                            @Override
                            public void onYes() {
                                finish();
                            }

                            @Override
                            public void onNo() {

                            }
                        }
                ).show();
            }
        });
    }

    private void getControl() {
        cardDangXuat = findViewById(R.id.cardDangXuat);
        cardCaiDat = findViewById(R.id.cardCaiDat);
        cardThoat = findViewById(R.id.cardThoat);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ThietBiDangNhapHelper.stopTheoDoi(
                SessionManager.getUserId(this),
                DeviceHelper.getDeviceId(TrangChuActivity.this),
                dangHoatDongListener
        );
    }


}