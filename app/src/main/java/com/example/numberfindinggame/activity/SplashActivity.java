package com.example.numberfindinggame.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.activity.home.TrangChuActivity;

public class SplashActivity extends AppCompatActivity {
//Đây là màn hình xuất hiện đầu tiên khi mở app.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences =
                getSharedPreferences("USER", MODE_PRIVATE);

        String maNguoiDung =
                preferences.getString("MaNguoiDung", "");

        if (!maNguoiDung.isEmpty()) {

            startActivity(
                    new Intent(
                            this,
                            TrangChuActivity.class));

        } else {

            startActivity(
                    new Intent(
                            this,
                            DangNhapActivity.class));
        }

        finish();
    }
}