package com.example.numberfindinggame.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.HieuUng;
import com.example.numberfindinggame.model.NhacNen;
import com.example.numberfindinggame.session.HieuUngSession;
import com.example.numberfindinggame.session.NhacHieuUngNenSession;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HieuUngAdapter
        extends ArrayAdapter<HieuUng> {

    private final Context context;
    private final List<HieuUng> dsHieuUng;
    private HieuUngSession hieuUngSession;
    private NhacHieuUngNenSession nhacHieuUngNenSession;

    public HieuUngAdapter(
            Context context,
            List<HieuUng> dsHieuUng
    ) {
        super(
                context,
                R.layout.item_hieu_ung,
                dsHieuUng
        );

        this.context = context;
        this.dsHieuUng = dsHieuUng;
        hieuUngSession = new HieuUngSession(context);
        nhacHieuUngNenSession = new NhacHieuUngNenSession(context);
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
                            R.layout.item_hieu_ung,
                            parent,
                            false
                    );
        }

        TextView txtTenHieuUng = convertView.findViewById(R.id.txtTenHieuUng);
        TextView txtGhiChu = convertView.findViewById(R.id.txtGhiChu);
        TextView txtAmThanhHieuUng = convertView.findViewById(R.id.txtAmThanhHieuUng);

        MaterialCardView cardHieuUng = convertView.findViewById(R.id.cardHieuUng);
        MaterialCardView cardAmThanhHieuUng = convertView.findViewById(R.id.cardAmThanhHieuUng);

        HieuUng hieuUng = dsHieuUng.get(position);
        txtTenHieuUng.setText(hieuUng.getTenHieuUng());
        txtGhiChu.setText(hieuUng.getGhiChu());

        Integer hieuUngHienTai =
                hieuUngSession.getHieuUng();

        if (hieuUngHienTai > 0) {

            if (hieuUngHienTai
                    == hieuUng.getMaHieuUng()) {

                cardHieuUng.setStrokeWidth(
                        dpToPx(1));

                if (hieuUng.getMaHieuUng() == 1) {
                    cardAmThanhHieuUng.setVisibility(View.GONE);
                } else {
                    cardAmThanhHieuUng.setVisibility(View.VISIBLE);
                }

                boolean dangBat =
                        nhacHieuUngNenSession.isNhacHieuUng();
                if (dangBat) {
                    //BẬT
                    txtAmThanhHieuUng.setText("\uD83C\uDFB5");

                } else {
                    //TẮT
                    txtAmThanhHieuUng.setText("\uD83D\uDD07");
                }

            } else {

                cardHieuUng.setStrokeWidth(
                        dpToPx(0));
                cardAmThanhHieuUng.setVisibility(View.GONE);

            }

        }
        txtGhiChu.setVisibility(View.GONE);

        cardHieuUng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                hieuUngSession.setHieuUng(hieuUng.getMaHieuUng());
                notifyDataSetChanged();
            }
        });

        cardAmThanhHieuUng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean dangBat =
                        nhacHieuUngNenSession.isNhacHieuUng();

                // Đảo trạng thái
                nhacHieuUngNenSession.setNhacHieuUng(!dangBat);

                if (!dangBat) {
                    // Sau khi nhấn -> BẬT
                    txtAmThanhHieuUng.setText("\uD83C\uDFB5");

                } else {
                    // Sau khi nhấn -> TẮT
                    txtAmThanhHieuUng.setText("\uD83D\uDD07");
                }
            }
        });


        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


}
