package com.example.numberfindinggame.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.helper.MessageHelper;
import com.google.android.material.card.MaterialCardView;

import java.io.File;

public class ConfirmDialogThongTinNhac {

    private final Dialog dialog;

    public ConfirmDialogThongTinNhac(
            Context context,
            String title,
            String linkDrive,
            String linkYoutube,
            String linkAndroid,
            Integer luuTru
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(
                R.layout.layout_dialog_thong_tin_nhac
        );

        dialog.setCancelable(false);


        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtLinkDrive = dialog.findViewById(R.id.txtLinkDrive);
        TextView txtLinkYouTube = dialog.findViewById(R.id.txtLinkYouTube);
        TextView txtLinkAndroid = dialog.findViewById(R.id.txtLinkAndroid);

        LinearLayout layoutLinkDrive = dialog.findViewById(R.id.layoutLinkDrive);
        LinearLayout layoutLinkYouTube = dialog.findViewById(R.id.layoutLinkYouTube);
        LinearLayout layoutLinkAndroid = dialog.findViewById(R.id.layoutLinkAndroid);

        if (luuTru == 1) {

            layoutLinkDrive.setVisibility(View.VISIBLE);
            layoutLinkYouTube.setVisibility(View.VISIBLE);
            layoutLinkAndroid.setVisibility(View.GONE);

        } else {

            layoutLinkDrive.setVisibility(View.GONE);
            layoutLinkYouTube.setVisibility(View.GONE);
            layoutLinkAndroid.setVisibility(View.VISIBLE);
            txtLinkAndroid.setText("Xem trên máy \n" + linkAndroid);
        }

        txtTitle.setText(title);

        MaterialCardView cardThoat = dialog.findViewById(R.id.cardThoat);
        cardThoat.setOnClickListener(v -> {

            dialog.dismiss();

        });

        // Google Drive
        txtLinkDrive.setTextColor(Color.BLUE);

        txtLinkDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(linkDrive)
                );

                context.startActivity(intent);
            }
        });

        // YouTube
        txtLinkYouTube.setTextColor(Color.BLUE);

        txtLinkYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(linkYoutube)
                );

                context.startActivity(intent);
            }
        });

        // linkAndroid
        txtLinkAndroid.setTextColor(Color.BLUE);

        txtLinkAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    File file = new File(linkAndroid);

                    if (!file.exists()) {

                        MessageHelper.info((Activity) context, "Không tìm thấy file trên thiết bị!");

                        return;
                    }

                    Uri uri = FileProvider.getUriForFile(
                            context,
                            context.getPackageName() + ".provider",
                            file
                    );

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "audio/*");

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    context.startActivity(
                            Intent.createChooser(intent, "Mở file bằng")
                    );

                } catch (Exception e) {

                    MessageHelper.error((Activity) context, "Không thể mở file!");

                    e.printStackTrace();
                }
            }
        });

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