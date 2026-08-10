package com.example.numberfindinggame.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.manchoi.ManChoiActivity;
import com.example.numberfindinggame.activity.manchoi.ManHaiActivity;
import com.example.numberfindinggame.activity.manchoi.ManMotActivity;
import com.example.numberfindinggame.dialog.ConfirmDialogManChoi;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.model.ManChoi;
import com.example.numberfindinggame.model.MangSong;
import com.example.numberfindinggame.model.TimSo;
import com.example.numberfindinggame.repository.ManChoiRepository;

import java.util.List;

public class ManHaiAdapter extends RecyclerView.Adapter<ManHaiAdapter.ViewHolder> {

    private Context context;
    private List<TimSo> timSoList;
    private int minSo;

    private int mangChoi;

    private List<MangSong> mangSongList;
    private MangSongAdapter mangSongAdapter;
    private ManChoiRepository manChoiRepository;

    private String maNguoiDung;

    private Integer soDaTim = 0;

    public ManHaiAdapter(Context context, List<TimSo> timSoList) {
        this.context = context;
        this.timSoList = timSoList;
        mangChoi = 5;
        maNguoiDung = SessionManager.getUserId(context);

        timSoBeNhat();
    }

    private void timSoBeNhat() {
        if (timSoList != null && !timSoList.isEmpty()) {

            for (TimSo item : timSoList) {
                if (item.getSo() != -1) {
                    minSo = item.getSo();
                    break;
                }
            }

            for (TimSo item : timSoList) {
                if (item.getSo() != -1) {
                    if (item.getSo() < minSo) {
                        minSo = item.getSo();
                    }
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

        if (timSo.getSo() == -1) {
            holder.tvTimSo.setTextColor(Color.WHITE);
        } else {
            holder.tvTimSo.setTextColor(Color.BLACK);
        }

        holder.tvTimSo.setOnClickListener(v -> {
            SoundManager.playButton(context);
            if (mangChoi == 0) {
                menuKetThuc(context, "❌ Rất tiếc!", "\uD83D\uDE22 Bạn đã không hoàn thành màn chơi hiện tại? Hãy chơi lại và cố gắng đạt kết quả tốt hơn.", 2);
            } else {
                int so = timSo.getSo();
                if (so == minSo) {
                    MessageHelper.success((Activity) context, "Số bé nhất là: " + so);

                    soDaTim = soDaTim + 1;
                    timSo.setSo(-1);
                    notifyDataSetChanged();
                    timSoBeNhat();
                    if (soDaTim == 2) {
                        long ngay = System.currentTimeMillis();
                        ManChoi manChoi = new ManChoi(maNguoiDung, 2, ngay, ngay);
                        //MessageHelper.success((Activity) context, "Hoàn thành màn chơi: " + so);
                        ManChoiRepository.themHoacCapNhat(manChoi, new ManChoiRepository.OnCompleteListener() {
                            @Override
                            public void onSuccess() {
                                //MessageHelper.success((Activity) context, "Lưu màn chơi thành công");
                                menuKetThuc(context, "✔\uFE0F Thành công", "\uD83C\uDF89 Bạn đã hoàn thành màn chơi hiện tại?", 3);
                            }

                            @Override
                            public void onFailed(String error) {
                                MessageHelper.error((Activity) context, error);
                            }
                        });
                    }


                } else {
                    mangChoi = mangChoi - 1;
                    mangSongList.clear();
                    for (int i = 0; i < 5; i++) {
                        mangSongList.add(new MangSong(i < mangChoi));
                    }
                    mangSongAdapter.notifyDataSetChanged();

                    if (mangChoi == 0) {
                        menuKetThuc(context, "❌ Rất tiếc!", "\uD83D\uDE22 Bạn đã không hoàn thành màn chơi hiện tại? Hãy chơi lại và cố gắng đạt kết quả tốt hơn.", 2);
                    }

                }
            }


        });
    }

    private void menuKetThuc(Context context, String title, String message, Integer hanhDong) {

        new ConfirmDialogManChoi(context, title, message, hanhDong, new ConfirmDialogManChoi.ConfirmCallback() {
            @Override
            public void onManChoi() {
                SoundManager.playButton(context);
                Intent intent = new Intent(context, ManChoiActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void onChoiLai() {
                SoundManager.playButton(context);
                Intent intent = new Intent(context, ManHaiActivity.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void onTiepTheo() {

            }
        }).show();

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

