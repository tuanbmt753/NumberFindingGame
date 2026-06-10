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
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.ValueEventListener;

public class TrangChuActivity extends AppCompatActivity {
    private MaterialCardView cardThoat;
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