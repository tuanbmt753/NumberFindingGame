package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.session.HieuUngSession;
import com.example.numberfindinggame.session.NhacHieuUngNenSession;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.example.numberfindinggame.utils.Validator;
import com.google.android.material.card.MaterialCardView;

public class DoiMatKhauActivity extends AppCompatActivity {

    private TextView txtLoiMatKhau, txtLoiNhapLai, txtDangNhap;
    private EditText edtPassword, edtNhapLai;

    private MaterialCardView cardXacNhan;

    private String email;

    private ImageView imgShowPassword, imgNhapLai;

    private boolean isPasswordVisible = false;
    private boolean isPasswordVisible2 = false;
    private HieuUngGlitchLayout layoutGlitch;
    private ConstraintLayout layoutLogo;
    private GlitchView viewNhieu;

    private HieuUngSession hieuUngSession;
    private Integer hieuUng = 4;
    private Handler handler = new Handler(Looper.getMainLooper());
    private NhacHieuUngNenSession nhacHieuUngNenSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doi_mat_khau);

        setControl();
        setEvent();

    }

    private void setEvent() {
        nhacHieuUngNenSession =
                new NhacHieuUngNenSession(this);
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
            SoundManager.playElectric(DoiMatKhauActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }


        txtLoiMatKhau.setText("");
        txtLoiNhapLai.setText("");

        if (getIntent().hasExtra(IntentKey.EMAIL)) {

            email = getIntent().getStringExtra(IntentKey.EMAIL);

            MessageHelper.success(
                    DoiMatKhauActivity.this,
                    "Xác thực " + email + " thành công"
            );
        }

        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển màn hình
                Intent intent = new Intent(
                        DoiMatKhauActivity.this,
                        DangNhapActivity.class
                );

                intent.putExtra(IntentKey.TEXT, "Đã hủy đổi mật khẩu!"); // nếu cần

                startActivity(intent);
                finish();
            }
        });

        cardXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kiemTraMang();

                if (!validateInput()) {
                    return;
                }

                LoadingDialog loading =
                        new LoadingDialog(DoiMatKhauActivity.this);
                loading.setMessage("Đổi mật khẩu...");
                loading.show();

                NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
                nguoiDungRepository.doiMatKhau(
                        email,
                        edtPassword.getText().toString().trim(),
                        task -> {

                            if (task.isSuccessful()) {

                                MessageHelper.success(
                                        DoiMatKhauActivity.this,
                                        "Đổi mật khẩu thành công"
                                );
                                loading.dismiss();

                                if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
                                    String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);

                                    if (activityType.toString().trim().equals(ActivityType.DOI_MAT_KHAU)) {
                                        // Chuyển màn hình
                                        Intent intent = new Intent(
                                                DoiMatKhauActivity.this,
                                                DangNhapActivity.class
                                        );

                                        intent.putExtra(IntentKey.EMAIL, email);
                                        intent.putExtra(IntentKey.PASSWORD, edtPassword.getText().toString()); // nếu cần
                                        intent.putExtra(IntentKey.TEXT, "Đổi mật khẩu thành công!"); // nếu cần

                                        startActivity(intent);
                                        finish();

                                    }

                                    if (activityType.toString().trim().equals(ActivityType.THONG_TIN_NGUOI_DUNG)) {
                                        // Chuyển màn hình
                                        Intent intent = new Intent(
                                                DoiMatKhauActivity.this,
                                                ThongTinNguoiDungActivity.class
                                        );

                                        intent.putExtra(IntentKey.TEXT, "Đổi mật khẩu thành công!"); // nếu cần

                                        startActivity(intent);
                                        finish();

                                    }
                                }


                            } else {

                                MessageHelper.error(
                                        DoiMatKhauActivity.this,
                                        task.getException().getMessage()
                                );
                                loading.dismiss();
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

        imgNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible2) {
                    // Ẩn mật khẩu
                    edtNhapLai.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                    isPasswordVisible2 = false;

                    // Đổi icon nếu muốn
                    imgNhapLai.setImageResource(R.drawable.ic_eye);

                } else {
                    // Hiện mật khẩu
                    edtNhapLai.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible2 = true;

                    // Đổi icon nếu muốn
                    imgNhapLai.setImageResource(R.drawable.ic_eye_visibility);
                }

                // Đặt lại focus
                edtNhapLai.requestFocus();

                // Đưa con trỏ về cuối
                edtNhapLai.setSelection(
                        edtNhapLai.getText().toString().length()
                );
            }
        });
    }

    private void setControl() {
        txtLoiMatKhau = findViewById(R.id.txtLoiMatKhau);
        txtLoiNhapLai = findViewById(R.id.txtLoiNhapLai);
        txtDangNhap = findViewById(R.id.txtDangNhap);

        edtPassword = findViewById(R.id.edtPassword);
        edtNhapLai = findViewById(R.id.edtNhapLai);

        cardXacNhan = findViewById(R.id.cardXacNhan);

        imgShowPassword = findViewById(R.id.imgShowPassword);
        imgNhapLai = findViewById(R.id.imgNhapLai);

        layoutGlitch = findViewById(R.id.layoutGlitch);
        layoutLogo = findViewById(R.id.layoutLogo);
        viewNhieu = findViewById(R.id.viewNhieu);

    }

    private void kiemTraMang() {
        if (!NetworkHelper.isConnected(DoiMatKhauActivity.this)) {

            MessageHelper.error(
                    DoiMatKhauActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        if (NetworkHelper.isWifiConnected(DoiMatKhauActivity.this)) {

            MessageHelper.info(
                    DoiMatKhauActivity.this,
                    "Đang sử dụng WiFi"
            );

        }

        if (NetworkHelper.isMobileDataConnected(DoiMatKhauActivity.this)) {

            MessageHelper.info(
                    DoiMatKhauActivity.this,
                    "Đang sử dụng dữ liệu di động"
            );

        }
    }

    private boolean validateInput() {
        txtLoiNhapLai.setText("");
        txtLoiMatKhau.setText("");

        boolean isValid = true;

        String nhapLai = edtNhapLai.getText().toString().trim();
        String password = edtPassword.getText().toString();


        String passwordError = Validator.validatePassword(password);
        String nhapLaiError = Validator.validateNhapLaiPassword(password, nhapLai);


        if (passwordError != null) {
            txtLoiMatKhau.setText(passwordError);
            isValid = false;
        }

        if (nhapLaiError != null) {
            txtLoiNhapLai.setText(nhapLaiError);
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
            // Chỉ phát âm thanh hiệu ứng khi đang bật
            if (nhacHieuUngNenSession.isNhacHieuUng()) {
                SoundManager.playElectric(DoiMatKhauActivity.this);
            }

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 15000);
            }
        }
    };
}