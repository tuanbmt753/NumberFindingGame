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
import com.example.numberfindinggame.adapter.MangSongAdapter;
import com.example.numberfindinggame.adapter.ManMotAdapter;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.dialog.ConfirmDialogMenu;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.MangSong;
import com.example.numberfindinggame.model.TimSo;
import com.example.numberfindinggame.session.MenuSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManMotActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTimSo;
    private ManMotAdapter manMotAdapter;
    private List<TimSo> timSoList = new ArrayList<>();
    private TextView txtMenu, txtTrangChu, txtThoat, txtChoiLai, txtMoRongMenu;
    private LinearLayout linearLayoutChoiLai;

    private RecyclerView rvMang;
    private List<MangSong> mangSongList = new ArrayList<>();
    private MangSongAdapter mangSongAdapter;

    private MenuSession menuSession;
    private LinearLayout layoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_mot);

        setControl();
        setEvent();

    }

    private void setEvent() {
        menuSession = new MenuSession(this);
        khoiTao();
        linearLayoutChoiLai.setVisibility(View.VISIBLE);

        txtThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(ManMotActivity.this);
                chuyenManHinh(ManChoiActivity.class);
            }
        });

        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(ManMotActivity.this);
                new ConfirmDialogMenu(ManMotActivity.this, 1).show();
            }
        });

        txtTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(ManMotActivity.this);
                chuyenManHinh(TrangChuActivity.class);
            }
        });

        txtChoiLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(ManMotActivity.this);
                chuyenManHinh(ManMotActivity.class);
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
        Context context = ManMotActivity.this;
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

        for (int i = 1; i <= 10; i++) {
            dsSo.add(i);
        }

        Collections.shuffle(dsSo);

        timSoList.clear();

        for (int i = 0; i < 4; i++) {
            timSoList.add(new TimSo(dsSo.get(i)));
        }

        manMotAdapter = new ManMotAdapter(this, timSoList);
        recyclerViewTimSo.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewTimSo.setAdapter(manMotAdapter);

        rvMang.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,
                        false));


        int soMangCon = manMotAdapter.getMangChoi();

        for (int i = 0; i < 5; i++) {
            mangSongList.add(new MangSong(i < soMangCon));
        }

        mangSongAdapter = new MangSongAdapter(mangSongList);
        rvMang.setAdapter(mangSongAdapter);

        manMotAdapter.setMangSongList(mangSongList);
        manMotAdapter.setMangSongAdapter(mangSongAdapter);
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
    }
}