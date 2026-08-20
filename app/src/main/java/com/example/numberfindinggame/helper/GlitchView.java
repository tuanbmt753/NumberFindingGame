package com.example.numberfindinggame.helper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

public class GlitchView extends View {

    private Paint paint = new Paint();
    private Random random = new Random();

    private boolean isRunning = false;

    public GlitchView(Context context) {
        super(context);
        init();
    }

    public GlitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setAntiAlias(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isRunning) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        // Nền đen hơi trong suốt
        paint.setColor(0x33000000);
        canvas.drawRect(0, 0, width, height, paint);

        // Tạo các sọc nhiễu
        for (int i = 0; i < 25; i++) {

            int y = random.nextInt(height);

            int stripeHeight = random.nextInt(20) + 2;

            int alpha = random.nextInt(150) + 30;

            int gray = random.nextInt(256);

            paint.setColor(
                    android.graphics.Color.argb(
                            alpha,
                            gray,
                            gray,
                            gray
                    )
            );

            int offsetX = random.nextInt(100) - 50;

            RectF rect = new RectF(
                    offsetX,
                    y,
                    width + offsetX,
                    y + stripeHeight
            );

            canvas.drawRect(rect, paint);
        }

        // Một số sọc trắng mạnh
        for (int i = 0; i < 5; i++) {

            int y = random.nextInt(height);

            int stripeHeight = random.nextInt(8) + 1;

            paint.setColor(
                    android.graphics.Color.argb(
                            random.nextInt(150) + 100,
                            255,
                            255,
                            255
                    )
            );

            canvas.drawRect(
                    0,
                    y,
                    width,
                    y + stripeHeight,
                    paint
            );
        }
    }

    public void startGlitch(long duration) {

        isRunning = true;

        ValueAnimator animator =
                ValueAnimator.ofFloat(0f, 1f);

        animator.setDuration(duration);

        animator.setInterpolator(
                new LinearInterpolator()
        );

        animator.addUpdateListener(animation -> {

            invalidate();

        });

        animator.start();

        postDelayed(() -> {

            isRunning = false;

            invalidate();

            setVisibility(GONE);

        }, duration);
    }
}