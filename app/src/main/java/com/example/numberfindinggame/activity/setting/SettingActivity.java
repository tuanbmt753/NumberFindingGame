package com.example.numberfindinggame.activity.setting;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.adapter.HieuUngAdapter;
import com.example.numberfindinggame.adapter.NhacHieuUngAdapter;
import com.example.numberfindinggame.adapter.NhacNenAdapter;
import com.example.numberfindinggame.adapter.ThietBiDangNhapAdapter;
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.constant.MusicType;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.dialog.ConfirmDialogMenu;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.MusicFileHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.model.HieuUng;
import com.example.numberfindinggame.session.HieuUngSession;
import com.example.numberfindinggame.session.MenuSession;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.session.SessionManagerSetting;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.MaKhoiPhuc;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.NhacHieuUng;
import com.example.numberfindinggame.model.NhacNen;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.example.numberfindinggame.repository.MaKhoiPhucRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.example.numberfindinggame.utils.LoadingDialog;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SettingActivity extends AppCompatActivity {

    private ListView lvThietBiDangNhap, lvNhacNen, lvNhacHieuUng, lvHieuUng;
    private ArrayList<ThietBiDangNhap> dsThietBiDangNhap = new ArrayList<>();
    private ThietBiDangNhapAdapter thietBiDangNhapAdapter;


    private ArrayList<NhacNen> dsNhacNen = new ArrayList<>();
    private NhacNenAdapter nhacNenAdapter;

    private ArrayList<NhacHieuUng> dsNhacHieuUng = new ArrayList<>();
    private NhacHieuUngAdapter nhacHieuUngAdapter;

    private ArrayList<HieuUng> dsHieuUng = new ArrayList<>();
    private HieuUngAdapter hieuUngAdapter;

    private ValueEventListener dsThietBiListener;

    private ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();

    private TextView txtXacThucEmailDongMo, txtMaKhoiPhucDongMo;
    private TextView txtMenu, txtTrangChu, txtThoat, txtMoRongMenu;

    private SeekBar seekBarBackground, seekBarEffect;

    private MaterialCardView cardXacThucEmailDongMo, cardMaKhoiPhucDongMo, cardThemNhacNen;

    private NguoiDungRepository repository = new NguoiDungRepository();

    private CaiDat caiDat;

    private ImageView imgQR, imgLogo;

    private ActivityResultLauncher<Intent> pickMp3;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();

    private MenuSession menuSession;
    private LinearLayout layoutMenu;

    private HieuUngGlitchLayout layoutGlitch;
    private GlitchView viewNhieu;
    private ConstraintLayout layoutLogo;

    private Handler handler = new Handler(Looper.getMainLooper());

    private HieuUngSession hieuUngSession;
    private Integer hieuUng = 4;

    private MusicType musicType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        setControl();
        setEvent();
    }

    private void setEvent() {
        musicType = new MusicType();
        hieuUngSession = new HieuUngSession(this);

        // Nếu lần đầu tiên chưa có dữ liệu
        if (!hieuUngSession.isHieuUngExists()) {

            hieuUng = 4;

            // Lưu mặc định hiệu ứng 4
            hieuUngSession.setHieuUng(hieuUng);

        }

        hieuUng = hieuUngSession.getHieuUng();
        if (hieuUng == 3 || hieuUng == 4 || hieuUng == 1) {
            SoundManager.playElectric(SettingActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }

        menuSession = new MenuSession(this);
        thietBiDangNhapAdapter = new ThietBiDangNhapAdapter(this, dsThietBiDangNhap);
        lvThietBiDangNhap.setAdapter(thietBiDangNhapAdapter);
        khoiTao();
        layThongTinNguoiDung();

        pickMp3 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK
                            &&
                            result.getData() != null) {

                        Uri uri =
                                result.getData()
                                        .getData();

                        boolean success =
                                MusicFileHelper
                                        .saveMusic(
                                                SettingActivity.this,
                                                uri
                                        );

                        if (success) {
                            MessageHelper.success(SettingActivity.this, "Thêm nhạc thành công");
                            loadLaiNhacNen();
                        } else {
                            MessageHelper.info(SettingActivity.this, "Chỉ hỗ trợ file mp3");
                        }

                    }

                });

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
                            "✅ " + text + " .⚠️ Xác thực có tác dụng trong 20 phút, khi hết 20 phút bạn sẽ cần phải xác thực lại để có thể thực hiện các hành động như cài đặt xác thực, đăng xuất từ xa, và xóa thiết bị, ...! ",
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

        if (menuSession.isMenuMo()) {
            layoutMenu.setVisibility(View.VISIBLE);
            txtMoRongMenu.setText("➖");
        } else {
            layoutMenu.setVisibility(View.GONE);
            txtMoRongMenu.setText("➕");
        }

        txtMoRongMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!menuSession.isMenuMo()) {
                    layoutMenu.setVisibility(View.VISIBLE);
                    menuSession.setMenuMo(true);
                    txtMoRongMenu.setText("➖");
                } else {
                    layoutMenu.setVisibility(View.GONE);
                    menuSession.setMenuMo(false);
                    txtMoRongMenu.setText("➕");
                }
            }
        });

        txtThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, TrangChuActivity.class);
                SoundManager.playButton(SettingActivity.this);
                startActivity(intent);
                finish();
            }
        });

        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(SettingActivity.this);

                new ConfirmDialogMenu(SettingActivity.this).show();
            }
        });

        txtTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(SettingActivity.this);

                Intent intent = new Intent(SettingActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cardMaKhoiPhucDongMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(SettingActivity.this);
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
                                                        SoundManager.playButton(SettingActivity.this);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onNo() {
                                                        SoundManager.playButton(SettingActivity.this);
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
                SoundManager.playButton(SettingActivity.this);
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
                                                        SoundManager.playButton(SettingActivity.this);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onNo() {
                                                        SoundManager.playButton(SettingActivity.this);
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

        cardThemNhacNen.setOnClickListener(v -> {

            SoundManager.playButton(
                    SettingActivity.this
            );

            Intent intent =
                    new Intent(
                            Intent.ACTION_OPEN_DOCUMENT
                    );

            intent.addCategory(
                    Intent.CATEGORY_OPENABLE
            );

            intent.setType("audio/*");

            pickMp3.launch(intent);


        });

    }

    private void layThongTinNguoiDung() {
        if (!NetworkHelper.isConnected(SettingActivity.this)) {

            MessageHelper.error(
                    SettingActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(SettingActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                SessionManager.getUserId(SettingActivity.this),
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


        Field[] fields = R.raw.class.getFields();

        int sttNhacNen = 1;
        int sttHieuUng = 1;

        for (Field field : fields) {

            String tenFile = field.getName();

            if (tenFile.startsWith("click")) {

                try {

                    int resId = field.getInt(null);

                    dsNhacHieuUng.add(
                            new NhacHieuUng(
                                    resId,
                                    "Nhạc hiệu ứng " + sttHieuUng,
                                    "Tên file: " + tenFile + ".mp3"
                            )
                    );

                    sttHieuUng++;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

        dsNhacNen.addAll(musicType.getDsNhacNen());

        List<File> list =
                MusicFileHelper.getAllMusic(this);


        for (File file : list) {

            Log.e(
                    "MUSIC",
                    file.getName()
            );
            dsNhacNen.add(new NhacNen(-1, file.getName(), file.getPath(), "", ""));

        }

        nhacNenAdapter = new NhacNenAdapter(SettingActivity.this, dsNhacNen);
        lvNhacNen.setAdapter(nhacNenAdapter);

        ListViewHelper
                .setListViewHeightBasedOnChildren(
                        lvNhacNen
                );

        nhacHieuUngAdapter = new NhacHieuUngAdapter(SettingActivity.this, dsNhacHieuUng);
        lvNhacHieuUng.setAdapter(nhacHieuUngAdapter);

        ListViewHelper
                .setListViewHeightBasedOnChildren(
                        lvNhacHieuUng
                );


        dsHieuUng.add(new HieuUng(1, "Không hiệu ứng", ""));
        dsHieuUng.add(new HieuUng(3, "Nhiễu sóng 1", ""));
        dsHieuUng.add(new HieuUng(4, "Nhiễu sóng 2", ""));
        hieuUngAdapter = new HieuUngAdapter(SettingActivity.this, dsHieuUng);
        lvHieuUng.setAdapter(hieuUngAdapter);
        ListViewHelper
                .setListViewHeightBasedOnChildren(
                        lvHieuUng
                );

    }

    private void loadLaiNhacNen() {

        dsNhacNen.clear();

        Field[] fields =
                R.raw.class.getFields();

        int stt = 1;

        dsNhacNen.addAll(musicType.getDsNhacNen());


        List<File> files =
                MusicFileHelper
                        .getAllMusic(this);


        Log.e(
                "MUSIC",
                "Số file = "
                        + files.size()
        );


        for (File file : files) {

            Log.e(
                    "MUSIC",
                    file.getAbsolutePath()
            );


            dsNhacNen.add(

                    new NhacNen(
                            -1,
                            file.getName(),
                            file.getAbsolutePath(),
                            "",
                            ""
                    )

            );

        }


        nhacNenAdapter.notifyDataSetChanged();


        ListViewHelper
                .setListViewHeightBasedOnChildren(
                        lvNhacNen
                );

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
        lvNhacNen = findViewById(R.id.lvNhacNen);
        lvNhacHieuUng = findViewById(R.id.lvNhacHieuUng);
        lvHieuUng = findViewById(R.id.lvHieuUng);

        txtXacThucEmailDongMo = findViewById(R.id.txtXacThucEmailDongMo);
        txtMaKhoiPhucDongMo = findViewById(R.id.txtMaKhoiPhucDongMo);

        txtThoat = findViewById(R.id.txtThoat);
        txtMenu = findViewById(R.id.txtMenu);
        txtTrangChu = findViewById(R.id.txtTrangChu);
        txtMoRongMenu = findViewById(R.id.txtMoRongMenu);

        seekBarEffect = findViewById(R.id.seekBarEffect);
        seekBarBackground = findViewById(R.id.seekBarBackground);

        cardXacThucEmailDongMo = findViewById(R.id.cardXacThucEmailDongMo);
        cardMaKhoiPhucDongMo = findViewById(R.id.cardMaKhoiPhucDongMo);

        cardThemNhacNen = findViewById(R.id.cardThemNhacNen);

        imgQR = findViewById(R.id.imgQR);
        imgLogo = findViewById(R.id.imgLogo);

        layoutMenu = findViewById(R.id.layoutMenu);

        layoutLogo = findViewById(R.id.layoutLogo);
        viewNhieu = findViewById(R.id.viewNhieu);

        layoutGlitch = findViewById(R.id.layoutGlitch);
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
        handler.removeCallbacks(glitchRunnable);
    }


    private Runnable glitchRunnable = new Runnable() {
        @Override
        public void run() {
            hieuUng = hieuUngSession.getHieuUng();
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
                SoundManager.playElectric(SettingActivity.this);

            } else if (hieuUng == 4) {
                viewNhieu.setVisibility(View.GONE);
                // =========================
                // HIỆU ỨNG 4
                // =========================

                layoutGlitch.batDauGlitch(900);
                SoundManager.playElectric(SettingActivity.this);
            }

            // Phát nhạc electric.mp3


            // Nếu đang là hiệu ứng 3 hoặc 4 hoặc 1
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4 || hieuUng == 1) {
                handler.postDelayed(this, 3000);
            }
        }
    };


}