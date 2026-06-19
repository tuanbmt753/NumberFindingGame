package com.example.numberfindinggame.activity.home;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangKyActivity;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.MusicManager;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.example.numberfindinggame.utils.DateUtils;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.ValueEventListener;

public class TrangChuActivity extends AppCompatActivity {
    private MaterialCardView cardCaiDat, cardThoat, cardTaiKhoan;
    private ValueEventListener dangHoatDongListener;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private ImageView imgLogo;

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

        getControl();
        getView();

    }

    private void getView() {
        MusicManager.play(this);
        //Khởi tạo nhạc hiệu ứng 1 lần
        SoundManager.init(this);
        layCaiDat();
        layThongTinNguoiDung();
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

    private void getControl() {

        cardCaiDat = findViewById(R.id.cardCaiDat);
        cardThoat = findViewById(R.id.cardThoat);
        cardTaiKhoan = findViewById(R.id.cardTaiKhoan);

        imgLogo = findViewById(R.id.imgLogo);

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
    }


}