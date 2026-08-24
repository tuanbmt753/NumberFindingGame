package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.session.HieuUngSession;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.CaiDatRepository;
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

    private ConstraintLayout layoutLogo;
    private GlitchView viewNhieu;
    private HieuUngGlitchLayout layoutGlitch;

    private HieuUngSession hieuUngSession;
    private Integer hieuUng = 4;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);

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
            SoundManager.playElectric(DangNhapActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }


        txtLoiEmail.setText("");
        txtLoiMatKhau.setText("");

        layoutLogo.setAlpha(0f);

        // Hiệu ứng logo xuất hiện glitch
        HieuUngHelper.hieuUngGlitch(layoutLogo);

        // Hiệu ứng nhiễu màn hình
        viewNhieu.startGlitch(900);

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

        if (getIntent().hasExtra(IntentKey.TRUE)) {
            String text = getIntent().getStringExtra(IntentKey.TRUE);

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

        if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
            String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);

            if (activityType.equals(ActivityType.DANG_NHAP)) {
                String text = getIntent().getStringExtra(IntentKey.TEXT);

                MessageHelper.success(
                        DangNhapActivity.this,
                        text
                );

                NguoiDung nguoiDung =
                        (NguoiDung) getIntent().getSerializableExtra(
                                IntentKey.NGUOI_DUNG
                        );

                nguoiDungRepository.capNhatDangNhapCuoi(
                        nguoiDung.getMaNguoiDung()
                );

                SessionManager.saveUser(
                        DangNhapActivity.this,
                        nguoiDung.getMaNguoiDung()
                );

                //Lưu thiết bị đăng nhập
                luuThietBi(nguoiDung);


            }

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

                                CaiDatRepository.layCaiDat(
                                        nguoiDung.getMaNguoiDung(),
                                        new CaiDatCallback() {
                                            @Override
                                            public void onSuccess(CaiDat caiDat) {
                                                if (caiDat.getXacThucEmail() == true) {

                                                    // Chuyển màn hình
                                                    Intent intent = new Intent(
                                                            DangNhapActivity.this,
                                                            ChonXacThucActivity.class
                                                    );

                                                    nguoiDung.setHinhDaiDien(null);
                                                    nguoiDung.setHinhNen(null);

                                                    intent.putExtra(IntentKey.EMAIL, nguoiDung.getEmail());
                                                    intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_NHAP); // nếu cần
                                                    intent.putExtra(
                                                            IntentKey.NGUOI_DUNG,
                                                            nguoiDung
                                                    );

                                                    startActivity(intent);
                                                    finish();


                                                    loading.dismiss();

                                                } else {
                                                    nguoiDungRepository.capNhatDangNhapCuoi(
                                                            nguoiDung.getMaNguoiDung()
                                                    );

                                                    SessionManager.saveUser(
                                                            DangNhapActivity.this,
                                                            nguoiDung.getMaNguoiDung()
                                                    );

                                                    //Lưu thiết bị đăng nhập
                                                    luuThietBi(nguoiDung);

                                                    loading.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(String message) {

                                                Log.e("CAIDAT", message);
                                            }
                                        }
                                );
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

    private void luuThietBi(NguoiDung nguoiDung) {
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
                            if (getIntent().hasExtra(IntentKey.MA_KHOI_PHUC)) {
                                intent.putExtra(IntentKey.MA_KHOI_PHUC, IntentKey.MA_KHOI_PHUC); // nếu cần
                            }

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
                            if (getIntent().hasExtra(IntentKey.MA_KHOI_PHUC)) {
                                intent.putExtra(IntentKey.MA_KHOI_PHUC, IntentKey.MA_KHOI_PHUC); // nếu cần
                            }

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
    }

    private void setControl() {
        cardDangNhap = findViewById(R.id.cardDangNhap);
        cardDangKy = findViewById(R.id.cardDangKy);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txtLoiEmail = findViewById(R.id.txtLoiEmail);
        txtLoiMatKhau = findViewById(R.id.txtLoiMatKhau);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);

        imgShowPassword = findViewById(R.id.imgShowPassword);

        layoutLogo = findViewById(R.id.layoutLogo);
        viewNhieu = findViewById(R.id.viewNhieu);
        layoutGlitch = findViewById(R.id.layoutGlitch);
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
            SoundManager.playElectric(DangNhapActivity.this);

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 7000);
            }
        }
    };

}