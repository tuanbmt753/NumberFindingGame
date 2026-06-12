package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.model.Emailjs;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.XacThucEmail;
import com.example.numberfindinggame.callback.EmailCallback;
import com.example.numberfindinggame.callback.EmailJsSelectCallback;
import com.example.numberfindinggame.repository.EmailjsRepository;
import com.example.numberfindinggame.callback.FirebaseCallback;
import com.example.numberfindinggame.repository.XacThucEmailRepository;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.example.numberfindinggame.utils.Validator;
import com.google.android.material.card.MaterialCardView;

import java.util.Random;

public class XacThucEmailActivity extends AppCompatActivity {
    private TextView txtQuayLai, txtNoiDung, txtLoiEmail, txtLoiOTP, txtOTP;

    private EditText edtEmail, edtOTP;

    private MaterialCardView cardEdtEmail, cardEdtOTP, cardEmail, cardOTP;

    private XacThucEmailRepository repository;
    private String emailHienTai;

    private CountDownTimer countDownTimer;
    private boolean hetHanOTP = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xac_thuc_email);

        getControl();
        getView();
    }

    private void getView() {
        //themEmailjs();

        txtNoiDung.setText("Xác thực email");
        txtLoiEmail.setText("");
        txtLoiOTP.setText("");

        showChucNangEmail();

        if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
            String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);

            if (activityType.equals(ActivityType.DANG_KY) || activityType.equals((ActivityType.DANG_XUAT_TU_XA))) {

                showChucNangOTP();
                String email = getIntent().getStringExtra(IntentKey.EMAIL);
                emailHienTai = email;

                int otp = 100000 + new Random().nextInt(900000);

                XacThucEmail xacThucEmail = new XacThucEmail();
                xacThucEmail.setEmail(email);
                xacThucEmail.setMaXacThuc("" + System.currentTimeMillis());
                xacThucEmail.setNgayTao(System.currentTimeMillis());
                xacThucEmail.setMaOTP(otp);

                repository = new XacThucEmailRepository();

                repository.themXacThucEmail(
                        xacThucEmail,
                        new XacThucEmailRepository.OnCompleteListener() {
                            @Override
                            public void onSuccess() {
                                layEmailJsSuDungNhoNhat(emailHienTai, otp);
                            }

                            @Override
                            public void onFailure(String message) {

                                MessageHelper.error(
                                        XacThucEmailActivity.this,
                                        message
                                );
                            }
                        }
                );
            }


        }

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
                    String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);
                    if (activityType.toString().trim().equals(ActivityType.DOI_MAT_KHAU)) {

                        Intent intent = new Intent(
                                XacThucEmailActivity.this,
                                DangNhapActivity.class
                        );

                        startActivity(intent);
                        finish();
                    }

                    if (activityType.toString().trim().equals(ActivityType.DANG_KY)) {

                        Intent intent = new Intent(
                                XacThucEmailActivity.this,
                                DangKyActivity.class
                        );

                        startActivity(intent);
                        finish();
                    }

                    if (activityType.toString().trim().equals(ActivityType.DANG_XUAT_TU_XA)) {

                        Intent intent = new Intent(
                                XacThucEmailActivity.this,
                                SettingActivity.class
                        );

                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        cardEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kiemTraMang();

                if (!validateInput()) {
                    return;
                }

                LoadingDialog loading =
                        new LoadingDialog(XacThucEmailActivity.this);
                loading.setMessage("Đang xác thực email...");
                loading.show();

                String email = edtEmail.getText().toString().trim();

                int otp = 100000 + new Random().nextInt(900000);

                XacThucEmail xacThucEmail = new XacThucEmail(
                        String.valueOf(System.currentTimeMillis()),
                        email,
                        System.currentTimeMillis(),
                        otp
                );

                repository =
                        new XacThucEmailRepository();

                repository.them(
                        xacThucEmail,
                        new XacThucEmailRepository.OnCompleteListener() {

                            @Override
                            public void onSuccess() {

                                loading.dismiss();
                                emailHienTai = email;
                                layEmailJsSuDungNhoNhat(emailHienTai, otp);

                            }

                            @Override
                            public void onFailure(String message) {

                                MessageHelper.error(
                                        XacThucEmailActivity.this,
                                        message
                                );
                                loading.dismiss();
                            }
                        }
                );

            }
        });

        cardOTP.setOnClickListener(v -> {

            if (hetHanOTP) {

                // Gửi lại OTP

                int otp = 100000 + new Random().nextInt(900000);

                XacThucEmail xacThucEmail = new XacThucEmail(
                        String.valueOf(System.currentTimeMillis()),
                        emailHienTai,
                        System.currentTimeMillis(),
                        otp
                );
                LoadingDialog loading =
                        new LoadingDialog(XacThucEmailActivity.this);
                loading.setMessage("Loading...");
                loading.show();

                repository.them(
                        xacThucEmail,
                        new XacThucEmailRepository.OnCompleteListener() {

                            @Override
                            public void onSuccess() {

                                MessageHelper.success(
                                        XacThucEmailActivity.this,
                                        "Đã gửi lại OTP"
                                );

                                batDauDemNguoc();
                                loading.dismiss();
                            }

                            @Override
                            public void onFailure(String message) {

                                MessageHelper.error(
                                        XacThucEmailActivity.this,
                                        message
                                );
                                loading.dismiss();
                            }
                        }
                );

                return;
            }

            // Kiểm tra OTP
            kiemTraOTP();
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

        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLoiOTP.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getControl() {
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtNoiDung = findViewById(R.id.txtNoiDung);
        txtLoiEmail = findViewById(R.id.txtLoiEmail);
        txtLoiOTP = findViewById(R.id.txtLoiOTP);
        txtOTP = findViewById(R.id.txtOTP);

        edtEmail = findViewById(R.id.edtEmail);
        edtOTP = findViewById(R.id.edtOTP);

        cardEdtEmail = findViewById(R.id.cardEdtEmail);
        cardEdtOTP = findViewById(R.id.cardEdtOTP);
        cardEmail = findViewById(R.id.cardEmail);
        cardOTP = findViewById(R.id.cardOTP);


    }

    private void showChucNangEmail() {
        cardEdtEmail.setVisibility(View.VISIBLE);
        cardEmail.setVisibility(View.VISIBLE);
        txtLoiEmail.setVisibility(View.VISIBLE);

        cardEdtOTP.setVisibility(View.GONE);
        txtLoiOTP.setVisibility(View.GONE);
        cardOTP.setVisibility(View.GONE);
    }

    private void showChucNangOTP() {
        cardEdtOTP.setVisibility(View.VISIBLE);
        txtLoiOTP.setVisibility(View.VISIBLE);
        cardOTP.setVisibility(View.VISIBLE);

        cardEdtEmail.setVisibility(View.GONE);
        cardEmail.setVisibility(View.GONE);
        txtLoiEmail.setVisibility(View.GONE);

        txtNoiDung.setText("Nhập mã OTP");
        txtOTP.setText("Gửi");

    }

    private boolean validateInput() {
        boolean isValid = true;

        if (cardEdtEmail.getVisibility() != View.GONE) {
            txtLoiEmail.setText("");
            String email = edtEmail.getText().toString().trim();
            String emailError = Validator.validateEmail(email);
            if (emailError != null) {
                txtLoiEmail.setText(emailError);
                isValid = false;
            }
        }

        if (cardEdtOTP.getVisibility() != View.GONE) {
            txtLoiOTP.setText("");
            String otp = edtOTP.getText().toString();
            String otpError = Validator.validateOTP(otp);
            if (otpError != null) {
                txtLoiOTP.setText(otpError);
                isValid = false;
            }
        }

        return isValid;
    }

    private void batDauDemNguoc() {

        hetHanOTP = false;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(120000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                long phut = millisUntilFinished / 60000;
                long giay = (millisUntilFinished % 60000) / 1000;

                txtOTP.setText(
                        String.format("Gửi (%02d:%02d)", phut, giay)
                );
            }

            @Override
            public void onFinish() {

                hetHanOTP = true;
                txtOTP.setText("Gửi lại");
            }
        };

        countDownTimer.start();
    }


    private void kiemTraOTP() {
        if (!validateInput()) {
            return;
        }

        LoadingDialog loading =
                new LoadingDialog(XacThucEmailActivity.this);
        loading.setMessage("Đang kiểm tra otp...");
        loading.show();

        String otpNhap = edtOTP.getText().toString().trim();

        repository.timTheoEmail(
                emailHienTai,
                new XacThucEmailRepository.OnFindListener() {

                    @Override
                    public void onFound(XacThucEmail xacThucEmail) {

                        long hienTai = System.currentTimeMillis();

                        if (hienTai - xacThucEmail.getNgayTao() > 120000) {

                            txtLoiOTP.setText(
                                    "Mã OTP đã hết hạn"
                            );
                            loading.dismiss();
                            return;
                        }

                        if (!otpNhap.equals(
                                String.valueOf(
                                        xacThucEmail.getMaOTP()
                                )
                        )) {

                            txtLoiOTP.setText(
                                    "Mã OTP không chính xác"
                            );
                            loading.dismiss();
                            return;
                        }

                        MessageHelper.success(
                                XacThucEmailActivity.this,
                                "Xác thực thành công"
                        );

                        repository.xoa(
                                emailHienTai,
                                new XacThucEmailRepository.OnCompleteListener() {

                                    @Override
                                    public void onSuccess() {

                                        // Chuyển màn hình hoặc finish()
                                        if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
                                            String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);
                                            if (activityType.toString().trim().equals(ActivityType.DOI_MAT_KHAU)) {

                                                Intent intent = new Intent(
                                                        XacThucEmailActivity.this,
                                                        DoiMatKhauActivity.class
                                                );
                                                intent.putExtra(IntentKey.EMAIL, emailHienTai);
                                                startActivity(intent);

                                                finish();
                                            }

                                            if (activityType.toString().trim().equals(ActivityType.DANG_KY)) {
                                                Intent intent = new Intent(
                                                        XacThucEmailActivity.this,
                                                        DangKyActivity.class
                                                );

                                                NguoiDung nguoiDung =
                                                        (NguoiDung) getIntent().getSerializableExtra(
                                                                IntentKey.NGUOI_DUNG
                                                        );
                                                intent.putExtra(
                                                        IntentKey.NGUOI_DUNG,
                                                        nguoiDung
                                                );

                                                startActivity(intent);
                                                finish();
                                            }


                                            if (activityType.toString().trim().equals(ActivityType.DANG_XUAT_TU_XA)) {
                                                Intent intent = new Intent(
                                                        XacThucEmailActivity.this,
                                                        SettingActivity.class
                                                );

                                                intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_XUAT_TU_XA);
                                                intent.putExtra(IntentKey.TEXT, "Xác thực email thành công!");

                                                startActivity(intent);
                                                finish();
                                            }


                                        }
                                    }

                                    @Override
                                    public void onFailure(String message) {

                                    }
                                }
                        );
                    }

                    @Override
                    public void onNotFound() {

                        txtLoiOTP.setText(
                                "Không tìm thấy mã OTP"
                        );
                    }

                    @Override
                    public void onFailure(String message) {

                        MessageHelper.error(
                                XacThucEmailActivity.this,
                                message
                        );
                    }
                }
        );
    }

    private void layEmailJsSuDungNhoNhat(String email, int otp) {
        EmailjsRepository emailjsRepository =
                new EmailjsRepository();


        emailjsRepository.layEmailJsSuDungNhoNhat(
                new EmailJsSelectCallback() {

                    @Override
                    public void onSuccess(
                            Emailjs emailjs,
                            String key
                    ) {
                        sendOTP(email, otp, emailjs.getServiceID(), emailjs.getTemplateID(), emailjs.getPublicKey(), key);

                    }

                    @Override
                    public void onFailure(
                            String message
                    ) {

                        MessageHelper.error(XacThucEmailActivity.this, message);
                    }
                }
        );

    }

    private void sendOTP(String email, int otp,
                         String service_id,
                         String template_id,
                         String user_id,
                         String key) {
        LoadingDialog loading =
                new LoadingDialog(XacThucEmailActivity.this);
        loading.setMessage("Đang gửi mã OTP...");
        loading.show();
        EmailjsRepository repository =
                new EmailjsRepository();

        repository.sendOTP(
                email,
                String.valueOf(otp),
                service_id,
                template_id,
                user_id,
                new EmailCallback() {

                    @Override
                    public void onSuccess() {

                        runOnUiThread(() -> {

                            MessageHelper.success(
                                    XacThucEmailActivity.this,
                                    "Đã gửi OTP"
                            );
                            repository.capNhatSuDung(
                                    key,
                                    new FirebaseCallback() {

                                        @Override
                                        public void onSuccess() {
                                            Log.d(
                                                    "EMAILJS",
                                                    "Cập nhật suDung thành công"
                                            );
                                        }

                                        @Override
                                        public void onFailure(
                                                String message
                                        ) {

                                            MessageHelper.error(XacThucEmailActivity.this, message);
                                        }
                                    }
                            );

                            showChucNangOTP();
                            batDauDemNguoc();
                            loading.dismiss();

                        });
                    }

                    @Override
                    public void onFailure(String error) {

                        runOnUiThread(() -> {

                            MessageHelper.success(
                                    XacThucEmailActivity.this,
                                    error
                            );

                            loading.dismiss();

                        });
                    }
                }
        );
    }

    private void kiemTraMang() {
        if (!NetworkHelper.isConnected(XacThucEmailActivity.this)) {

            MessageHelper.error(
                    XacThucEmailActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        if (NetworkHelper.isWifiConnected(XacThucEmailActivity.this)) {

            MessageHelper.info(
                    XacThucEmailActivity.this,
                    "Đang sử dụng WiFi"
            );

        }

        if (NetworkHelper.isMobileDataConnected(XacThucEmailActivity.this)) {

            MessageHelper.info(
                    XacThucEmailActivity.this,
                    "Đang sử dụng dữ liệu di động"
            );

        }
    }

    private void themEmailjs() {
        EmailjsRepository repository =
                new EmailjsRepository();

        repository.themEmailJs(
                "service_adkno1f",
                "template_fa9h6g6",
                "8UVhvzVFiJxKiR8oA",
                "numberfindinggame2@gmail.com",
                new FirebaseCallback() {

                    @Override
                    public void onSuccess() {

                        MessageHelper.success(
                                XacThucEmailActivity.this,
                                "Thêm EmailJS thành công"
                        );

                    }

                    @Override
                    public void onFailure(String error) {

                        MessageHelper.error(
                                XacThucEmailActivity.this,
                                error
                        );
                    }
                }
        );
    }


}