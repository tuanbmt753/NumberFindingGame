package com.example.numberfindinggame.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.model.BackgroundItem;
import com.example.numberfindinggame.model.LinkHinhNenDong;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DuongDanHinhNenDongAdapter extends ArrayAdapter<LinkHinhNenDong> {

    private final Context context;

    private final List<LinkHinhNenDong> dsLinkHinhNenDong;

    public DuongDanHinhNenDongAdapter(
            Context context,
            List<LinkHinhNenDong> dsLinkHinhNenDong) {
        super(
                context,
                R.layout.item_link_hinh_nen_dong,
                dsLinkHinhNenDong
        );
        this.context = context;
        this.dsLinkHinhNenDong = dsLinkHinhNenDong;

    }


    @NonNull
    @Override
    public View getView(

            int position,

            View convertView,

            @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_link_hinh_nen_dong, parent, false);
        }

        TextView txtLink = convertView.findViewById(R.id.txtLink);

        LinkHinhNenDong item = dsLinkHinhNenDong.get(position);
        txtLink.setText("▶ " + item.getTenLink());

        txtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(item.getDuongDanLink())
                );

                context.startActivity(intent);
            }
        });

        return convertView;

    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}