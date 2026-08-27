package com.example.numberfindinggame.activity.manchoi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.adapter.ManBaAdapter;
import com.example.numberfindinggame.adapter.ManBonAdapter;
import com.example.numberfindinggame.adapter.MangSongAdapter;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.dialog.ConfirmDialogMenu;
import com.example.numberfindinggame.helper.HieuUngHelper;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.MangSong;
import com.example.numberfindinggame.model.TimSo;
import com.example.numberfindinggame.session.MenuSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManBonActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTimSo;
    private ManBonAdapter manBonAdapter;
    private List<TimSo> timSoList = new ArrayList<>();
    private RecyclerView rvMang;
    private List<MangSong> mangSongList = new ArrayList<>();
    private MangSongAdapter mangSongAdapter;
    private TextView txtMenu, txtTrangChu, txtThoat, txtChoiLai, txtMoRongMenu;

    private Context context;
    private LinearLayout linearLayoutChoiLai;

    private MenuSession menuSession;
    private LinearLayout layoutMenu;

    private TextView txtManChoi, txtCauHoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_ba);

        setControl();
        setEvent();

    }

    private void setEvent() {
        txtManChoi.setText("Màn 4");
        txtCauHoi.setText("Tìm bốn số nhỏ nhất trong các số sau ?");
        menuSession = new MenuSession(this);
        context = ManBonActivity.this;
        khoiTao();
        HieuUngHelper.xuatHienLanLuot(
                recyclerViewTimSo,
                400,
                100
        );

        txtThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                chuyenManHinh(ManChoiActivity.class);
            }
        });

        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);

                new ConfirmDialogMenu(context, 1).show();
            }
        });

        txtTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                chuyenManHinh(TrangChuActivity.class);
            }
        });

        linearLayoutChoiLai.setVisibility(View.VISIBLE);
        txtChoiLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                chuyenManHinh(ManBonActivity.class);
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

    private void chuyenManHinh(Class<?> dichDen) {
        new ConfirmDialog(
                context,
                "⚠️Xác nhận",
                "Bạn đang trong trận, bạn có muốn thực hiện hành động này không? " +
                        "Thực hiện hành động sau sẽ mất tiến độ chơi hiện tại!",
                new ConfirmDialog.ConfirmCallback() {

                    @Override
                    public void onYes() {
                        SoundManager.playButton(context);

                        Intent intent = new Intent(context, dichDen);
                        SoundManager.playButton(context);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                    @Override
                    public void onNo() {

                    }
                }
        ).show();
    }

    private void khoiTao() {
        List<Integer> dsSo = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            dsSo.add(i);
        }

        Collections.shuffle(dsSo);

        timSoList.clear();

        for (int i = 0; i < 16; i++) {
            timSoList.add(new TimSo(dsSo.get(i)));
        }

        manBonAdapter = new ManBonAdapter(this, timSoList);
        recyclerViewTimSo.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewTimSo.setAdapter(manBonAdapter);

        rvMang.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,
                        false));


        int soMangCon = manBonAdapter.getMangChoi();

        for (int i = 0; i < 5; i++) {
            mangSongList.add(new MangSong(i < soMangCon));
        }

        mangSongAdapter = new MangSongAdapter(mangSongList);
        rvMang.setAdapter(mangSongAdapter);

        manBonAdapter.setMangSongList(mangSongList);
        manBonAdapter.setMangSongAdapter(mangSongAdapter);
    }

    private void setControl() {
        recyclerViewTimSo = findViewById(R.id.recyclerViewTimSo);
        rvMang = findViewById(R.id.rvMang);

        txtThoat = findViewById(R.id.txtThoat);
        txtMenu = findViewById(R.id.txtMenu);
        txtTrangChu = findViewById(R.id.txtTrangChu);
        txtChoiLai = findViewById(R.id.txtChoiLai);
        txtMoRongMenu = findViewById(R.id.txtMoRongMenu);

        layoutMenu = findViewById(R.id.layoutMenu);

        linearLayoutChoiLai = findViewById(R.id.linearLayoutChoiLai);

        txtManChoi = findViewById(R.id.txtManChoi);
        txtCauHoi = findViewById(R.id.txtCauHoi);
    }


}