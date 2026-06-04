package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.google.android.material.card.MaterialCardView;

public class DangKyActivity extends AppCompatActivity {

    TextView txtLogin;
    EditText edtUsername, edtEmail, edtPhone, edtPassword;
    ImageView imgShowPassword;

    MaterialCardView cardDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);

        getControl();
        getView();

    }

    private void getView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtLogin.setText(
                    Html.fromHtml(
                            "Đã có tài khoản? <font color='#143485'>Đăng nhập</font>",
                            Html.FROM_HTML_MODE_LEGACY
                    )
            );
        } else {
            txtLogin.setText(
                    Html.fromHtml(
                            "Đã có tài khoản? <font color='#143485'>Đăng nhập</font>"
                    )
            );
        }

        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DangKyActivity.this,
                    DangNhapActivity.class
            );
            startActivity(intent);
            finish();
        });

    }

    private void getControl() {

        txtLogin = findViewById(R.id.txtLogin);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        imgShowPassword = findViewById(R.id.imgShowPassword);

        cardDangKy = findViewById(R.id.cardDangKy);

    }
}