package com.example.numberfindinggame.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.model.MangSong;

import java.util.List;

public class MangSongAdapter extends RecyclerView.Adapter<MangSongAdapter.ViewHolder> {

    private List<MangSong> list;

    public MangSongAdapter(List<MangSong> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mang, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (list.get(position).isConMang()) {
            holder.imgMang.setImageResource(R.drawable.ic_network);
        } else {
            holder.imgMang.setImageResource(R.drawable.ic_trai_tim);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMang = itemView.findViewById(R.id.imgMang);
        }
    }
}
