package com.example.numberfindinggame.activity.BanBe;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LoiMoiKetBanActivity extends AppCompatActivity {

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


    private ListView lvDanhSach;
    ArrayList<BanBe> dsKetBan = new ArrayList<>();
    ArrayList<BanBe> dsKetBanGoc = new ArrayList<>();
    private LoiMoiKetBanAdapter loiMoiKetBanAdapter;
    private BanBeRepository banBeRepository = new BanBeRepository();
    private EditText edtTimKiem;

    private ImageView imgKhongTimThay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loi_moi_ket_ban);


        setControl();
        setEvent();

    }

    private void setEvent() {
        maNguoiDung = SessionManager.getUserId(LoiMoiKetBanActivity.this);
        khoiTao();

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
            SoundManager.playElectric(LoiMoiKetBanActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }


        menuSession = new MenuSession(this);

        layThongTinNguoiDung(maNguoiDung);

        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(LoiMoiKetBanActivity.this);

                new ConfirmDialogMenu(LoiMoiKetBanActivity.this).show();
            }
        });

        txtTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(LoiMoiKetBanActivity.this);

                Intent intent = new Intent(LoiMoiKetBanActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(LoiMoiKetBanActivity.this);

                Intent intent = new Intent(LoiMoiKetBanActivity.this, BanBeActivity.class);
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


        edtTimKiem.addTextChangedListener(
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

                        dsKetBan.clear();

                        // Nếu ô tìm kiếm trống
                        if (tuKhoa.isEmpty()) {
                            dsKetBan.addAll(dsKetBanGoc);

                        } else {

                            for (BanBe banBe : dsKetBanGoc) {

                                if (banBe.getNguoiDung1() == null) {
                                    continue;
                                }

                                String name =
                                        banBe.getNguoiDung1().getTenNguoiDung();

                                if (name == null) {
                                    name = "";
                                }

                                name = name.toLowerCase();

                                if (name.contains(tuKhoa)) {
                                    dsKetBan.add(banBe);
                                }
                            }
                        }

                        if (loiMoiKetBanAdapter != null) {
                            loiMoiKetBanAdapter.notifyDataSetChanged();

                            ListViewHelper.setListViewHeightBasedOnChildren(
                                    lvDanhSach
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
        layDanhSachLoiMoiKetBan();
    }

    private void layDanhSachLoiMoiKetBan() {

        banBeRepository.layDanhSachLoiMoiKetBan(
                maNguoiDung,
                new BanBeRepository.OnGetBanBeListener() {

                    @Override
                    public void onResult(List<BanBe> danhSach) {

                        dsKetBan.clear();
                        dsKetBanGoc.clear();

                        dsKetBan.addAll(danhSach);
                        dsKetBanGoc.addAll(danhSach);

                        khongCoDuLieu();

                        if (danhSach == null || danhSach.isEmpty()) {

                            khoiTaoAdapter();

                            return;
                        }

                        // Số lượng người dùng cần lấy
                        AtomicInteger soNguoiConLai =
                                new AtomicInteger(danhSach.size());

                        for (BanBe banBe : danhSach) {

                            layThongTinNguoiDung2(
                                    banBe.getMaNguoiDung1(),
                                    () -> {

                                        // Giảm số lượng còn lại
                                        int conLai =
                                                soNguoiConLai.decrementAndGet();

                                        // Chỉ tạo Adapter khi tất cả đã lấy xong
                                        if (conLai == 0) {

                                            runOnUiThread(() -> {

                                                khoiTaoAdapter();

                                            });
                                        }
                                    }
                            );
                        }

                        // Log
                        for (BanBe banBe : danhSach) {

                            Log.d(
                                    "LOI_MOI",
                                    "Người gửi: "
                                            + banBe.getMaNguoiDung1()
                            );
                        }
                    }
                }
        );
    }

    private void khoiTaoAdapter() {

        loiMoiKetBanAdapter =
                new LoiMoiKetBanAdapter(
                        LoiMoiKetBanActivity.this,
                        dsKetBan
                );

        lvDanhSach.setAdapter(loiMoiKetBanAdapter);

        ListViewHelper.setListViewHeightBasedOnChildren(
                lvDanhSach
        );

        khongCoDuLieu();
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

        lvDanhSach = findViewById(R.id.lvDanhSach);

        edtTimKiem = findViewById(R.id.edtTimKiem);

        imgKhongTimThay = findViewById(R.id.imgKhongTimThay);
    }

    private void layThongTinNguoiDung(String maNguoiDung) {
        if (!NetworkHelper.isConnected(LoiMoiKetBanActivity.this)) {

            MessageHelper.error(
                    LoiMoiKetBanActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(LoiMoiKetBanActivity.this);
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
                                task.getResult().getValue(NguoiDung.class);

                        if (nguoiDung != null) {

                            // Cập nhật dsKetBan
                            for (BanBe banBe : dsKetBan) {

                                if (maNguoiDung.equals(
                                        banBe.getMaNguoiDung1()
                                )) {

                                    banBe.setNguoiDung1(
                                            nguoiDung
                                    );

                                    break;
                                }
                            }

                            // Cập nhật dsKetBanGoc
                            for (BanBe banBe : dsKetBanGoc) {

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

                    // Báo đã hoàn thành request này
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
        );
    }

    private void khongCoDuLieu() {
        if (dsKetBan.size() == 0) {
            lvDanhSach.setVisibility(View.GONE);
            imgKhongTimThay.setVisibility(View.VISIBLE);
        } else {
            lvDanhSach.setVisibility(View.VISIBLE);
            imgKhongTimThay.setVisibility(View.GONE);
        }
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
                SoundManager.playElectric(LoiMoiKetBanActivity.this);
            }

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 7000);
            }
        }
    };
}