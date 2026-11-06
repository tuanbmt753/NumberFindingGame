package com.example.numberfindinggame.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.adapter.DuongDanHinhNenDongAdapter;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.model.LinkHinhNenDong;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;

public class ConfirmDialogThongTinHinhNenDong {

    private final Dialog dialog;
    private TextView txtTitle;
    private MaterialCardView cardThoat;
    private String title;

    private ListView lvLink;

    private ArrayList<LinkHinhNenDong> dsLinkHinhNenDong = new ArrayList<>();
    private DuongDanHinhNenDongAdapter duongDanHinhNenDongAdapter;

    private Context context;

    public ConfirmDialogThongTinHinhNenDong(
            Context context,
            String title,
            ArrayList<LinkHinhNenDong> dsLinkHinhNenDong
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(
                R.layout.layout_dialog_thong_tin_hinh_nen
        );

        dialog.setCancelable(false);
        this.title = title;
        this.dsLinkHinhNenDong.addAll(dsLinkHinhNenDong);
        this.context = context;

        setControl();
        setEvent();
    }

    private void setEvent() {
        khoiTao();
        txtTitle.setText(title);

        cardThoat.setOnClickListener(v -> {

            dialog.dismiss();

        });
    }

    private void khoiTao() {
        duongDanHinhNenDongAdapter = new DuongDanHinhNenDongAdapter(context, dsLinkHinhNenDong);
        lvLink.setAdapter(duongDanHinhNenDongAdapter);

        ListViewHelper
                .setListViewHeightBasedOnChildren(
                        lvLink
                );
    }

    private void setControl() {
        txtTitle = dialog.findViewById(R.id.txtTitle);

        cardThoat = dialog.findViewById(R.id.cardThoat);

        lvLink = dialog.findViewById(R.id.lvLink);
    }

    public void show() {
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public void dismiss() {
        dialog.dismiss();
    }

}