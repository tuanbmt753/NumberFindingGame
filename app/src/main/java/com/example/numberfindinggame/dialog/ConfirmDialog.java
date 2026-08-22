package com.example.numberfindinggame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.example.numberfindinggame.R;
import com.google.android.material.card.MaterialCardView;

public class ConfirmDialog {

    private final Dialog dialog;

    public ConfirmDialog(
            Context context,
            String title,
            String message,
            ConfirmCallback callback
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(
                R.layout.layout_dialog_confirm
        );

        dialog.setCancelable(false);

        TextView txtTitle =
                dialog.findViewById(R.id.txtTitle3);

        TextView txtMessage =
                dialog.findViewById(R.id.txtMessage);

        MaterialCardView cardYes =
                dialog.findViewById(R.id.cardYes);

        MaterialCardView cardNo =
                dialog.findViewById(R.id.cardNo);

        txtTitle.setText(title);
        txtMessage.setText(message);

        cardYes.setOnClickListener(v -> {

            dialog.dismiss();

            if (callback != null) {
                callback.onYes();
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
        void onYes();

        void onNo();
    }
}