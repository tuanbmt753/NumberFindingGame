package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
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
import com.example.numberfindinggame.constant.LoginType;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;

import com.example.numberfindinggame.repository.OnCheckListener;
import com.example.numberfindinggame.repository.NguoiDungRepository;

import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.utils.Validator;

import com.example.numberfindinggame.helper.NetworkHelper;

public class DangKyActivity extends AppCompatActivity {

    private TextView txtLogin;
    private EditText edtUsername, edtEmail, edtPhone, edtPassword;
    private TextView txtLoiUser, txtLoiEmail, txtLoiSoDienThoai, txtLoiMatKhau;

    private ImageView imgShowPassword;

    private MaterialCardView cardDangKy;

    private boolean isPasswordVisible = false;

    private LoadingDialog loading;

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
        txtLoiUser.setText("");
        txtLoiEmail.setText("");
        txtLoiSoDienThoai.setText("");
        txtLoiMatKhau.setText("");


        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DangKyActivity.this,
                    DangNhapActivity.class
            );
            startActivity(intent);
            finish();
        });

        cardDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!NetworkHelper.isConnected(DangKyActivity.this)) {

                    MessageHelper.error(
                            DangKyActivity.this,
                            "Không có kết nối Internet"
                    );

                    return;
                }

                if (NetworkHelper.isWifiConnected(DangKyActivity.this)) {

                    MessageHelper.info(
                            DangKyActivity.this,
                            "Đang sử dụng WiFi"
                    );

                }

                if (NetworkHelper.isMobileDataConnected(DangKyActivity.this)) {

                    MessageHelper.info(
                            DangKyActivity.this,
                            "Đang sử dụng dữ liệu di động"
                    );

                }

                if (!validateInput()) {
                    return;
                }

                txtLoiUser.setText("");
                txtLoiEmail.setText("");
                txtLoiSoDienThoai.setText("");

                NguoiDungRepository repository = new NguoiDungRepository();

                String username = edtUsername.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();

                repository.kiemTraTonTai(
                        username,
                        email,
                        phone,
                        new OnCheckListener() {

                            @Override
                            public void onUsernameExists() {
                                txtLoiUser.setText("Tên đăng nhập đã tồn tại.");
                            }

                            @Override
                            public void onEmailExists() {
                                txtLoiEmail.setText("Email đã tồn tại.");
                            }

                            @Override
                            public void onPhoneExists() {
                                txtLoiSoDienThoai.setText("Số điện thoại đã tồn tại.");
                            }

                            @Override
                            public void onSuccess() {

                                String maNguoiDung = FirebaseManager
                                        .nguoiDung()
                                        .push()
                                        .getKey();

                                long currentTime = System.currentTimeMillis();

                                NguoiDung nguoiDung = new NguoiDung(
                                        maNguoiDung,
                                        username,
                                        email,
                                        phone,
                                        "",                     // hinhDaiDien
                                        "",                     // hinhNen
                                        edtPassword.getText().toString(),
                                        LoginType.LOCAL,                // loaiDangNhap
                                        currentTime,            // dangNhapCuoi
                                        currentTime,            // ngayCapNhat
                                        currentTime             // ngayTao
                                );

                                LoadingDialog loading =
                                        new LoadingDialog(DangKyActivity.this);
                                loading.show();

                                repository.themNguoiDung(
                                        nguoiDung,
                                        task -> {

                                                loading.dismiss();

                                            if (task.isSuccessful()) {
                                                edtUsername.setText("");
                                                edtEmail.setText("");
                                                edtPhone.setText("");
                                                edtPassword.setText("");

                                                MessageHelper.success(
                                                        DangKyActivity.this,
                                                        "Đăng ký thành công"
                                                );

                                            } else {
                                                edtUsername.setText("");
                                                edtEmail.setText("");
                                                edtPhone.setText("");
                                                edtPassword.setText("");

                                                MessageHelper.error(
                                                        DangKyActivity.this,
                                                        "Đăng ký thất bại"
                                                );
                                            }
                                        }
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

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLoiUser.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
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

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLoiSoDienThoai.setText("");
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
    }

    private void getControl() {

        txtLogin = findViewById(R.id.txtLogin);

        txtLoiUser = findViewById(R.id.txtLoiUser);
        txtLoiEmail = findViewById(R.id.txtLoiEmail);
        txtLoiSoDienThoai = findViewById(R.id.txtLoiSoDienThoai);
        txtLoiMatKhau = findViewById(R.id.txtLoiMatKhau);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        imgShowPassword = findViewById(R.id.imgShowPassword);

        cardDangKy = findViewById(R.id.cardDangKy);

    }

    private boolean validateInput() {

        txtLoiUser.setText("");
        txtLoiEmail.setText("");
        txtLoiSoDienThoai.setText("");
        txtLoiMatKhau.setText("");

        boolean isValid = true;

        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString();

        String usernameError = Validator.validateUsername(username);
        String emailError = Validator.validateEmail(email);
        String phoneError = Validator.validatePhone(phone);
        String passwordError = Validator.validatePassword(password);

        if (usernameError != null) {
            txtLoiUser.setText(usernameError);
            isValid = false;
        }

        if (emailError != null) {
            txtLoiEmail.setText(emailError);
            isValid = false;
        }

        if (phoneError != null) {
            txtLoiSoDienThoai.setText(phoneError);
            isValid = false;
        }

        if (passwordError != null) {
            txtLoiMatKhau.setText(passwordError);
            isValid = false;
        }

        return isValid;
    }
}