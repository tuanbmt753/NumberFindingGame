package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.OnLoginListener;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.utils.Validator;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DangNhapActivity extends AppCompatActivity {

    private MaterialCardView cardDangNhap, cardDangKy;
    private EditText edtEmail, edtPassword;

    private TextView txtLoiEmail, txtLoiMatKhau, txtQuenMatKhau;

    private ImageView imgShowPassword;
    private boolean isPasswordVisible = false;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();

    private ThietBiDangNhapRepository repository = new ThietBiDangNhapRepository();

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

        if (getIntent().hasExtra(IntentKey.EMAIL)) {

            String email = getIntent().getStringExtra(IntentKey.EMAIL);
            String password = getIntent().getStringExtra(IntentKey.PASSWORD);

            edtEmail.setText(email);
            edtPassword.setText(password);


        }

        if (getIntent().hasExtra(IntentKey.TEXT)) {
            String text = getIntent().getStringExtra(IntentKey.TEXT);
            MessageHelper.success(
                    DangNhapActivity.this,
                    text
            );

        }
        if (getIntent().hasExtra(IntentKey.FALSE)) {
            String text = getIntent().getStringExtra(IntentKey.FALSE);
            MessageHelper.error(
                    DangNhapActivity.this,
                    text
            );
        }

        //SessionManager.logout(this);

        if (!SessionManager.getUserId(this).isEmpty()) {

            Intent intent = new Intent(this, TrangChuActivity.class);
            intent.putExtra(IntentKey.TRUE, "Đăng nhập thành công!"); // nếu cần

            startActivity(
                    intent
            );
            finish();
//            MessageHelper.success(
//                    DangNhapActivity.this,
//                    "Đăng nhập thành công"
//            );
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

                //Đăng  nhập
                loading.show();
                loading.setMessage("Đang đăng nhập...");

                nguoiDungRepository.dangNhap(
                        email,
                        password,
                        new OnLoginListener() {
                            @Override
                            public void onSuccess(NguoiDung nguoiDung) {


                                nguoiDungRepository.capNhatDangNhapCuoi(
                                        nguoiDung.getMaNguoiDung()
                                );

                                SessionManager.saveUser(
                                        DangNhapActivity.this,
                                        nguoiDung.getMaNguoiDung()
                                );

                                String maNguoiDung = nguoiDung.getMaNguoiDung();
                                String maThietBi = DeviceHelper.getDeviceId(DangNhapActivity.this);

                                repository.layThietBi(
                                        maNguoiDung,
                                        maThietBi,
                                        new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                Long ngayHienTai =
                                                        System.currentTimeMillis();

                                                if (!snapshot.exists()) {

                                                    // Chưa có thiết bị
                                                    ThietBiDangNhap thietBi =
                                                            new ThietBiDangNhap();

                                                    thietBi.setMaNguoiDung(maNguoiDung);
                                                    thietBi.setMaThietBi(maThietBi);
                                                    thietBi.setTenThietBi(
                                                            Build.MANUFACTURER + " " + Build.MODEL
                                                    );
                                                    thietBi.setNgayTao(ngayHienTai);
                                                    thietBi.setNgayCapNhatCuoi(ngayHienTai);
                                                    thietBi.setDangHoatDong(true);

                                                    repository.luuThietBiDangNhap(
                                                            thietBi,
                                                            task -> {

                                                                if (task.isSuccessful()) {
                                                                    Log.d(
                                                                            "THIET_BI",
                                                                            "Lưu thành công"
                                                                    );
                                                                }

                                                            }
                                                    );

                                                    // Chuyển màn hình
                                                    Intent intent = new Intent(
                                                            DangNhapActivity.this,
                                                            TrangChuActivity.class
                                                    );
                                                    intent.putExtra(IntentKey.TRUE, "Đăng nhập thành công!"); // nếu cần
                                                    startActivity(intent);
                                                    finish();

                                                } else {

                                                    // Đã có thiết bị
                                                    repository.capNhatLanDangNhapCuoi(
                                                            maNguoiDung,
                                                            maThietBi
                                                    );

                                                    // Chuyển màn hình
                                                    Intent intent = new Intent(
                                                            DangNhapActivity.this,
                                                            TrangChuActivity.class
                                                    );

                                                    intent.putExtra(IntentKey.TRUE, "Đăng nhập thành công!"); // nếu cần
                                                    startActivity(intent);
                                                    finish();

                                                    Log.d(
                                                            "THIETBI",
                                                            "Đã cập nhật lần đăng nhập cuối"
                                                    );


                                                }

                                            }

                                            @Override
                                            public void onCancelled(
                                                    @NonNull DatabaseError error
                                            ) {

                                            }

                                        }
                                );


                                loading.dismiss();


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

                intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DOI_MAT_KHAU);
                startActivity(intent);
                finish();
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