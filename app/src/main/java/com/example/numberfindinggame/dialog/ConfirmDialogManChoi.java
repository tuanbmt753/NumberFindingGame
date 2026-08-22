package com.example.numberfindinggame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.numberfindinggame.R;
import com.google.android.material.card.MaterialCardView;

public class ConfirmDialogManChoi {

    private final Dialog dialog;

    public ConfirmDialogManChoi(
            Context context,
            String title,
            String message,
            Integer hanhDong,
            ConfirmCallback callback
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(
                R.layout.layout_dialog_confirm_man_choi
        );

        dialog.setCancelable(false);

        TextView txtTitle =
                dialog.findViewById(R.id.txtTitle3);

        TextView txtMessage =
                dialog.findViewById(R.id.txtMessage);

        MaterialCardView cardMapChoi =
                dialog.findViewById(R.id.cardMapChoi);

        MaterialCardView cardTiepTheo =
                dialog.findViewById(R.id.cardTiepTheo);

        MaterialCardView cardChoiLai =
                dialog.findViewById(R.id.cardChoiLai);


        txtTitle.setText(title);
        txtMessage.setText(message);

        if (hanhDong != 0) {
            if (hanhDong == 2) {
                cardTiepTheo.setVisibility(View.GONE);
            }
        }

        cardMapChoi.setOnClickListener(v -> {

            dialog.dismiss();

            if (callback != null) {
                callback.onManChoi();
            }
        });

        cardTiepTheo.setOnClickListener(v -> {

            dialog.dismiss();

            if (callback != null) {
                callback.onTiepTheo();
            }
        });

        cardChoiLai.setOnClickListener(v -> {

            dialog.dismiss();

            if (callback != null) {
                callback.onChoiLai();
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

    public interface ConfirmCallback {
        void onManChoi();

        void onChoiLai();

        void onTiepTheo();


    }
}