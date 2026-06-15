package com.example.numberfindinggame.activity.setting;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.adapter.ThietBiDangNhapAdapter;
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.constant.MusicType;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.MusicManager;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SessionManagerSetting;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.MaKhoiPhuc;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.example.numberfindinggame.repository.MaKhoiPhucRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class SettingActivity extends AppCompatActivity {

    private ListView lvThietBiDangNhap;
    private ArrayList<ThietBiDangNhap> dsThietBiDangNhap = new ArrayList<>();
    private ThietBiDangNhapAdapter thietBiDangNhapAdapter;

    private ValueEventListener dsThietBiListener;

    private ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();

    private TextView txtQuayLai, txtXacThucEmailDongMo, txtMaKhoiPhucDongMo;
    private SeekBar seekBarBackground, seekBarEffect;

    private MaterialCardView cardXacThucEmailDongMo, cardMaKhoiPhucDongMo, cardNhacNen1, cardNhacNen2;

    private MaterialCardView cardAmThanhHieuUng3, cardAmThanhHieuUng1, cardAmThanhHieuUng2;
    private NguoiDungRepository repository = new NguoiDungRepository();

    private CaiDat caiDat;

    private ImageView imgQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        setControl();
        setEvent();
    }

    private void setEvent() {

        thietBiDangNhapAdapter = new ThietBiDangNhapAdapter(this, dsThietBiDangNhap);
        lvThietBiDangNhap.setAdapter(thietBiDangNhapAdapter);
        khoiTao();

        String setting = SessionManagerSetting.getSetting(SettingActivity.this);
        if (setting != null && !setting.isEmpty()) {
            if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
                String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);
                if (activityType.equals(ActivityType.DANG_XUAT_TU_XA)) {
                    String text = getIntent().getStringExtra(IntentKey.TEXT);
                    MessageHelper.success(SettingActivity.this, "" + text);

                    new ConfirmDialog(
                            SettingActivity.this,
                            "Xác nhận",
                            "✅ " + text + " .⚠️ Hành động này chỉ có tác dụng 10 phút ở màn hình Setting, khi hết 10 phút bạn sẽ cần phải xác thực lại để có thể thực hiện các hành động như cài đặt xác thực, đăng xuất từ xa, và xóa thiết bị, ...! ",
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
            }
        }

        if (MusicManager.getCurrentMusic(SettingActivity.this) == MusicType.DEFAULT) {
            cardNhacNen1.setStrokeWidth(dpToPx(1));
            cardNhacNen2.setStrokeWidth(dpToPx(0));

        }

        if (MusicManager.getCurrentMusic(SettingActivity.this) == MusicType.MUSIC_2) {
            cardNhacNen1.setStrokeWidth(dpToPx(0));
            cardNhacNen2.setStrokeWidth(dpToPx(1));
        }

        cardNhacNen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.changeMusic(
                        SettingActivity.this,
                        MusicType.DEFAULT);

                cardNhacNen1.setStrokeWidth(dpToPx(1));
                cardNhacNen2.setStrokeWidth(dpToPx(0));
            }
        });

        cardNhacNen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicManager.changeMusic(
                        SettingActivity.this,
                        MusicType.MUSIC_2);

                cardNhacNen1.setStrokeWidth(dpToPx(0));
                cardNhacNen2.setStrokeWidth(dpToPx(1));
            }
        });

        if (SoundManager.getCurrentSound(SettingActivity.this) == R.raw.click_3) {
            cardAmThanhHieuUng3.setStrokeWidth(dpToPx(1));
            cardAmThanhHieuUng1.setStrokeWidth(dpToPx(0));
            cardAmThanhHieuUng2.setStrokeWidth(dpToPx(0));
        }

        if (SoundManager.getCurrentSound(SettingActivity.this) == R.raw.click_1) {
            cardAmThanhHieuUng3.setStrokeWidth(dpToPx(0));
            cardAmThanhHieuUng1.setStrokeWidth(dpToPx(1));
            cardAmThanhHieuUng2.setStrokeWidth(dpToPx(0));
        }

        if (SoundManager.getCurrentSound(SettingActivity.this) == R.raw.click_2) {
            cardAmThanhHieuUng3.setStrokeWidth(dpToPx(0));
            cardAmThanhHieuUng1.setStrokeWidth(dpToPx(0));
            cardAmThanhHieuUng2.setStrokeWidth(dpToPx(1));
        }

        cardAmThanhHieuUng1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.changeButtonSound(
                        SettingActivity.this,
                        R.raw.click_1);
                SoundManager.playButton(SettingActivity.this);

                cardAmThanhHieuUng3.setStrokeWidth(dpToPx(0));
                cardAmThanhHieuUng1.setStrokeWidth(dpToPx(1));
                cardAmThanhHieuUng2.setStrokeWidth(dpToPx(0));
            }
        });

        cardAmThanhHieuUng2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.changeButtonSound(
                        SettingActivity.this,
                        R.raw.click_2);
                SoundManager.playButton(SettingActivity.this);

                cardAmThanhHieuUng3.setStrokeWidth(dpToPx(0));
                cardAmThanhHieuUng1.setStrokeWidth(dpToPx(0));
                cardAmThanhHieuUng2.setStrokeWidth(dpToPx(1));

            }
        });

        cardAmThanhHieuUng3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.changeButtonSound(
                        SettingActivity.this,
                        R.raw.click_3);
                SoundManager.playButton(SettingActivity.this);

                cardAmThanhHieuUng3.setStrokeWidth(dpToPx(1));
                cardAmThanhHieuUng1.setStrokeWidth(dpToPx(0));
                cardAmThanhHieuUng2.setStrokeWidth(dpToPx(0));
            }
        });


        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        SettingActivity.this,
                        TrangChuActivity.class
                );
                startActivity(intent);
                finish();

            }
        });

        cardMaKhoiPhucDongMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setting = SessionManagerSetting.getSetting(SettingActivity.this);
                if (setting != null && !setting.isEmpty()) {
                    luuCaiDat(0, 1, 0, 0);
                } else {

                    repository.layEmailTheoMaNguoiDung(
                            SessionManager.getUserId(SettingActivity.this),
                            new NguoiDungRepository.OnGetEmailListener() {

                                @Override
                                public void onSuccess(String email) {

                                    if (email != null) {
                                        new ConfirmDialog(
                                                SettingActivity.this,
                                                "Xác nhận",
                                                "Bạn phải xác thực email " + email + " để có thể mở xác thực bằng mã khôi phục mỗi khi đăng nhập ? ",
                                                new ConfirmDialog.ConfirmCallback() {

                                                    @Override
                                                    public void onYes() {
                                                        Intent intent = new Intent(SettingActivity.this, XacThucEmailActivity.class);
                                                        intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_XUAT_TU_XA);
                                                        intent.putExtra(IntentKey.EMAIL, email);

                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onNo() {

                                                    }
                                                }
                                        ).show();
                                    }
                                }

                                @Override
                                public void onFailed(String message) {
                                    MessageHelper.error(SettingActivity.this, "" + message);
                                }
                            }
                    );

                }
            }
        });

        seekBarBackground.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {


                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {

                        // Bắt đầu kéo
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {

                        luuCaiDat(0, 0, 1, 0);
                    }
                }
        );

        seekBarEffect.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {


                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {

                        // Bắt đầu kéo
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {

                        luuCaiDat(0, 0, 0, 1);
                    }
                }
        );

        cardXacThucEmailDongMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String setting = SessionManagerSetting.getSetting(SettingActivity.this);
                if (setting != null && !setting.isEmpty()) {
                    luuCaiDat(1, 0, 0, 0);
                } else {

                    repository.layEmailTheoMaNguoiDung(
                            SessionManager.getUserId(SettingActivity.this),
                            new NguoiDungRepository.OnGetEmailListener() {

                                @Override
                                public void onSuccess(String email) {

                                    if (email != null) {
                                        new ConfirmDialog(
                                                SettingActivity.this,
                                                "Xác nhận",
                                                "Bạn phải xác thực email " + email + " để có thể mở xác thực email mỗi khi đăng nhập?",
                                                new ConfirmDialog.ConfirmCallback() {

                                                    @Override
                                                    public void onYes() {
                                                        Intent intent = new Intent(SettingActivity.this, XacThucEmailActivity.class);
                                                        intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_XUAT_TU_XA);
                                                        intent.putExtra(IntentKey.EMAIL, email);

                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onNo() {

                                                    }
                                                }
                                        ).show();
                                    }
                                }

                                @Override
                                public void onFailed(String message) {
                                    MessageHelper.error(SettingActivity.this, "" + message);
                                }
                            }
                    );

                }

            }
        });

        imgQR.setOnClickListener(v -> {

            new ConfirmDialog(
                    SettingActivity.this,
                    "Xác nhận",
                    "Bạn có muốn lưu ảnh mã khôi phục gốc để dùng sau này không?",
                    new ConfirmDialog.ConfirmCallback() {

                        @Override
                        public void onYes() {
                            imgQR.setDrawingCacheEnabled(true);

                            Bitmap bitmap =
                                    Bitmap.createBitmap(
                                            imgQR.getDrawingCache()
                                    );

                            imgQR.setDrawingCacheEnabled(false);

                            luuAnh(bitmap);
                        }

                        @Override
                        public void onNo() {

                        }
                    }
            ).show();
        });

    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void luuAnh(Bitmap bitmap) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            // Android 10+

            ContentValues values = new ContentValues();

            values.put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "QR_" + System.currentTimeMillis() + ".png"
            );

            values.put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/png"
            );

            values.put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES
                            + "/NumberFindingGame"
            );

            Uri uri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
            );

            try {

                OutputStream os =
                        getContentResolver()
                                .openOutputStream(uri);

                bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        os
                );

                os.close();

                MessageHelper.success(SettingActivity.this, "Đã lưu ảnh ở đường dẫn bộ nhớ trong ../Pictures/NumberFindingGame/OR...png" + uri);

            } catch (Exception e) {

                e.printStackTrace();

            }

        } else {

            // Android 9 trở xuống

            File folder = new File(
                    Environment
                            .getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES
                            ),
                    "NumberFindingGame"
            );

            if (!folder.exists()) {

                folder.mkdirs();

            }

            File file = new File(
                    folder,
                    "QR_"
                            + System.currentTimeMillis()
                            + ".png"
            );

            try {

                FileOutputStream fos =
                        new FileOutputStream(file);

                bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        fos);

                fos.flush();

                fos.close();

                MediaScannerConnection.scanFile(
                        this,
                        new String[]{
                                file.getAbsolutePath()
                        },
                        null,
                        null
                );

                MessageHelper.success(SettingActivity.this, "Đã lưu ảnh ở đường dẫn bộ nhớ trong ../Pictures/NumberFindingGame/OR...png");

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    private void luuCaiDat(int xacThuc, int maKhoiPhuc, int amThanhNen, int amThanhHieuUng) {
        CaiDatRepository.layCaiDat(
                SessionManager.getUserId(this),
                new CaiDatCallback() {
                    @Override
                    public void onSuccess(CaiDat caiDat) {

                        if (!NetworkHelper.isConnected(SettingActivity.this)) {

                            MessageHelper.error(
                                    SettingActivity.this,
                                    "Không có kết nối Internet"
                            );

                            return;
                        }

                        CaiDat caiDat2 = new CaiDat(caiDat);

                        Log.d("CAIDAT", caiDat.toString());

                        if (maKhoiPhuc > 0) {
                            if (caiDat.getMaKhoiPhuc() == true) {
                                caiDat2.setMaKhoiPhuc(false);
                            } else {
                                caiDat2.setMaKhoiPhuc(true);
                                layMaKhoiPhuc();
                            }
                        }

                        if (xacThuc > 0) {
                            if (caiDat.getXacThucEmail() == true) {
                                caiDat2.setXacThucEmail(false);
                                caiDat2.setMaKhoiPhuc(false);
                            } else {
                                caiDat2.setXacThucEmail(true);
                            }
                        }

                        if (amThanhNen > 0) {
                            caiDat2.setAmThanhNen(seekBarBackground.getProgress());
                        }
                        if (amThanhHieuUng > 0) {
                            caiDat2.setAmThanhHieuUng(seekBarEffect.getProgress());
                        }

                        CaiDatRepository.luuCaiDat(
                                caiDat2,
                                task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("CAIDAT", "Lưu thành công");
                                    } else {
                                        Log.d("CAIDAT", "Lưu thất bại");
                                    }
                                }
                        );

                        layCaiDat();


                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("CAIDAT", message);
                    }
                }
        );


    }

    private void layMaKhoiPhuc() {
        MaKhoiPhucRepository.layMaKhoiPhuc(
                SessionManager.getUserId(this),
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot
                    ) {

                        if (snapshot.exists()) {

                            MaKhoiPhuc maKhoiPhuc = snapshot.getValue(MaKhoiPhuc.class);

                            int ma = maKhoiPhuc.getMaKhoiPhuc();
                            long ngayTao = maKhoiPhuc.getNgayTao();


                            Log.d("TEST", "" + ma);

                        } else {

                            Log.d(
                                    "TEST",
                                    "Không tìm thấy mã khôi phục"
                            );

                            themMaKhoiPhuc();
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

    private void themMaKhoiPhuc() {

        String maNguoiDung = SessionManager.getUserId(this);
        int maKhoiPhuc = 100000 + new Random().nextInt(900000);

        MaKhoiPhuc ma = new MaKhoiPhuc();

        ma.setMaNguoiDung(maNguoiDung);
        ma.setMaKhoiPhuc((maKhoiPhuc));
        ma.setNgayTao(System.currentTimeMillis());

        MaKhoiPhucRepository.themMaKhoiPhuc(
                ma,
                task -> {

                    if (task.isSuccessful()) {

                        MessageHelper.success(SettingActivity.this, "Lưu mã thành công");

                    } else {

                        MessageHelper.success(SettingActivity.this, "Lỗi: " + task.getException().getMessage());

                    }

                }
        );
    }

    private void khoiTao() {
        //luuThietBi(SessionManager.getUserId(this));
        dsThietBiListener =
                ThietBiDangNhapHelper.theoDoiDanhSachThietBi(
                        SessionManager.getUserId(this),
                        danhSach -> {

                            dsThietBiDangNhap.clear();
                            dsThietBiDangNhap.addAll(danhSach);

                            thietBiDangNhapAdapter.notifyDataSetChanged();

                            ListViewHelper
                                    .setListViewHeightBasedOnChildren(
                                            lvThietBiDangNhap
                                    );
                        }
                );

        taoCaiDatMatDinh();
        layCaiDat();
    }

    private void layCaiDat() {
        CaiDatRepository.layCaiDat(
                SessionManager.getUserId(this),
                new CaiDatCallback() {
                    @Override
                    public void onSuccess(CaiDat caiDat) {
                        Log.d("CAIDAT", caiDat.toString());

                        int amThanhNen = caiDat.getAmThanhNen();
                        int amThanhHieuUng = caiDat.getAmThanhHieuUng();

                        MusicManager.setVolume(
                                SettingActivity.this,
                                amThanhNen);

                        SoundManager.setVolume(
                                SettingActivity.this,
                                amThanhHieuUng);

                        boolean xacThucEmail = caiDat.getXacThucEmail();
                        boolean maKhoiPhuc = caiDat.getMaKhoiPhuc();

                        if (xacThucEmail == true) {
                            txtXacThucEmailDongMo.setText("✅");
                            cardMaKhoiPhucDongMo.setEnabled(true);
                            cardMaKhoiPhucDongMo.setVisibility(View.VISIBLE);
                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#78C0C6"));
                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                        } else {
                            txtXacThucEmailDongMo.setText("❌");

                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                            cardMaKhoiPhucDongMo.setEnabled(false);
                            cardMaKhoiPhucDongMo.setVisibility(View.GONE);

                            if (maKhoiPhuc == true) {
                                caiDat.setMaKhoiPhuc(false);
                                CaiDatRepository.luuCaiDat(
                                        caiDat,
                                        task -> {
                                            if (task.isSuccessful()) {
                                                Log.d("CAIDAT", "Lưu thành công");
                                            } else {
                                                Log.d("CAIDAT", "Lưu thất bại");
                                            }
                                        }
                                );
                            }


                        }

                        if (maKhoiPhuc == true) {
                            txtMaKhoiPhucDongMo.setText("✅");
                            imgQR.setVisibility(View.VISIBLE);
                            layMaKhoiPhuc();
                            theoDoiMaKhoiPhuc();
                            //cardMaKhoiPhucDongMo.setCardBackgroundColor(Color.parseColor("#78C0C6"));
                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                        } else {
                            txtMaKhoiPhucDongMo.setText("❌");
                            imgQR.setVisibility(View.GONE);
                            //cardMaKhoiPhucDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        }

                        seekBarBackground.setProgress(amThanhNen);
                        seekBarEffect.setProgress(amThanhHieuUng);

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("CAIDAT", message);
                    }
                }
        );
    }

    private void theoDoiMaKhoiPhuc() {
        MaKhoiPhucRepository.theoDoiMaKhoiPhuc(
                SessionManager.getUserId(this),
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot
                    ) {

                        if (!snapshot.exists()) {
                            return;
                        }

                        MaKhoiPhuc maKhoiPhuc =
                                snapshot.getValue(
                                        MaKhoiPhuc.class
                                );

                        if (maKhoiPhuc != null) {

                            taoMaOR(maKhoiPhuc);
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

    private void taoMaOR(MaKhoiPhuc maKhoiPhuc) {
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(
                    "maNguoiDung",
                    maKhoiPhuc.getMaNguoiDung()
            );

            jsonObject.put(
                    "maKhoiPhuc",
                    maKhoiPhuc.getMaKhoiPhuc()
            );

        } catch (JSONException e) {

            //e.printStackTrace();

        }

        String dataQR = jsonObject.toString();

        Bitmap bitmap = taoQRCode(dataQR);

        imgQR.setImageBitmap(bitmap);
    }

    public Bitmap taoQRCode(String text) {

        try {

            BitMatrix bitMatrix =
                    new MultiFormatWriter().encode(
                            text,
                            BarcodeFormat.QR_CODE,
                            500,
                            500
                    );

            Bitmap bitmap = Bitmap.createBitmap(
                    500,
                    500,
                    Bitmap.Config.RGB_565
            );

            for (int x = 0; x < 500; x++) {

                for (int y = 0; y < 500; y++) {

                    bitmap.setPixel(
                            x,
                            y,
                            bitMatrix.get(x, y)
                                    ? Color.BLACK
                                    : Color.WHITE
                    );

                }

            }

            return bitmap;

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }

    private void taoCaiDatMatDinh() {
        if (!NetworkHelper.isConnected(SettingActivity.this)) {

            MessageHelper.error(
                    SettingActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        CaiDatRepository.taoMacDinhNeuChuaCo(
                SessionManager.getUserId(this),
                task -> {

                    if (task.isSuccessful()) {
                        Log.d("CAIDAT", "Đã có hoặc đã tạo mới thành công");
                    } else {
                        Log.e(
                                "CAIDAT",
                                task.getException().getMessage()
                        );
                    }
                }
        );
    }

    private void setControl() {
        lvThietBiDangNhap = findViewById(R.id.lvThietBiDangNhap);
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtXacThucEmailDongMo = findViewById(R.id.txtXacThucEmailDongMo);
        txtMaKhoiPhucDongMo = findViewById(R.id.txtMaKhoiPhucDongMo);

        seekBarEffect = findViewById(R.id.seekBarEffect);
        seekBarBackground = findViewById(R.id.seekBarBackground);

        cardXacThucEmailDongMo = findViewById(R.id.cardXacThucEmailDongMo);
        cardMaKhoiPhucDongMo = findViewById(R.id.cardMaKhoiPhucDongMo);

        cardNhacNen1 = findViewById(R.id.cardNhacNen1);
        cardNhacNen2 = findViewById(R.id.cardNhacNen2);

        cardAmThanhHieuUng3 = findViewById(R.id.cardAmThanhHieuUng3);
        cardAmThanhHieuUng1 = findViewById(R.id.cardAmThanhHieuUng1);
        cardAmThanhHieuUng2 = findViewById(R.id.cardAmThanhHieuUng2);

        imgQR = findViewById(R.id.imgQR);
    }

    private void luuThietBi(String maNguoiDung) {
        //String maThietBi = DeviceHelper.getDeviceId(SettingActivity.this);
        String maThietBi = "333333a933f8aaa";

        thietBiDangNhapRepository.layThietBi(
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
                                    "SamSung A12F5010"
                            );
                            thietBi.setNgayTao(ngayHienTai);
                            thietBi.setNgayCapNhatCuoi(ngayHienTai);
                            thietBi.setDangHoatDong(true);

                            thietBiDangNhapRepository.luuThietBiDangNhap(
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


                        } else {

                            // Đã có thiết bị
                            thietBiDangNhapRepository.capNhatLanDangNhapCuoi(
                                    maNguoiDung,
                                    maThietBi
                            );


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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ThietBiDangNhapHelper.stopTheoDoiDanhSachThietBi(
                SessionManager.getUserId(this),
                dsThietBiListener
        );
    }

}