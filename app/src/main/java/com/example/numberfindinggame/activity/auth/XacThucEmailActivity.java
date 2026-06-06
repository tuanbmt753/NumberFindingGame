package com.example.numberfindinggame.activity.auth;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.model.XacThucEmail;
import com.example.numberfindinggame.repository.XacThucEmailRepository;
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

        txtNoiDung.setText("Xác thực email");
        txtLoiEmail.setText("");
        txtLoiOTP.setText("");

        showChucNangEmail();

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cardEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                if (!validateInput()) {
                    return;
                }

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

                                MessageHelper.success(
                                        XacThucEmailActivity.this,
                                        "Tạo OTP thành công"
                                );

                                emailHienTai = email;
                                showChucNangOTP();
                                batDauDemNguoc();

                            }

                            @Override
                            public void onFailure(String message) {

                                MessageHelper.error(
                                        XacThucEmailActivity.this,
                                        "message"
                                );
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


}