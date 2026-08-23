package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.google.android.material.card.MaterialCardView;

public class ChonXacThucActivity extends AppCompatActivity {

    private MaterialCardView cardXacThucEmail, cardMaKhoiPhuc, cardTiepTuc;
    private NguoiDung nguoiDung = new NguoiDung();
    private Boolean xacThucEmail = false;
    private Boolean maKhoiPhuc = false;

    private TextView txtQuayLai;
    private HieuUngGlitchLayout layoutGlitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chon_xac_thuc);

        setControl();
        setEvent();

    }

    private void setEvent() {

        layoutGlitch.postDelayed(() -> {

            layoutGlitch.batDauGlitch(900);

        }, 300);

        if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
            String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);
            if (activityType.toString().trim().equals(ActivityType.DANG_NHAP)) {
                nguoiDung =
                        (NguoiDung) getIntent().getSerializableExtra(
                                IntentKey.NGUOI_DUNG
                        );


                layCaiDat();
            }
        }


        cardXacThucEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardXacThucEmail.setStrokeWidth(dpToPx(1));
                cardMaKhoiPhuc.setStrokeWidth(dpToPx(0));
                xacThucEmail = true;
                maKhoiPhuc = false;

            }
        });

        cardMaKhoiPhuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardMaKhoiPhuc.setStrokeWidth(dpToPx(1));
                cardXacThucEmail.setStrokeWidth(dpToPx(0));
                xacThucEmail = false;
                maKhoiPhuc = true;
            }
        });

        cardTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xacThucEmail == true) {

                    // Chuyển màn hình
                    Intent intent = new Intent(
                            ChonXacThucActivity.this,
                            XacThucEmailActivity.class
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

                if (maKhoiPhuc == true) {

                    Intent intent = new Intent(
                            ChonXacThucActivity.this,
                            MaKhoiPhucActivity.class
                    );

                    intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_NHAP); // nếu cần
                    intent.putExtra(
                            IntentKey.NGUOI_DUNG,
                            nguoiDung
                    );

                    startActivity(intent);
                    finish();
                }

            }
        });

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ChonXacThucActivity.this,
                        DangNhapActivity.class
                );


                startActivity(intent);
                finish();
            }
        });

    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void layCaiDat() {
        CaiDatRepository.layCaiDat(
                nguoiDung.getMaNguoiDung(),
                new CaiDatCallback() {
                    @Override
                    public void onSuccess(CaiDat caiDat) {
                        if (caiDat.getXacThucEmail() == true) {
                            cardXacThucEmail.setVisibility(View.VISIBLE);
                        } else {
                            cardXacThucEmail.setVisibility(View.GONE);
                        }

                        if (caiDat.getMaKhoiPhuc() == true) {
                            cardMaKhoiPhuc.setVisibility(View.VISIBLE);
                        } else {
                            cardMaKhoiPhuc.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                        Log.e("CAIDAT", message);
                    }
                }
        );
    }

    private void setControl() {
        cardXacThucEmail = findViewById(R.id.cardXacThucEmail);
        cardMaKhoiPhuc = findViewById(R.id.cardMaKhoiPhuc);
        cardTiepTuc = findViewById(R.id.cardTiepTuc);

        txtQuayLai = findViewById(R.id.txtQuayLai);
        layoutGlitch = findViewById(R.id.layoutGlitch);
    }
}