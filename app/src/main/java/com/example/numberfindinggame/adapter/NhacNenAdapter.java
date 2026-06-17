package com.example.numberfindinggame.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.constant.MusicType;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.MusicManager;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.helper.SessionManagerSetting;
import com.example.numberfindinggame.model.NhacNen;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.example.numberfindinggame.utils.DateUtils;
import com.example.numberfindinggame.utils.LoadingDialog;
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

        NhacNen nhacNen = dsNhacNen.get(position);
        txtTenNhacNen.setText(nhacNen.getTxtTenNhacNen());
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


        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


}
