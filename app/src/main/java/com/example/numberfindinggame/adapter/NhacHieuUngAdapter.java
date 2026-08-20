package com.example.numberfindinggame.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.NhacHieuUng;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class NhacHieuUngAdapter
        extends ArrayAdapter<NhacHieuUng> {

    private final Context context;
    private final List<NhacHieuUng> dsNhacHieuUng;

    public NhacHieuUngAdapter(
            Context context,
            List<NhacHieuUng> dsNhacHieuUng
    ) {
        super(
                context,
                R.layout.item_am_thanh_hieu_ung,
                dsNhacHieuUng
        );

        this.context = context;
        this.dsNhacHieuUng = dsNhacHieuUng;

    }


    @NonNull
    @Override
    public View getView(
            int position,
            View convertView,
            @NonNull ViewGroup parent
    ) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(
                            R.layout.item_am_thanh_hieu_ung,
                            parent,
                            false
                    );
        }
        NhacHieuUng nhacHieuUng = dsNhacHieuUng.get(position);

        TextView txtTenNhacHieuUng = convertView.findViewById(R.id.txtTenNhacHieuUng);
        TextView txtGhiChu = convertView.findViewById(R.id.txtGhiChu);

        MaterialCardView cardNhacHieuUng = convertView.findViewById(R.id.cardNhacHieuUng);

        txtTenNhacHieuUng.setText(nhacHieuUng.getTenHieuUng());
        txtGhiChu.setText(nhacHieuUng.getGhiChu());

        if (SoundManager.getCurrentSound(context) == nhacHieuUng.getMaHieuUng()) {
            cardNhacHieuUng.setStrokeWidth(dpToPx(1));
        } else {
            cardNhacHieuUng.setStrokeWidth(dpToPx(0));
        }

        cardNhacHieuUng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SoundManager.changeButtonSound(
                        context,
                        nhacHieuUng.getMaHieuUng());
                ;

                SoundManager.playButton(context);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


}
