package com.example.numberfindinggame.activity.manchoi;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.adapter.ManChoiAdapter;
import com.example.numberfindinggame.dialog.ConfirmDialogManChoi;
import com.example.numberfindinggame.dialog.ConfirmDialogMenu;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.model.ManChoi;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.ManChoiRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.utils.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManChoiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ManChoiAdapter adapter;
    private List<ManChoi> manChoiList;

    private ImageView imgLogo;
    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private ManChoiRepository manChoiRepository = new ManChoiRepository();

    private TextView txtMenu, txtTrangChu, txtThoat;

    private String maNguoiDung;
    private Integer manChoiHienTai = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_choi);

        setControl();
        setEvent();

    }

    private void setEvent() {
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

    }

    private void khoiTao() {
        manChoiList = taoDuLieuMau(3);
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
                    manChoiHienTai = 1;
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
        txtMenu = findViewById(R.id.txtMenu);
        txtTrangChu = findViewById(R.id.txtTrangChu);
        txtThoat = findViewById(R.id.txtThoat);

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
}