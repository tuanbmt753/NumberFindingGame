package com.example.numberfindinggame.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.example.numberfindinggame.R;

public class LoadingDialog {

    private final Dialog dialog;
    private final TextView tvLoading;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_loading_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        tvLoading = dialog.findViewById(R.id.tvLoading);
    }

    public void setMessage(String message) {
        tvLoading.setText(message);
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}