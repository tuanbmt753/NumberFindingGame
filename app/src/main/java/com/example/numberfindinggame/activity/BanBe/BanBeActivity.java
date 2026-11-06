package com.example.numberfindinggame.activity.BanBe;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.activity.manchoi.ManChoiActivity;
import com.example.numberfindinggame.adapter.BanBeAdapter;
import com.example.numberfindinggame.adapter.LoiMoiKetBanAdapter;
import com.example.numberfindinggame.dialog.ConfirmDialogMenu;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.BanBe;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.BanBeRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.session.HieuUngSession;
import com.example.numberfindinggame.session.MenuSession;
import com.example.numberfindinggame.session.NhacHieuUngNenSession;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BanBeActivity extends AppCompatActivity {

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private TextView txtMenu, txtTrangChu, txtThoat, txtMoRongMenu;

    private String maNguoiDung;

    private MenuSession menuSession;
    private LinearLayout layoutMenu;

    private ImageView imgLogo;
    private HieuUngGlitchLayout layoutGlitch;
    private ConstraintLayout layoutLogo;
    private GlitchView viewNhieu;

    private HieuUngSession hieuUngSession;
    private Integer hieuUng = 4;
    private Handler handler = new Handler(Looper.getMainLooper());
    private NhacHieuUngNenSession nhacHieuUngNenSession;
    private MaterialCardView cardThemBanBe, cardLoiMoiKetBan;

    private ListView lvBanBe;
    ArrayList<BanBe> dsBanBe = new ArrayList<>();
    ArrayList<BanBe> dsBanBeGoc = new ArrayList<>();
    private BanBeAdapter banBeAdapter;
    private BanBeRepository banBeRepository = new BanBeRepository();

    private EditText edtTimKiemBanBe;
    private ImageView imgKhongTimThay;

    private Integer soLoiMoiKetBan = 0;
    private TextView txtLoiMoi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_ban_be);


        setControl();
        setEvent();

    }

    private void setEvent() {
        nhacHieuUngNenSession = new NhacHieuUngNenSession(this);
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
            SoundManager.playElectric(BanBeActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }


        menuSession = new MenuSession(this);
        maNguoiDung = SessionManager.getUserId(BanBeActivity.this);
        layThongTinNguoiDung(maNguoiDung);
        demSoLoiMoiKetBan();

        khoiTao();

        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(BanBeActivity.this);

                new ConfirmDialogMenu(BanBeActivity.this).show();
            }
        });

        txtTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(BanBeActivity.this);

                Intent intent = new Intent(BanBeActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(BanBeActivity.this);

                Intent intent = new Intent(BanBeActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        cardThemBanBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(BanBeActivity.this);

                Intent intent = new Intent(BanBeActivity.this, ThemBanBeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cardLoiMoiKetBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(BanBeActivity.this);

                Intent intent = new Intent(BanBeActivity.this, LoiMoiKetBanActivity.class);
                startActivity(intent);
                finish();
            }
        });


        edtTimKiemBanBe.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        String tuKhoa = s.toString().trim().toLowerCase();

                        dsBanBe.clear();

                        // Nếu ô tìm kiếm trống
                        if (tuKhoa.isEmpty()) {
                            dsBanBe.addAll(dsBanBeGoc);

                        } else {

                            for (BanBe banBe : dsBanBeGoc) {

                                NguoiDung nguoiDung =
                                        banBe.getNguoiDung1();

                                if (nguoiDung == null) {
                                    continue;
                                }

                                String name =
                                        nguoiDung.getTenNguoiDung();

                                if (name == null) {
                                    name = "";
                                }

                                name = name.toLowerCase();

                                if (name.contains(tuKhoa)) {
                                    dsBanBe.add(banBe);
                                }
                            }
                        }

                        if (banBeAdapter != null) {
                            banBeAdapter.notifyDataSetChanged();

                            ListViewHelper.setListViewHeightBasedOnChildren(
                                    lvBanBe
                            );

                            khongCoDuLieu();
                        }

                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );


    }

    private void khoiTao() {
        layDanhSachBanBe();
    }

    private void layDanhSachBanBe() {

        banBeRepository.layDanhSachBanBe(
                maNguoiDung,
                new BanBeRepository.OnGetBanBeListener() {

                    @Override
                    public void onResult(List<BanBe> danhSach) {

                        dsBanBe.clear();
                        dsBanBeGoc.clear();

                        if (danhSach == null || danhSach.isEmpty()) {

                            khongCoDuLieu();

                            khoiTaoAdapter();

                            return;
                        }

                        dsBanBe.addAll(danhSach);
                        dsBanBeGoc.addAll(danhSach);

                        khongCoDuLieu();

                        // Số lượng người dùng cần lấy
                        AtomicInteger soNguoiConLai =
                                new AtomicInteger(danhSach.size());

                        for (BanBe banBe : danhSach) {


                            layThongTinNguoiDung2(
                                    banBe.getMaNguoiDung1(),
                                    () -> {

                                        int conLai =
                                                soNguoiConLai.decrementAndGet();

                                        // Chỉ tạo Adapter khi
                                        // tất cả request đã hoàn thành
                                        if (conLai == 0) {

                                            runOnUiThread(() -> {

                                                khoiTaoAdapter();

                                            });
                                        }
                                    }
                            );
                        }

                        for (BanBe banBe : danhSach) {

                            Log.d(
                                    "BAN_BE",
                                    "Ma ban be: "
                                            + banBe.getMaNguoiDung1()
                            );
                        }
                    }
                }
        );
    }

    private void khoiTaoAdapter() {

        banBeAdapter =
                new BanBeAdapter(
                        BanBeActivity.this,
                        dsBanBe
                );

        lvBanBe.setAdapter(banBeAdapter);

        ListViewHelper.setListViewHeightBasedOnChildren(
                lvBanBe
        );

        khongCoDuLieu();
    }

    private void khongCoDuLieu() {
        if (dsBanBe.size() == 0) {
            lvBanBe.setVisibility(View.GONE);
            imgKhongTimThay.setVisibility(View.VISIBLE);
        } else {
            lvBanBe.setVisibility(View.VISIBLE);
            imgKhongTimThay.setVisibility(View.GONE);
        }
    }

    private void layThongTinNguoiDung2(
            String maNguoiDung,
            Runnable onComplete
    ) {

        if (maNguoiDung == null
                || maNguoiDung.trim().isEmpty()) {

            if (onComplete != null) {
                onComplete.run();
            }

            return;
        }

        nguoiDungRepository.layNguoiDungTheoMa(
                maNguoiDung,
                task -> {

                    if (task.isSuccessful()
                            && task.getResult() != null
                            && task.getResult().exists()) {

                        NguoiDung nguoiDung =
                                task.getResult()
                                        .getValue(NguoiDung.class);

                        if (nguoiDung != null
                                && nguoiDung.getMaNguoiDung() != null) {

                            // Cập nhật danh sách đang hiển thị
                            for (BanBe banBe : dsBanBe) {

                                if (maNguoiDung.equals(
                                        banBe.getMaNguoiDung1()
                                )) {

                                    banBe.setNguoiDung1(
                                            nguoiDung
                                    );

                                    break;
                                }
                            }

                            // Cập nhật danh sách gốc
                            for (BanBe banBe : dsBanBeGoc) {

                                if (maNguoiDung.equals(
                                        banBe.getMaNguoiDung1()
                                )) {

                                    banBe.setNguoiDung1(
                                            nguoiDung
                                    );

                                    break;
                                }
                            }
                        }
                    }

                    // Request này đã hoàn thành
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
        );
    }

    private void demSoLoiMoiKetBan() {
        banBeRepository.demSoLoiMoiKetBan(
                maNguoiDung,
                new BanBeRepository.OnCountLoiMoiListener() {

                    @Override
                    public void onResult(int soLuong) {
                        if (soLuong > 0) {
                            txtLoiMoi.setText("🤝 Lời mời (" + soLuong + ")");
                        } else {
                            txtLoiMoi.setText("🤝 Lời mời");
                        }
                    }
                }
        );
    }

    private void setControl() {
        imgLogo = findViewById(R.id.imgLogo);

        layoutLogo = findViewById(R.id.layoutLogo);
        layoutGlitch = findViewById(R.id.layoutGlitch);
        layoutMenu = findViewById(R.id.layoutMenu);

        txtMenu = findViewById(R.id.txtMenu);
        txtTrangChu = findViewById(R.id.txtTrangChu);
        txtThoat = findViewById(R.id.txtThoat);
        txtMoRongMenu = findViewById(R.id.txtMoRongMenu);

        viewNhieu = findViewById(R.id.viewNhieu);

        cardThemBanBe = findViewById(R.id.cardThemBanBe);
        cardLoiMoiKetBan = findViewById(R.id.cardLoiMoiKetBan);

        lvBanBe = findViewById(R.id.lvBanBe);

        edtTimKiemBanBe = findViewById(R.id.edtTimKiemBanBe);
        imgKhongTimThay = findViewById(R.id.imgKhongTimThay);

        txtLoiMoi = findViewById(R.id.txtLoiMoi);
    }

    private void layThongTinNguoiDung(String maNguoiDung) {
        if (!NetworkHelper.isConnected(BanBeActivity.this)) {

            MessageHelper.error(
                    BanBeActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(BanBeActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                maNguoiDung,
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
            // Chỉ phát âm thanh hiệu ứng khi đang bật
            if (nhacHieuUngNenSession.isNhacHieuUng()) {
                SoundManager.playElectric(BanBeActivity.this);
            }

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 7000);
            }
        }
    };
}