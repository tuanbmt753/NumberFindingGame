package com.example.numberfindinggame.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.manchoi.ManBaActivity;
import com.example.numberfindinggame.activity.manchoi.ManBonActivity;
import com.example.numberfindinggame.activity.manchoi.ManHaiActivity;
import com.example.numberfindinggame.activity.manchoi.ManMotActivity;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.ManChoi;

import java.util.List;

public class ManChoiAdapter extends RecyclerView.Adapter<ManChoiAdapter.ViewHolder> {

    private Context context;
    private List<ManChoi> manChoiList;
    private int manHienTai;

    public ManChoiAdapter(Context context, List<ManChoi> manChoiList, int manHienTai) {
        this.context = context;
        this.manChoiList = manChoiList;
        this.manHienTai = manHienTai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_man_choi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ManChoi manChoi = manChoiList.get(position);
        holder.tvManChoi.setText(String.valueOf(manChoi.getManChoi()));

        if (manChoi.getManChoi() == manHienTai) {
            holder.tvManChoi.setBackgroundColor(
                    context.getResources().getColor(R.color.man_hien_tai)
            );
            holder.tvManChoi.setTextColor(context.getResources().getColor(R.color.man_text));
        } else if (manChoi.getManChoi() < manHienTai) {
            holder.tvManChoi.setBackgroundColor(
                    context.getResources().getColor(R.color.man_da_vuot)
            );
            holder.tvManChoi.setTextColor(context.getResources().getColor(R.color.man_text));
        } else {
            holder.tvManChoi.setBackgroundColor(
                    context.getResources().getColor(R.color.man_chua_choi)
            );
            holder.tvManChoi.setTextColor(context.getResources().getColor(R.color.man_text));
        }
        holder.tvManChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chonManChoi(holder);
            }
        });

        if (manChoi.getManChoi() <= manHienTai + 1) {
            holder.tvManChoi.setEnabled(true);
            holder.tvManChoi.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.tvManChoi.setEnabled(false);
            holder.tvManChoi.setTextColor(context.getResources().getColor(android.R.color.transparent));
            holder.tvManChoi.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

    }

    private void chonManChoi(ViewHolder holder) {

        if ("1".equals(holder.tvManChoi.getText().toString())) {
            Intent intent = new Intent(context, ManMotActivity.class);
            SoundManager.playButton(context);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        if ("2".equals(holder.tvManChoi.getText().toString())) {
            Intent intent = new Intent(context, ManHaiActivity.class);
            SoundManager.playButton(context);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        if ("3".equals(holder.tvManChoi.getText().toString())) {
            Intent intent = new Intent(context, ManBaActivity.class);
            SoundManager.playButton(context);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

        if ("4".equals(holder.tvManChoi.getText().toString())) {
            Intent intent = new Intent(context, ManBonActivity.class);
            SoundManager.playButton(context);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

    }


    @Override
    public int getItemCount() {
        return manChoiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvManChoi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvManChoi = itemView.findViewById(R.id.tvManChoi);
        }
    }
}

