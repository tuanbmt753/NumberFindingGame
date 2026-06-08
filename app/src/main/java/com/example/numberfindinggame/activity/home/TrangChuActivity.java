package com.example.numberfindinggame.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.google.android.material.card.MaterialCardView;

public class TrangChuActivity extends AppCompatActivity {
    private MaterialCardView cardThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        if (!SessionManager.getUserId(this).isEmpty()) {
            MessageHelper.success(
                    TrangChuActivity.this,
                    "Đăng nhập thành công"
            );
        }

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
}