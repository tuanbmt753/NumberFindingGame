package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.constant.LoginType;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.example.numberfindinggame.session.HieuUngSession;
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

    private NguoiDungRepository repository = new NguoiDungRepository();
    private NguoiDung nguoiDung = new NguoiDung();

    private String emailNguoiDung = "";
    private HieuUngGlitchLayout layoutGlitch;
    private ConstraintLayout layoutLogo;
    private GlitchView viewNhieu;

    private HieuUngSession hieuUngSession;
    private Integer hieuUng = 4;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);

        setControl();
        setEvent();

    }

    private void setEvent() {

        MusicManager.play(this);

        //Khởi tạo nhạc hiệu ứng 1 lần
        SoundManager.init(this);

        hieuUngSession = new HieuUngSession(this);
        // Nếu lần đầu tiên chưa có dữ liệu
        if (!hieuUngSession.isHieuUngExists()) {

            hieuUng = 4;

            // Lưu mặc định hiệu ứng 4
            hieuUngSession.setHieuUng(hieuUng);

        }
        hieuUng = hieuUngSession.getHieuUng();
        if (hieuUng == 3 || hieuUng == 4) {
            SoundManager.playElectric(DangKyActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }

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

        if (getIntent().hasExtra(IntentKey.NGUOI_DUNG)) {

            NguoiDung nguoiDung =
                    (NguoiDung) getIntent().getSerializableExtra(
                            IntentKey.NGUOI_DUNG
                    );

            if (nguoiDung != null) {
                themNguoiDung(repository, nguoiDung);
            } else {

                MessageHelper.error(DangKyActivity.this, "Thêm dữ liệu không thành công");
            }
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

                                nguoiDung = new NguoiDung(
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

                                emailNguoiDung = email;
                                //themNguoiDung(repository, nguoiDung, email);

                                //Chuyển màn hình xác thực email
                                Intent intent = new Intent(
                                        DangKyActivity.this,
                                        XacThucEmailActivity.class
                                );

                                intent.putExtra(IntentKey.EMAIL, email);
                                intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_KY); // nếu cần
                                intent.putExtra(
                                        IntentKey.NGUOI_DUNG,
                                        nguoiDung
                                );

                                startActivity(intent);
                                finish();
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

    private void setControl() {

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
        layoutGlitch = findViewById(R.id.layoutGlitch);
        layoutLogo = findViewById(R.id.layoutLogo);
        viewNhieu = findViewById(R.id.viewNhieu);

    }

    private void themNguoiDung(NguoiDungRepository repository, NguoiDung nguoiDung) {
        LoadingDialog loading =
                new LoadingDialog(DangKyActivity.this);
        loading.show();

        repository.themNguoiDung(
                nguoiDung,
                task -> {

                    loading.dismiss();

                    if (task.isSuccessful()) {


                        MessageHelper.success(
                                DangKyActivity.this,
                                "Đăng ký thành công"
                        );
                        taoCaiDatMatDinh(nguoiDung);

                        // Chuyển màn hình
                        Intent intent = new Intent(
                                DangKyActivity.this,
                                DangNhapActivity.class
                        );

                        intent.putExtra(IntentKey.EMAIL, nguoiDung.getEmail());
                        intent.putExtra(IntentKey.PASSWORD, edtPassword.getText()); // nếu cần
                        intent.putExtra(IntentKey.TEXT, "Đăng ký tài khoản thành công!"); // nếu cần

                        startActivity(intent);
                        finish();

                        edtUsername.setText("");
                        edtEmail.setText("");
                        edtPhone.setText("");
                        edtPassword.setText("");

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

    private void taoCaiDatMatDinh(NguoiDung nguoiDung) {
        CaiDatRepository.taoMacDinhNeuChuaCo(
                nguoiDung.getMaNguoiDung(),
                task -> {

                    if (task.isSuccessful()) {
                        Log.d("CAIDAT", "Đã có hoặc đã tạo mới thành công");
                    } else {
                        Log.e(
                                "CAIDAT",
                                task.getException().getMessage()
                        );
                    }
                }
        );
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(glitchRunnable);
    }

    private Runnable glitchRunnable = new Runnable() {
        @Override
        public void run() {

            if (hieuUng == 3) {

                // =========================
                // HIỆU ỨNG 3
                // =========================

                viewNhieu.setVisibility(View.VISIBLE);
                layoutLogo.setAlpha(0f);

                // Hiệu ứng logo xuất hiện glitch
                HieuUngHelper.hieuUngGlitch(layoutLogo);

                // Hiệu ứng nhiễu màn hình
                viewNhieu.startGlitch(900);

            } else if (hieuUng == 4) {
                viewNhieu.setVisibility(View.GONE);
                // =========================
                // HIỆU ỨNG 4
                // =========================

                layoutGlitch.batDauGlitch(900);
            }

            // Phát nhạc electric.mp3
            SoundManager.playElectric(DangKyActivity.this);

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 7000);
            }
        }
    };
}