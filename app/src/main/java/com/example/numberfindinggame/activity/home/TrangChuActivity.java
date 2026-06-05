package com.example.numberfindinggame.activity.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;

public class TrangChuActivity extends AppCompatActivity {

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

    }
}