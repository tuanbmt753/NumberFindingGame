package com.example.numberfindinggame.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;

public class LoadingDialog {

    private Dialog dialog;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_loading_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}