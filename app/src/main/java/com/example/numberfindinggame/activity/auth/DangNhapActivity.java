package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.OnLoginListener;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.utils.Validator;
import com.google.android.material.card.MaterialCardView;

public class DangNhapActivity extends AppCompatActivity {

    private MaterialCardView cardDangNhap, cardDangKy;
    private EditText edtEmail, edtPassword;

    private TextView txtLoiEmail, txtLoiMatKhau, txtQuenMatKhau;

    private ImageView imgShowPassword;
    private boolean isPasswordVisible = false;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);

        getControl();
        getView();

    }

    private void getView() {
        txtLoiEmail.setText("");
        txtLoiMatKhau.setText("");
        //SessionManager.logout(this);

        if (!SessionManager.getUserId(this).isEmpty()) {
            startActivity(
                    new Intent(this, TrangChuActivity.class)
            );
            finish();
            MessageHelper.success(
                    DangNhapActivity.this,
                    "Đăng nhập thành công"
            );
        }

        cardDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DangNhapActivity.this,
                    DangKyActivity.class
            );
            startActivity(intent);
            finish();
        });

        cardDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoadingDialog loading =
                        new LoadingDialog(DangNhapActivity.this);
                loading.setMessage("Đang kiểm tra tài khoản...");
                loading.show();

                if (!NetworkHelper.isConnected(DangNhapActivity.this)) {

                    MessageHelper.error(
                            DangNhapActivity.this,
                            "Không có kết nối Internet"
                    );
                    loading.dismiss();
                    return;
                }

                if (NetworkHelper.isWifiConnected(DangNhapActivity.this)) {

//                    MessageHelper.info(
//                            DangNhapActivity.this,
//                            "Đang sử dụng WiFi"
//                    );

                }

                if (NetworkHelper.isMobileDataConnected(DangNhapActivity.this)) {

//                    MessageHelper.info(
//                            DangNhapActivity.this,
//                            "Đang sử dụng dữ liệu di động"
//                    );

                }

                if (!validateInput()) {
                    loading.dismiss();
                    return;
                }

                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();


                nguoiDungRepository.dangNhap(
                        email,
                        password,
                        new OnLoginListener() {
                            @Override
                            public void onSuccess(NguoiDung nguoiDung) {


                                nguoiDungRepository.capNhatDangNhapCuoi(
                                        nguoiDung.getMaNguoiDung()
                                );

                                MessageHelper.success(
                                        DangNhapActivity.this,
                                        "Đăng nhập thành công"
                                );

                                SessionManager.saveUser(
                                        DangNhapActivity.this,
                                        nguoiDung.getMaNguoiDung()
                                );

                                loading.setMessage("Đang đăng nhập...");
                                loading.dismiss();

                                // Chuyển màn hình
                                Intent intent = new Intent(
                                        DangNhapActivity.this,
                                        TrangChuActivity.class
                                );

                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailed() {

                                loading.dismiss();

                                MessageHelper.error(
                                        DangNhapActivity.this,
                                        "Email hoặc mật khẩu không đúng"
                                );
                            }
                        }
                );


            }
        });

        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Ẩn mật khẩu
                    edtPassword.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;

                    // Đổi icon nếu muốn
                    imgShowPassword.setImageResource(R.drawable.ic_eye);

                } else {
                    // Hiện mật khẩu
                    edtPassword.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;

                    // Đổi icon nếu muốn
                    imgShowPassword.setImageResource(R.drawable.ic_eye_visibility);
                }

                // Đặt lại focus
                edtPassword.requestFocus();

                // Đưa con trỏ về cuối
                edtPassword.setSelection(
                        edtPassword.getText().toString().length()
                );
            }
        });


        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLoiEmail.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLoiMatKhau.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        DangNhapActivity.this,
                        XacThucEmailActivity.class
                );
                startActivity(intent);
                //finish();
            }
        });
    }

    private void getControl() {
        cardDangNhap = findViewById(R.id.cardDangNhap);
        cardDangKy = findViewById(R.id.cardDangKy);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txtLoiEmail = findViewById(R.id.txtLoiEmail);
        txtLoiMatKhau = findViewById(R.id.txtLoiMatKhau);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);

        imgShowPassword = findViewById(R.id.imgShowPassword);
    }

    private boolean validateInput() {
        txtLoiEmail.setText("");
        txtLoiMatKhau.setText("");

        boolean isValid = true;

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();


        String emailError = Validator.validateEmail(email);
        String passwordError = Validator.validatePassword(password);


        if (emailError != null) {
            txtLoiEmail.setText(emailError);
            isValid = false;
        }


        if (passwordError != null) {
            txtLoiMatKhau.setText(passwordError);
            isValid = false;
        }

        return isValid;
    }
}