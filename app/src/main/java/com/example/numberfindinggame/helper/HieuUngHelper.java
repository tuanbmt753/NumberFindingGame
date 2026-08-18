package com.example.numberfindinggame.helper;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.recyclerview.widget.RecyclerView;

public class HieuUngHelper {

    public static void xuatHienLanLuot(
            RecyclerView recyclerView,
            long thoiGian,
            long doTre
    ) {
        recyclerView.post(() -> {

            for (int i = 0; i < recyclerView.getChildCount(); i++) {

                View view = recyclerView.getChildAt(i);

                view.setAlpha(0f);
                view.setScaleX(0.7f);
                view.setScaleY(0.7f);
                view.setTranslationY(80f);

                view.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationY(0f)
                        .setDuration(thoiGian)
                        .setStartDelay(i * doTre)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        });
    }

    private void hieuUngXuatHienRecyclerView(RecyclerView recyclerView) {
        recyclerView.post(() -> {

            for (int i = 0; i < recyclerView.getChildCount(); i++) {

                View view = recyclerView.getChildAt(i);

                view.setAlpha(0f);
                view.setScaleX(0.3f);
                view.setScaleY(0.3f);

                view.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(350)
                        .setStartDelay(i * 80L)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
            }
        });
    }
}