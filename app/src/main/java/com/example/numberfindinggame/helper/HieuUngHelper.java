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

    public static void glitchXuatHien(View view, long delay) {

        view.setAlpha(0f);
        view.setScaleX(1.1f);
        view.setScaleY(1.1f);

        view.postDelayed(() -> {

            view.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(500)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();

        }, delay);
    }

    public static void hieuUngGlitch(View view) {

        view.setAlpha(0f);

        view.postDelayed(() -> {

            view.setTranslationX(-20);

            view.animate()
                    .alpha(1f)
                    .translationX(20)
                    .setDuration(80)
                    .withEndAction(() -> {

                        view.animate()
                                .translationX(-10)
                                .setDuration(60)
                                .withEndAction(() -> {

                                    view.animate()
                                            .translationX(5)
                                            .setDuration(50)
                                            .withEndAction(() -> {

                                                view.animate()
                                                        .translationX(0)
                                                        .setDuration(150)
                                                        .start();

                                            })
                                            .start();

                                })
                                .start();

                    })
                    .start();

        }, 300);
    }
}