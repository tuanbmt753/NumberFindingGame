package com.example.numberfindinggame.activity.manchoi;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangKyActivity;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.adapter.ManChoiAdapter;
import com.example.numberfindinggame.dialog.ConfirmDialogMenu;
import com.example.numberfindinggame.helper.GlitchView;
import com.example.numberfindinggame.helper.HieuUngGlitchLayout;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.session.HieuUngSession;
import com.example.numberfindinggame.session.MenuSession;
import com.example.numberfindinggame.session.NhacHieuUngNenSession;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.ManChoi;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.ManChoiRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class ManChoiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManChoiAdapter adapter;
    private List<ManChoi> manChoiList;


    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private ManChoiRepository manChoiRepository = new ManChoiRepository();

    private TextView txtMenu, txtTrangChu, txtThoat, txtMoRongMenu;

    private String maNguoiDung;
    private Integer manChoiHienTai = 0;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_choi);

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
            SoundManager.playElectric(ManChoiActivity.this);
            layoutGlitch.postDelayed(() -> {

                handler.post(glitchRunnable);

            }, 300);
        }


        menuSession = new MenuSession(this);
        maNguoiDung = SessionManager.getUserId(ManChoiActivity.this);
        layManChoiHienTai(maNguoiDung);
        layThongTinNguoiDung(maNguoiDung);

        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(ManChoiActivity.this);

                new ConfirmDialogMenu(ManChoiActivity.this).show();
            }
        });

        txtTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(ManChoiActivity.this);

                Intent intent = new Intent(ManChoiActivity.this, TrangChuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(ManChoiActivity.this);

                Intent intent = new Intent(ManChoiActivity.this, TrangChuActivity.class);
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


    }

    private void khoiTao() {
        manChoiList = taoDuLieuMau(4);
        adapter = new ManChoiAdapter(this, manChoiList, manChoiHienTai);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);
        // Scroll đến màn 10 (index = 9)
        recyclerView.scrollToPosition(manChoiHienTai);
    }

    private void layManChoiHienTai(String maNguoiDung) {
        if (!NetworkHelper.isConnected(ManChoiActivity.this)) {

            MessageHelper.error(
                    ManChoiActivity.this,
                    "Không có kết nối Internet"
            );
        }

        ManChoiRepository.layTheoMaNguoiDung(maNguoiDung, new ManChoiRepository.OnGetListener() {
            @Override
            public void onSuccess(ManChoi manChoi) {

                if (manChoi != null) {
                    // Có dữ liệu
                    //MessageHelper.success(ManChoiActivity.this, "Có dữ liệu " + manChoi.getManChoi());
                    Log.d("ManChoi", "Màn hiện tại: " + manChoi.getManChoi());
                    manChoiHienTai = manChoi.getManChoi();
                    khoiTao();

                } else {
                    // Không tìm thấy dữ liệu
                    //MessageHelper.success(ManChoiActivity.this, "Không tìm thấy dữ liệu");
                    Log.d("ManChoi", "Chưa có dữ liệu người chơi");
                    manChoiHienTai = 0;
                    khoiTao();

                }
            }

            @Override
            public void onFailed(String error) {
                MessageHelper.error(ManChoiActivity.this, error);
            }
        });
    }


    private List<ManChoi> taoDuLieuMau(int soMan) {
        List<ManChoi> list = new ArrayList<>();
        long ngay = System.currentTimeMillis();
        for (int i = 1; i <= soMan; i++) {
            list.add(new ManChoi("User01", i, ngay, ngay));
        }
        return list;
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerView);

        imgLogo = findViewById(R.id.imgLogo);

        layoutLogo = findViewById(R.id.layoutLogo);
        layoutGlitch = findViewById(R.id.layoutGlitch);
        layoutMenu = findViewById(R.id.layoutMenu);

        txtMenu = findViewById(R.id.txtMenu);
        txtTrangChu = findViewById(R.id.txtTrangChu);
        txtThoat = findViewById(R.id.txtThoat);
        txtMoRongMenu = findViewById(R.id.txtMoRongMenu);

        viewNhieu = findViewById(R.id.viewNhieu);
    }

    private void layThongTinNguoiDung(String maNguoiDung) {
        if (!NetworkHelper.isConnected(ManChoiActivity.this)) {

            MessageHelper.error(
                    ManChoiActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(ManChoiActivity.this);
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
                SoundManager.playElectric(ManChoiActivity.this);
            }

            // Nếu đang là hiệu ứng 3 hoặc 4
            // thì 7 giây sau chạy lại
            if (hieuUng == 3 || hieuUng == 4) {
                handler.postDelayed(this, 7000);
            }
        }
    };
}