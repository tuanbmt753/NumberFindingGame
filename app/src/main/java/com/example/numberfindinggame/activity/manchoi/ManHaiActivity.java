package com.example.numberfindinggame.activity.manchoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.adapter.ManHaiAdapter;
import com.example.numberfindinggame.adapter.ManMotAdapter;
import com.example.numberfindinggame.adapter.MangSongAdapter;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.model.MangSong;
import com.example.numberfindinggame.model.TimSo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManHaiActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTimSo;
    private ManHaiAdapter manHaiAdapter;
    private List<TimSo> timSoList = new ArrayList<>();
    private TextView txtQuayLai;

    private RecyclerView rvMang;
    private List<MangSong> mangSongList = new ArrayList<>();
    private MangSongAdapter mangSongAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man_hai);

        setControl();
        setEvent();

    }

    private void setEvent() {
        khoiTao();

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManHaiActivity.this, ManChoiActivity.class);
                SoundManager.playButton(ManHaiActivity.this);
                startActivity(intent);
                finish();
            }
        });


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

        manHaiAdapter = new ManHaiAdapter(this, timSoList);
        recyclerViewTimSo.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewTimSo.setAdapter(manHaiAdapter);

        rvMang.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,
                        false));


        int soMangCon = manHaiAdapter.getMangChoi();

        for (int i = 0; i < 5; i++) {
            mangSongList.add(new MangSong(i < soMangCon));
        }

        mangSongAdapter = new MangSongAdapter(mangSongList);
        rvMang.setAdapter(mangSongAdapter);

        manHaiAdapter.setMangSongList(mangSongList);
        manHaiAdapter.setMangSongAdapter(mangSongAdapter);
    }

    private void setControl() {
        recyclerViewTimSo = findViewById(R.id.recyclerViewTimSo);
        rvMang = findViewById(R.id.rvMang);
        txtQuayLai = findViewById(R.id.txtQuayLai);
    }
}