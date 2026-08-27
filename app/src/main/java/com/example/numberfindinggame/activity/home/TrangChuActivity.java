package com.example.numberfindinggame.activity.home;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.manchoi.ManChoiActivity;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.session.HieuUngSession;
import com.example.numberfindinggame.session.NhacHieuUngNenSession;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.ValueEventListener;

public class TrangChuActivity extends AppCompatActivity {
    private MaterialCardView cardCaiDat, cardThoat, cardTaiKhoan, cardMap;
    private ValueEventListener dangHoatDongListener;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private ImageView imgLogo;
    private HieuUngGlitchLayout layoutGlitch;
    private ConstraintLayout layoutLogo;
    private GlitchView viewNhieu;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Integer hieuUng = 4;
    private HieuUngSession hieuUngSession;
    private NhacHieuUngNenSession nhacHieuUngNenSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        dangHoatDongListener =
                ThietBiDangNhapHelper.kiemTraHoatDongThietBi(
                        this,
                        SessionManager.getUserId(this),
                        DeviceHelper.getDeviceId(TrangChuActivity.this)

                );

        setControl();
        setEvent();

    }

    private void setEvent() {
        nhacHieuUngNenSession = new NhacHieuUngNenSession(this);
        hieuUngSession = new HieuUngSession(this);
        // Nếu lần đầu tiên chưa có dữ liệu

        if (!hieuUngSession.isHieuUngExists()) {
            hieuUng = 4;
            // Lưu mặc định hiệu ứng 4
            hieuUngSession.setHieuUng(hieuUng);

        }
        hieuUng = hieuUngSession.getHieuUng();

        MusicManager.play(this);

        //Khởi tạo nhạc hiệu ứng 1 lần
        SoundManager.init(this);
        layCaiDat();
        layThongTinNguoiDung();

        if (hieuUng == 3 || hieuUng == 4) {
            SoundManager.playElectric(TrangChuActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }


        //Bật tắt nhạc trong Setting
        /*
       switchMusic.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {

            MusicManager.setEnabled(
                    this,
                    isChecked);

        });
        */
        /*Khi mở Activity:

        switchMusic.setChecked(
        MusicManager.isEnabled(this));
         */


        if (getIntent().hasExtra(IntentKey.TRUE)) {
            String text = getIntent().getStringExtra(IntentKey.TRUE);
            MessageHelper.success(
                    TrangChuActivity.this,
                    text
            );
        }

        if (getIntent().hasExtra(IntentKey.MA_KHOI_PHUC)) {

            new ConfirmDialog(
                    TrangChuActivity.this,
                    "Xác nhận",
                    "✅ Đăng nhập thành công! ⚠️ Mã khôi phục đã được làm mới, xem ở phần cài đặt !",
                    new ConfirmDialog.ConfirmCallback() {

                        @Override
                        public void onYes() {

                        }

                        @Override
                        public void onNo() {

                        }
                    }
            ).show();

        }


        cardCaiDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TrangChuActivity.this,
                        SettingActivity.class
                );

                SoundManager.playButton(TrangChuActivity.this);

                startActivity(intent);
                finish();
            }
        });

        cardTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TrangChuActivity.this,
                        ThongTinNguoiDungActivity.class
                );

                SoundManager.playButton(TrangChuActivity.this);

                startActivity(intent);
                finish();
            }
        });

        cardThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(TrangChuActivity.this);
                new ConfirmDialog(
                        TrangChuActivity.this,
                        "Xác nhận",
                        "Bạn có muốn thoát khỏi ứng dụng này không?",
                        new ConfirmDialog.ConfirmCallback() {

                            @Override
                            public void onYes() {
                                SoundManager.playButton(TrangChuActivity.this);
                                finish();
                            }

                            @Override
                            public void onNo() {
                                SoundManager.playButton(TrangChuActivity.this);
                            }
                        }
                ).show();
            }
        });

        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TrangChuActivity.this,
                        ManChoiActivity.class
                );

                SoundManager.playButton(TrangChuActivity.this);

                startActivity(intent);
                finish();
            }
        });
    }

    private void layThongTinNguoiDung() {
        if (!NetworkHelper.isConnected(TrangChuActivity.this)) {

            MessageHelper.error(
                    TrangChuActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(TrangChuActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                SessionManager.getUserId(TrangChuActivity.this),
                task -> {

                    if (task.isSuccessful() && task.getResult().exists()) {
                        NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);
                        try {
                            if (!nguoiDung.getHinhDaiDien().equals("")) {
                                byte[] byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhDaiDien());
                                imgLogo.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
                            } else {
                                imgLogo.setImageResource(R.drawable.avatar_default);
                            }
                        } catch (Exception exception) {
                            imgLogo.setImageResource(R.drawable.avatar_default);
                        }


                        loading.dismiss();


                    }
                }
        );
    }

    private void setControl() {

        cardCaiDat = findViewById(R.id.cardCaiDat);
        cardThoat = findViewById(R.id.cardThoat);
        cardTaiKhoan = findViewById(R.id.cardTaiKhoan);
        cardMap = findViewById(R.id.cardMap);

        imgLogo = findViewById(R.id.imgLogo);
        layoutLogo = findViewById(R.id.layoutLogo);
        layoutGlitch = findViewById(R.id.layoutGlitch);
        viewNhieu = findViewById(R.id.viewNhieu);

    }

    private void layCaiDat() {
        CaiDatRepository.layCaiDat(
                SessionManager.getUserId(this),
                new CaiDatCallback() {
                    @Override
                    public void onSuccess(CaiDat caiDat) {
                        Log.d("CAIDAT", caiDat.toString());

                        MusicManager.setVolume(
                                TrangChuActivity.this,
                                caiDat.getAmThanhNen());

                        SoundManager.setVolume(
                                TrangChuActivity.this,
                                caiDat.getAmThanhHieuUng());

//                        MusicManager.changeMusic(
//                                TrangChuActivity.this,
//                                R.raw.nhac_nen2);

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("CAIDAT", message);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ThietBiDangNhapHelper.stopTheoDoi(
                SessionManager.getUserId(this),
                DeviceHelper.getDeviceId(TrangChuActivity.this),
                dangHoatDongListener
        );
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
                SoundManager.playElectric(TrangChuActivity.this);
            }

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 7000);
            }
        }
    };

}