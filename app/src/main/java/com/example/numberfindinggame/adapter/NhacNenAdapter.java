package com.example.numberfindinggame.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.dialog.ConfirmDialogAnhNen;
import com.example.numberfindinggame.dialog.ConfirmDialogThongTinNhac;
import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.NhacNen;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class NhacNenAdapter
        extends ArrayAdapter<NhacNen> {

    private final Context context;
    private final List<NhacNen> dsNhacNen;

    public NhacNenAdapter(
            Context context,
            List<NhacNen> dsNhacNen
    ) {
        super(
                context,
                R.layout.item_nhac_nen,
                dsNhacNen
        );

        this.context = context;
        this.dsNhacNen = dsNhacNen;

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
                            R.layout.item_nhac_nen,
                            parent,
                            false
                    );
        }

        TextView txtTenNhacNen = convertView.findViewById(R.id.txtTenNhacNen);
        TextView txtGhiChu = convertView.findViewById(R.id.txtGhiChu);

        MaterialCardView cardNhacNen = convertView.findViewById(R.id.cardNhacNen);
        MaterialCardView cardThongTin = convertView.findViewById(R.id.cardThongTin);

        NhacNen nhacNen = dsNhacNen.get(position);
        txtTenNhacNen.setText("▶ " + nhacNen.getTxtTenNhacNen());
        txtGhiChu.setText(nhacNen.getTxtGhiChu());

        String currentMusicPath =
                MusicManager.getCurrentMusicPath(context);

        if (currentMusicPath == null
                || currentMusicPath.isEmpty()) {

            // Nhạc trong R.raw
            if (MusicManager.getCurrentMusic(context)
                    == nhacNen.getMaNhacNen()) {

                cardNhacNen.setStrokeWidth(
                        dpToPx(1));

            } else {

                cardNhacNen.setStrokeWidth(
                        dpToPx(0));

            }


        } else {

            // Nhạc ngoài
            if (currentMusicPath.equals(
                    nhacNen.getTxtGhiChu())) {

                cardNhacNen.setStrokeWidth(
                        dpToPx(1));

            } else {

                cardNhacNen.setStrokeWidth(
                        dpToPx(0));

            }


        }
        txtGhiChu.setVisibility(View.GONE);

        cardNhacNen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                if (nhacNen.getMaNhacNen() > 0) {
                    MusicManager.changeMusic(
                            context,
                            nhacNen.getMaNhacNen());
                } else {
                    MusicManager.changeMusic(
                            context,
                            nhacNen.getTxtGhiChu());
                }


                notifyDataSetChanged();
            }
        });

        cardThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nhacNen.getMaNhacNen() != -1) {
                    confirmDialogThongTin(nhacNen.getTxtTenNhacNen(), nhacNen.getLinhDrive(), nhacNen.getLinhYoutube(), nhacNen.getTxtGhiChu(), 1);
                } else {
                    confirmDialogThongTin(nhacNen.getTxtTenNhacNen(), nhacNen.getLinhDrive(), nhacNen.getLinhYoutube(), nhacNen.getTxtGhiChu(), 2);
                }

            }
        });


        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    private void confirmDialogThongTin(String title,
                                       String linkDrive,
                                       String linkYoutube,
                                       String linkAndroid,
                                       Integer luuTru) {
        new ConfirmDialogThongTinNhac(context,
                title,
                linkDrive,
                linkYoutube,
                linkAndroid,
                luuTru
        ).show();

    }


}
