package com.example.numberfindinggame.activity.manchoi;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.model.ManChoi;
import com.example.numberfindinggame.model.NguoiDung;
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

    private TextView txtQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_choi);

        setControl();
        setEvent();

    }

    private void setEvent() {
        khoiTao();
        layThongTinNguoiDung();

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManChoiActivity.this, TrangChuActivity.class);
                SoundManager.playButton(ManChoiActivity.this);
                startActivity(intent);
                finish();
            }
        });

    }

    private void khoiTao() {
        manChoiList = taoDuLieuMau(30);
        adapter = new ManChoiAdapter(this, manChoiList, 25);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        // Scroll đến màn 10 (index = 9)
        recyclerView.scrollToPosition(25);
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
        txtQuayLai = findViewById(R.id.txtQuayLai);

    }

    private void layThongTinNguoiDung() {
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
                SessionManager.getUserId(ManChoiActivity.this),
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