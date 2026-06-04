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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.google.android.material.card.MaterialCardView;

import com.example.numberfindinggame.repository.OnCheckListener;
import com.example.numberfindinggame.repository.NguoiDungRepository;

public class DangKyActivity extends AppCompatActivity {

    private TextView txtLogin;
    private EditText edtUsername, edtEmail, edtPhone, edtPassword;
    private TextView txtLoiUser, txtLoiEmail, txtLoiSoDienThoai, txtLoiMatKhau;

    private ImageView imgShowPassword;

    private MaterialCardView cardDangKy;

    private boolean isPasswordVisible = false;

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
                                        "LOCAL",                // loaiDangNhap
                                        currentTime,            // dangNhapCuoi
                                        currentTime,            // ngayCapNhat
                                        currentTime             // ngayTao
                                );

                                repository.themNguoiDung(
                                        nguoiDung,
                                        task -> {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(
                                                        DangKyActivity.this,
                                                        "Đăng ký thành công",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                            } else {

                                                Toast.makeText(
                                                        DangKyActivity.this,
                                                        "Đăng ký thất bại",
                                                        Toast.LENGTH_SHORT
                                                ).show();

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

        boolean isValid = true;

        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString();

        // Xóa lỗi cũ
        txtLoiUser.setText("");
        txtLoiEmail.setText("");
        txtLoiSoDienThoai.setText("");
        txtLoiMatKhau.setText("");

        // ================= USERNAME =================

        if (username.isEmpty()) {
            txtLoiUser.setText("Vui lòng nhập tên đăng nhập.");
            isValid = false;
        } else if (username.length() < 3) {
            txtLoiUser.setText("Tên đăng nhập phải có ít nhất 3 ký tự.");
            isValid = false;
        } else if (username.length() > 50) {
            txtLoiUser.setText("Tên đăng nhập không được vượt quá 50 ký tự.");
            isValid = false;
        } else if (!username.matches("^[a-zA-Z0-9_.]+$")) {
            txtLoiUser.setText("Tên đăng nhập chỉ được chứa chữ cái, số, dấu _ và dấu .");
            isValid = false;
        }

        // ================= EMAIL =================

        String emailRegex =
                "^[a-zA-Z0-9._%+-]+@(?!(?:[0-9]+\\.)+[a-zA-Z]{2,})[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (email.isEmpty()) {
            txtLoiEmail.setText("Vui lòng nhập email.");
            isValid = false;
        } else if (email.length() > 100) {
            txtLoiEmail.setText("Email không được vượt quá 100 ký tự.");
            isValid = false;
        } else if (!email.matches(emailRegex)) {
            txtLoiEmail.setText("Email không đúng định dạng.");
            isValid = false;
        }

        // ================= PHONE =================

        if (phone.isEmpty()) {
            txtLoiSoDienThoai.setText("Vui lòng nhập số điện thoại.");
            isValid = false;
        } else if (phone.length() > 20) {
            txtLoiSoDienThoai.setText("Số điện thoại không được vượt quá 20 ký tự.");
            isValid = false;
        } else if (phone.matches(".*[０-９].*")) {
            txtLoiSoDienThoai.setText("Không được sử dụng ký tự số đặc biệt.");
            isValid = false;
        } else if (!phone.matches("^(0|\\+84)[0-9]{9,10}$")) {
            txtLoiSoDienThoai.setText(
                    "Số điện thoại phải bắt đầu bằng 0 hoặc +84 và chứa 10-11 chữ số.");
            isValid = false;
        }

        // ================= PASSWORD =================

        if (password.isEmpty()) {
            txtLoiMatKhau.setText("Vui lòng nhập mật khẩu.");
            isValid = false;
        } else if (password.length() < 6) {
            txtLoiMatKhau.setText("Mật khẩu phải có ít nhất 6 ký tự.");
            isValid = false;
        } else if (password.length() > 225) {
            txtLoiMatKhau.setText("Mật khẩu không được vượt quá 225 ký tự.");
            isValid = false;
        }

        return isValid;
    }
}