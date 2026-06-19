package com.example.numberfindinggame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.example.numberfindinggame.R;
import com.google.android.material.card.MaterialCardView;

public class ConfirmDialogAnhNen {

    private final Dialog dialog;

    public ConfirmDialogAnhNen(
            Context context,
            String title,
            String message,
            ConfirmCallback callback
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(
                R.layout.layout_dialog_confirm_chonanh
        );

        dialog.setCancelable(false);

        TextView txtTitle =
                dialog.findViewById(R.id.txtTitle);

        TextView txtMessage =
                dialog.findViewById(R.id.txtMessage);

        MaterialCardView cardNenTinh =
                dialog.findViewById(R.id.cardNenTinh);

        MaterialCardView cardNenDong =
                dialog.findViewById(R.id.cardNenDong);

        MaterialCardView cardNo =
                dialog.findViewById(R.id.cardNo);

        txtTitle.setText(title);
        txtMessage.setText(message);

        cardNenDong.setOnClickListener(v -> {

            dialog.dismiss();

            if (callback != null) {
                callback.onNenDong();
            }
        });

        cardNenTinh.setOnClickListener(v -> {

            dialog.dismiss();

            if (callback != null) {
                callback.onNenTinh();
            }
        });

        cardNo.setOnClickListener(v -> {

            dialog.dismiss();

            if (callback != null) {
                callback.onNo();
            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface ConfirmCallback {
        void onNenTinh();

        void onNenDong();

        void onNo();
    }
}