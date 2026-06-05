package com.example.numberfindinggame.helper;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MessageHelper {

    public static void success(Activity activity, String message) {

        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                "✅ " + message,
                Snackbar.LENGTH_LONG
        );

        snackbar.setBackgroundTint(Color.WHITE);

        TextView textView = snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(Color.BLACK);

        snackbar.setAction("✕", v -> snackbar.dismiss());
        snackbar.setActionTextColor(Color.BLUE);

        snackbar.show();
    }

    public static void error(Activity activity, String message) {

        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                "❌ " + message,
                Snackbar.LENGTH_LONG
        );

        snackbar.setBackgroundTint(Color.WHITE);

        TextView textView = snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(Color.BLACK);

        snackbar.setAction("✕", v -> snackbar.dismiss());
        snackbar.setActionTextColor(Color.RED);

        snackbar.show();
    }

    public static void warning(Activity activity, String message) {

        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                "⚠️ " + message,
                Snackbar.LENGTH_LONG
        );

        snackbar.setBackgroundTint(Color.WHITE);

        TextView textView = snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(Color.BLACK);

        snackbar.setAction("✕", v -> snackbar.dismiss());
        snackbar.setActionTextColor(Color.parseColor("#FFA000"));

        snackbar.show();
    }

    public static void info(Activity activity, String message) {

        Snackbar snackbar = Snackbar.make(
                activity.findViewById(android.R.id.content),
                "ℹ️ " + message,
                Snackbar.LENGTH_LONG
        );

        snackbar.setBackgroundTint(Color.WHITE);

        TextView textView = snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(Color.BLACK);

        snackbar.setAction("✕", v -> snackbar.dismiss());
        snackbar.setActionTextColor(Color.BLUE);

        snackbar.show();
    }
}