package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.model.MaKhoiPhuc;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.MaKhoiPhucRepository;
import com.example.numberfindinggame.utils.Validator;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MaKhoiPhucActivity extends AppCompatActivity {
    private TextView txtQuayLai, txtLoiMaKhoiPhuc, txtNoiDung;
    private EditText edtMaKhoiPhuc;

    private MaterialCardView cardXacNhan;

    private NguoiDung nguoiDung = new NguoiDung();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ma_khoi_phuc);

        setControl();
        setEvent();

    }

    private void setEvent() {

        if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
            String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);
            if (activityType.toString().trim().equals(ActivityType.DANG_NHAP)) {
                nguoiDung =
                        (NguoiDung) getIntent().getSerializableExtra(
                                IntentKey.NGUOI_DUNG
                        );
            }
        }

        txtLoiMaKhoiPhuc.setText("");
        txtNoiDung.setText("Nhập mã khôi phục");

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        MaKhoiPhucActivity.this,
                        ChonXacThucActivity.class
                );

                NguoiDung nguoiDung =
                        (NguoiDung) getIntent().getSerializableExtra(
                                IntentKey.NGUOI_DUNG
                        );
                intent.putExtra(IntentKey.EMAIL, nguoiDung.getEmail());
                intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_NHAP); // nếu cần
                intent.putExtra(
                        IntentKey.NGUOI_DUNG,
                        nguoiDung
                );


                startActivity(intent);
                finish();
            }
        });

        cardXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkHelper.isConnected(MaKhoiPhucActivity.this)) {

                    MessageHelper.error(
                            MaKhoiPhucActivity.this,
                            "Không có kết nối Internet"
                    );

                    return;
                }

                if (!validateInput()) {
                    return;
                }

                layMaKhoiPhuc();
            }
        });

        edtMaKhoiPhuc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLoiMaKhoiPhuc.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void layMaKhoiPhuc() {

        if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
            String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);
            if (activityType.toString().trim().equals(ActivityType.DANG_NHAP)) {
                MaKhoiPhucRepository.layMaKhoiPhuc(
                        ((NguoiDung) getIntent().getSerializableExtra(
                                IntentKey.NGUOI_DUNG
                        )).getMaNguoiDung(),
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                if (snapshot.exists()) {

                                    MaKhoiPhuc maKhoiPhuc = snapshot.getValue(MaKhoiPhuc.class);

                                    int ma = maKhoiPhuc.getMaKhoiPhuc();
                                    if (edtMaKhoiPhuc.getText().toString().trim().equals(String.valueOf(ma).toString().trim())) {

                                        themMaKhoiPhuc(((NguoiDung) getIntent().getSerializableExtra(
                                                IntentKey.NGUOI_DUNG
                                        )).getMaNguoiDung());

                                        Intent intent = new Intent(
                                                MaKhoiPhucActivity.this,
                                                DangNhapActivity.class
                                        );

                                        intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_NHAP);
                                        intent.putExtra(IntentKey.MA_KHOI_PHUC, IntentKey.MA_KHOI_PHUC);
                                        intent.putExtra(IntentKey.TEXT, "Sử dụng mã khôi phục thành công!");
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

                                    } else {
                                        MessageHelper.error(MaKhoiPhucActivity.this, "Mã khôi phục không chính xác");
                                        txtLoiMaKhoiPhuc.setText("Mã khôi phục không chính xác");
                                    }
                                    Log.d("TEST", "" + ma);


                                } else {

                                    Log.d(
                                            "TEST",
                                            "Không tìm thấy mã khôi phục"
                                    );
                                }

                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                Log.d(
                                        "TEST",
                                        error.getMessage()
                                );

                            }

                        }
                );
            }
        }
    }

    private void themMaKhoiPhuc(String maNguoiDung) {
        int maKhoiPhuc = 100000 + new Random().nextInt(900000);

        MaKhoiPhuc ma = new MaKhoiPhuc();

        ma.setMaNguoiDung(maNguoiDung);
        ma.setMaKhoiPhuc((maKhoiPhuc));
        ma.setNgayTao(System.currentTimeMillis());

        MaKhoiPhucRepository.themMaKhoiPhuc(
                ma,
                task -> {

                    if (task.isSuccessful()) {

                        MessageHelper.success(MaKhoiPhucActivity.this, "Lưu mã thành công");

                    } else {

                        MessageHelper.success(MaKhoiPhucActivity.this, "Lỗi: " + task.getException().getMessage());

                    }

                }
        );
    }


    private void setControl() {
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtLoiMaKhoiPhuc = findViewById(R.id.txtLoiMaKhoiPhuc);
        txtNoiDung = findViewById(R.id.txtNoiDung);

        edtMaKhoiPhuc = findViewById(R.id.edtMaKhoiPhuc);

        cardXacNhan = findViewById(R.id.cardXacNhan);
    }

    private boolean validateInput() {
        boolean isValid = true;

        txtLoiMaKhoiPhuc.setText("");
        String maKhoiPhuc = edtMaKhoiPhuc.getText().toString();
        String maKhoiPhucError = Validator.validateMaKhoiPhuc(maKhoiPhuc);
        if (maKhoiPhucError != null) {
            txtLoiMaKhoiPhuc.setText(maKhoiPhucError);
            isValid = false;
        }

        return isValid;
    }
}