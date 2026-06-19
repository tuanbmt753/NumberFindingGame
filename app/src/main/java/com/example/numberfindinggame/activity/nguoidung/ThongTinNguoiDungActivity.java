package com.example.numberfindinggame.activity.nguoidung;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangChuoi;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.VideoSize;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangKyActivity;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.BackgroundDialog;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.dialog.ConfirmDialogAnhNen;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.HinhAnhHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.example.numberfindinggame.utils.DateUtils;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThongTinNguoiDungActivity extends AppCompatActivity {

    private TextView txtQuayLai, txtTenNguoiDung, txtMaNguoiDung, txtEmail, txtUsername, txtNgayTao;
    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();

    private ImageView imgAvatar, imgHinhNen;
    private byte[] byteArrayHinh = new byte[0];
    private String maNguoiDung;
    private String themAnh = IntentKey.ANH_DAI_DIEN;

    private MaterialCardView cardDangXuat, cardDoiMatKhau;

    private ExoPlayer exoPlayer;
    private PlayerView playerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_nguoi_dung);

        setControl();
        setEvent();

    }

    private void setEvent() {

        if (getIntent().hasExtra(IntentKey.TEXT)) {
            String text = getIntent().getStringExtra(IntentKey.TEXT);

            MessageHelper.success(
                    ThongTinNguoiDungActivity.this,
                    text
            );

        }

        maNguoiDung = SessionManager.getUserId(ThongTinNguoiDungActivity.this);
        ThietBiDangNhapHelper.kiemTraThietBiDangNhap(
                this,
                SessionManager.getUserId(this),
                DeviceHelper.getDeviceId(ThongTinNguoiDungActivity.this)

        );
        layThongTinNguoiDung();

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ThongTinNguoiDungActivity.this,
                        TrangChuActivity.class
                );
                SoundManager.playButton(ThongTinNguoiDungActivity.this);
                startActivity(intent);
                finish();

            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themAnh = IntentKey.ANH_DAI_DIEN;
                SoundManager.playButton(ThongTinNguoiDungActivity.this);

                //Mở thư viên
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);


            }
        });

        imgHinhNen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ConfirmDialogAnhNen(
                        ThongTinNguoiDungActivity.this,
                        "Xác nhận",
                        "Bạn muốn chọn loại ảnh gì làm ảnh nền? ",
                        new ConfirmDialogAnhNen.ConfirmCallback() {
                            @Override
                            public void onNenTinh() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);

                                imgHinhNen.setVisibility(View.VISIBLE);
                                playerView.setVisibility(View.GONE);

                                themAnh = IntentKey.ANH_NEN;
                                //Mở thư viên
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 1);
                            }

                            @Override
                            public void onNenDong() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);
                                themAnh = IntentKey.ANH_NEN_DONG;

                                imgHinhNen.setVisibility(View.GONE);
                                playerView.setVisibility(View.VISIBLE);

                                new BackgroundDialog(

                                        ThongTinNguoiDungActivity.this,

                                        item -> {

                                            MessageHelper.success(ThongTinNguoiDungActivity.this, "" + item.getName());
                                            int resId = item.getResId();
                                            phatVideoNen(resId);
                                            capNhapHinhNen("" + resId);


                                        }

                                ).show();

                            }

                            @Override
                            public void onNo() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);

                            }
                        }
                ).show();
            }
        });

        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ConfirmDialogAnhNen(
                        ThongTinNguoiDungActivity.this,
                        "Xác nhận",
                        "Bạn muốn chọn loại ảnh gì làm ảnh nền? ",
                        new ConfirmDialogAnhNen.ConfirmCallback() {
                            @Override
                            public void onNenTinh() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);

                                imgHinhNen.setVisibility(View.VISIBLE);
                                playerView.setVisibility(View.GONE);

                                themAnh = IntentKey.ANH_NEN;
                                //Mở thư viên
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 1);
                            }

                            @Override
                            public void onNenDong() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);
                                themAnh = IntentKey.ANH_NEN_DONG;

                                imgHinhNen.setVisibility(View.GONE);
                                playerView.setVisibility(View.VISIBLE);

                                new BackgroundDialog(

                                        ThongTinNguoiDungActivity.this,

                                        item -> {

                                            MessageHelper.success(ThongTinNguoiDungActivity.this, "" + item.getName());
                                            int resId = item.getResId();
                                            phatVideoNen(resId);
                                            capNhapHinhNen("" + resId);


                                        }

                                ).show();

                            }

                            @Override
                            public void onNo() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);

                            }
                        }
                ).show();
            }
        });


        cardDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ConfirmDialog(
                        ThongTinNguoiDungActivity.this,
                        "Xác nhận",
                        "⚠️ Bạn có muốn đăng xuất tài khoản này? ",
                        new ConfirmDialog.ConfirmCallback() {

                            @Override
                            public void onYes() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);
                                Intent intent = new Intent(
                                        ThongTinNguoiDungActivity.this,
                                        DangNhapActivity.class
                                );


                                SessionManager.logout(ThongTinNguoiDungActivity.this);

                                ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();
                                thietBiDangNhapRepository.voHieuHoaThietBi(
                                        SessionManager.getUserId(ThongTinNguoiDungActivity.this),
                                        DeviceHelper.getDeviceId(ThongTinNguoiDungActivity.this),
                                        task -> {

                                            if (task.isSuccessful()) {
                                            }


                                        }
                                );

                                //intent.putExtra(IntentKey.TEXT, "Đăng xuất tài khoản thành công!"); // nếu cần
                                intent.putExtra(IntentKey.TRUE, "Đăng xuất tài khoản thành công!");
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onNo() {
                                SoundManager.playButton(ThongTinNguoiDungActivity.this);
                            }
                        }
                ).show();


            }
        });

        cardDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nguoiDungRepository.layEmailTheoMaNguoiDung(
                        SessionManager.getUserId(ThongTinNguoiDungActivity.this),
                        new NguoiDungRepository.OnGetEmailListener() {

                            @Override
                            public void onSuccess(String email) {

                                if (email != null) {
                                    new ConfirmDialog(
                                            ThongTinNguoiDungActivity.this,
                                            "Xác nhận",
                                            "Bạn phải xác thực email " + email + " để có thể đổi mật khẩu ? ",
                                            new ConfirmDialog.ConfirmCallback() {

                                                @Override
                                                public void onYes() {
                                                    Intent intent = new Intent(ThongTinNguoiDungActivity.this, XacThucEmailActivity.class);
                                                    intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.THONG_TIN_NGUOI_DUNG);
                                                    intent.putExtra(IntentKey.EMAIL, email);
                                                    SoundManager.playButton(ThongTinNguoiDungActivity.this);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onNo() {
                                                    SoundManager.playButton(ThongTinNguoiDungActivity.this);
                                                }
                                            }
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailed(String message) {
                                MessageHelper.error(ThongTinNguoiDungActivity.this, "" + message);
                            }
                        }
                );
            }
        });

    }

    public static boolean isInteger(String s) {

        if (s == null || s.trim().isEmpty()) {

            return false;

        }

        try {

            Integer.parseInt(s);

            return true;

        } catch (NumberFormatException e) {

            return false;

        }

    }

    private void layThongTinNguoiDung() {
        if (!NetworkHelper.isConnected(ThongTinNguoiDungActivity.this)) {

            MessageHelper.error(
                    ThongTinNguoiDungActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(ThongTinNguoiDungActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                SessionManager.getUserId(ThongTinNguoiDungActivity.this),
                task -> {

                    if (task.isSuccessful() && task.getResult().exists()) {
                        NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);

                        txtTenNguoiDung
                                .setText("" + nguoiDung.getTenNguoiDung());
                        txtMaNguoiDung
                                .setText("\uD83C\uDD94 ID: " + nguoiDung.getMaNguoiDung());
                        txtEmail
                                .setText("✉\uFE0F Email: " + nguoiDung.getEmail());
                        txtUsername
                                .setText("\uD83D\uDC64 Username: " + nguoiDung.getTenNguoiDung());
                        txtNgayTao
                                .setText("\uD83D\uDCC5 Ngày tạo: " + DateUtils.format(nguoiDung.getNgayTao()));

                        try {

                            if (!nguoiDung.getHinhDaiDien().isEmpty()) {
                                byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhDaiDien());
                                imgAvatar.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
                            } else {
                                imgAvatar.setImageResource(R.drawable.avatar_default);
                            }

                        } catch (Exception exception) {
                            imgAvatar.setImageResource(R.drawable.avatar_default);
                        }

                        if (isInteger(nguoiDung.getHinhNen()) == false) {

                            try {

                                if (!nguoiDung.getHinhNen().isEmpty()) {
                                    byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhNen());
                                    imgHinhNen.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
                                } else {
                                    imgHinhNen.setImageResource(R.drawable.background_profile);
                                }

                            } catch (Exception exception) {
                                imgHinhNen.setImageResource(R.drawable.background_profile);
                            }

                            imgHinhNen.setVisibility(View.VISIBLE);
                            playerView.setVisibility(View.GONE);
                        } else {

                            phatVideoNen(Integer.parseInt(nguoiDung.getHinhNen()));
                            imgHinhNen.setVisibility(View.GONE);
                            playerView.setVisibility(View.VISIBLE);
                        }


                        loading.dismiss();


                    }
                }
        );
    }

    private void phatVideoNen(int redID) {
        exoPlayer = new ExoPlayer.Builder(this).build();

        playerView.setPlayer(exoPlayer);

        Uri uri = Uri.parse(
                "android.resource://"
                        + getPackageName()
                        + "/"
                        + redID);

        MediaItem mediaItem =
                MediaItem.fromUri(uri);

        exoPlayer.setMediaItem(mediaItem);

        // lặp vô hạn
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

        // tắt tiếng nếu muốn
        exoPlayer.setVolume(0f);

        exoPlayer.prepare();

        exoPlayer.play();

        VideoSize size = exoPlayer.getVideoSize();

        int width = size.width;
        int height = size.height;

        float ratio = (float) height / width;

        ViewGroup.LayoutParams params =
                playerView.getLayoutParams();

        params.height =
                (int) (playerView.getWidth() * ratio);

        playerView.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (exoPlayer != null) {

            exoPlayer.release();

            exoPlayer = null;
        }
    }

    private void setControl() {
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtTenNguoiDung = findViewById(R.id.txtTenNguoiDung);
        txtMaNguoiDung = findViewById(R.id.txtMaNguoiDung);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtNgayTao = findViewById(R.id.txtNgayTao);

        imgAvatar = findViewById(R.id.imgAvatar);
        imgHinhNen = findViewById(R.id.imgHinhNen);

        cardDangXuat = findViewById(R.id.cardDangXuat);
        cardDoiMatKhau = findViewById(R.id.cardDoiMatKhau);

        playerView = findViewById(R.id.playerView);

    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data);

        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null) {

            Uri uri = data.getData();

            try {

                // Đọc ảnh an toàn
                Bitmap bitmap =
                        HinhAnhHelper.decodeSampledBitmapFromUri(
                                this,
                                uri,
                                512,
                                512
                        );

                // Resize giữ tỉ lệ
                Bitmap resizedBitmap =
                        HinhAnhHelper.resizeBitmap(
                                bitmap,
                                512
                        );

                // Hiển thị avatar
                if (themAnh.equals(IntentKey.ANH_DAI_DIEN)) {
                    imgAvatar.setImageBitmap(
                            resizedBitmap);
                }
                if (themAnh.equals(IntentKey.ANH_NEN)) {
                    imgHinhNen.setImageBitmap(
                            resizedBitmap);
                }

                // Chuyển thành byte[]
                byteArrayHinh =
                        HinhAnhHelper.compressToMaxSize(
                                resizedBitmap,
                                1
                        );

                Log.d(
                        "Avatar",
                        "Size = "
                                + byteArrayHinh.length / 1024
                                + " KB"
                );

                if (themAnh.equals(IntentKey.ANH_DAI_DIEN)) {
                    capNhapAnhDaiDien(chuyenByteSangChuoi(byteArrayHinh));
                }
                if (themAnh.equals(IntentKey.ANH_NEN)) {
                    capNhapHinhNen(chuyenByteSangChuoi(byteArrayHinh));
                }


            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    private void capNhapHinhNen(String hinhNen) {
        new ConfirmDialog(
                ThongTinNguoiDungActivity.this,
                "Xác nhận",
                "⚠️ Bạn có muốn lấy ảnh này làm ảnh nền? ",
                new ConfirmDialog.ConfirmCallback() {

                    @Override
                    public void onYes() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);

                        nguoiDungRepository.capNhatHinhNen(

                                maNguoiDung,
                                hinhNen,

                                new NguoiDungRepository.OnUpdateListener() {

                                    @Override
                                    public void onSuccess() {

                                        MessageHelper.success(ThongTinNguoiDungActivity.this, "Cập nhật thành công");

                                    }

                                    @Override
                                    public void onFailed(String message) {

                                        MessageHelper.error(ThongTinNguoiDungActivity.this, "" + message);

                                    }

                                }

                        );
                    }

                    @Override
                    public void onNo() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);
                        layThongTinNguoiDung();
                    }
                }
        ).show();
    }


    private void capNhapAnhDaiDien(String maHoaHinhAnh) {

        new ConfirmDialog(
                ThongTinNguoiDungActivity.this,
                "Xác nhận",
                "⚠️ Bạn có muốn lấy ảnh này làm ảnh đại diện? ",
                new ConfirmDialog.ConfirmCallback() {

                    @Override
                    public void onYes() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);

                        nguoiDungRepository.capNhatHinhDaiDien(

                                maNguoiDung,
                                maHoaHinhAnh,

                                new NguoiDungRepository.OnUpdateListener() {

                                    @Override
                                    public void onSuccess() {

                                        MessageHelper.success(ThongTinNguoiDungActivity.this, "Cập nhật thành công");

                                    }

                                    @Override
                                    public void onFailed(String message) {

                                        MessageHelper.error(ThongTinNguoiDungActivity.this, "" + message);

                                    }

                                }

                        );
                    }

                    @Override
                    public void onNo() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);
                        layThongTinNguoiDung();
                    }
                }
        ).show();
    }
}