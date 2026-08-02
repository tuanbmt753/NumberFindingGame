package com.example.numberfindinggame.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.model.MangSong;
import com.example.numberfindinggame.model.TimSo;

import java.util.List;

public class ManMot extends RecyclerView.Adapter<ManMot.ViewHolder> {

    private Context context;
    private List<TimSo> timSoList;
    private int minSo;

    private int mangChoi;

    private List<MangSong> mangSongList;
    private MangSongAdapter mangSongAdapter;

    public ManMot(Context context, List<TimSo> timSoList) {
        this.context = context;
        this.timSoList = timSoList;
        mangChoi = 5;

        if (timSoList != null && !timSoList.isEmpty()) {
            minSo = timSoList.get(0).getSo();

            for (TimSo item : timSoList) {
                if (item.getSo() < minSo) {
                    minSo = item.getSo();
                }
            }
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tim_so, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimSo timSo = timSoList.get(position);
        holder.tvTimSo.setText(String.valueOf(timSo.getSo()));

        holder.tvTimSo.setOnClickListener(v -> {
            SoundManager.playButton(context);
            if (mangChoi == 1) {
                MessageHelper.info((Activity) context,
                        "Bạn đã hết mạng sống: " + mangChoi);
            } else {
                int so = timSo.getSo();
                if (so == minSo) {
                    MessageHelper.success((Activity) context,
                            "Số bé nhất là: " + so);
                } else {
                    mangChoi = mangChoi - 1;
                    mangSongList.clear();
                    for (int i = 0; i < 5; i++) {
                        mangSongList.add(new MangSong(i < mangChoi));
                    }
                    mangSongAdapter.notifyDataSetChanged();

                    if (mangChoi == 1) {
                        MessageHelper.info((Activity) context,
                                "Bạn đã hết mạng sống: " + mangChoi);
                    }

                }
            }


        });
    }

    public int getMangChoi() {
        return mangChoi;
    }

    public void setMangChoi(int mangChoi) {
        this.mangChoi = mangChoi;
    }

    public List<MangSong> getMangSongList() {
        return mangSongList;
    }

    public void setMangSongList(List<MangSong> mangSongList) {
        this.mangSongList = mangSongList;
    }

    public MangSongAdapter getMangSongAdapter() {
        return mangSongAdapter;
    }

    public void setMangSongAdapter(MangSongAdapter mangSongAdapter) {
        this.mangSongAdapter = mangSongAdapter;
    }

    @Override
    public int getItemCount() {
        return timSoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimSo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimSo = itemView.findViewById(R.id.tvTimSo);
        }
    }
}

