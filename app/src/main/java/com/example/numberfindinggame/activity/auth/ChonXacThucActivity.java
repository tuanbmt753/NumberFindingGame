package com.example.numberfindinggame.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.example.numberfindinggame.session.HieuUngSession;
import com.google.android.material.card.MaterialCardView;

public class ChonXacThucActivity extends AppCompatActivity {

    private MaterialCardView cardXacThucEmail, cardMaKhoiPhuc, cardTiepTuc;
    private NguoiDung nguoiDung = new NguoiDung();
    private Boolean xacThucEmail = false;
    private Boolean maKhoiPhuc = false;

    private TextView txtQuayLai;
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
        setContentView(R.layout.activity_chon_xac_thuc);

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
            SoundManager.playElectric(ChonXacThucActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }

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
        layoutLogo = findViewById(R.id.layoutLogo);
        viewNhieu = findViewById(R.id.viewNhieu);
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
            SoundManager.playElectric(ChonXacThucActivity.this);

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 15000);
            }
        }
    };
}