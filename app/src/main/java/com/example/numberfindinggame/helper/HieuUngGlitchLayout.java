package com.example.numberfindinggame.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Random;

public class HieuUngGlitchLayout extends FrameLayout {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Random random = new Random();

    private Bitmap bitmapGoc;

    private boolean dangGlitch = false;

    private long thoiGianBatDau;
    private long thoiGianGlitch = 900;

    // Số lượng dải ngang bị xé
    private int soLuongSong = 22;

    // Độ lệch tối đa trái/phải
    private int doLechToiDa = 55;

    public HieuUngGlitchLayout(Context context) {
        super(context);
        init();
    }

    public HieuUngGlitchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HieuUngGlitchLayout(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        // Cho phép layout tự vẽ
        setWillNotDraw(false);

        paint.setFilterBitmap(false);

    }


    /**
     * Bắt đầu hiệu ứng glitch
     */
    public void batDauGlitch() {

        post(() -> {

            taoBitmapGoc();

            dangGlitch = true;

            thoiGianBatDau = System.currentTimeMillis();

            // Ẩn View con thật
            for (int i = 0; i < getChildCount(); i++) {

                getChildAt(i).setAlpha(0f);

            }

            invalidate();

        });
    }


    /**
     * Bắt đầu glitch với thời gian tùy chỉnh
     */
    public void batDauGlitch(long duration) {

        thoiGianGlitch = duration;

        batDauGlitch();
    }


    /**
     * Chụp toàn bộ layout thành Bitmap
     */
    private void taoBitmapGoc() {

        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        bitmapGoc = Bitmap.createBitmap(
                getWidth(),
                getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(bitmapGoc);

        // Vẽ tất cả View con vào Bitmap
        super.dispatchDraw(canvas);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (!dangGlitch || bitmapGoc == null) {

            super.dispatchDraw(canvas);

            return;
        }

        long thoiGianHienTai = System.currentTimeMillis();

        long daChay =
                thoiGianHienTai - thoiGianBatDau;


        // Hiệu ứng kết thúc
        if (daChay >= thoiGianGlitch) {

            dangGlitch = false;

            // Hiện lại View thật
            for (int i = 0; i < getChildCount(); i++) {

                getChildAt(i).setAlpha(1f);

            }

            bitmapGoc = null;

            super.dispatchDraw(canvas);

            return;
        }


        int width = getWidth();

        int height = getHeight();


        // =================================================
        // GIẢM DẦN CƯỜNG ĐỘ GLITCH
        // =================================================

        float tienDo =
                (float) daChay / thoiGianGlitch;

        float cuongDo =
                1f - tienDo;


        // =================================================
        // VẼ BITMAP BÌNH THƯỜNG NHẸ
        // =================================================

        float alphaFloat;

        if (tienDo < 0.35f) {

            alphaFloat = tienDo / 0.35f;

        } else {

            alphaFloat = 1f;

        }

        paint.setAlpha(
                (int) (255 * alphaFloat)
        );

        canvas.drawBitmap(
                bitmapGoc,
                0,
                0,
                paint
        );


        // =================================================
        // XÉ BITMAP THÀNH CÁC DẢI NGANG
        // =================================================

        for (int i = 0; i < soLuongSong; i++) {

            // Vị trí Y ngẫu nhiên
            int y =
                    random.nextInt(
                            Math.max(1, height)
                    );


            // Chiều cao dải bị xé
            int chieuCaoSong =
                    random.nextInt(18) + 3;


            if (y + chieuCaoSong > height) {

                chieuCaoSong =
                        height - y;

            }


            // Tính độ lệch
            int doLech =
                    (int) (
                            (random.nextInt(
                                    doLechToiDa * 2
                            )
                                    - doLechToiDa)
                                    * cuongDo
                    );


            Rect source = new Rect(
                    0,
                    y,
                    width,
                    y + chieuCaoSong
            );


            Rect destination = new Rect(
                    doLech,
                    y,
                    width + doLech,
                    y + chieuCaoSong
            );


            canvas.drawBitmap(
                    bitmapGoc,
                    source,
                    destination,
                    paint
            );
        }


        // =================================================
        // TẠO CÁC DẢI NHIỄU MẠNH
        // =================================================

        for (int i = 0; i < 5; i++) {

            int y =
                    random.nextInt(
                            Math.max(1, height)
                    );

            int chieuCao =
                    random.nextInt(5) + 1;


            paint.setColor(
                    0x55FFFFFF
            );

            canvas.drawRect(
                    0,
                    y,
                    width,
                    y + chieuCao,
                    paint
            );
        }


        // =================================================
        // SCAN LINE
        // =================================================

        paint.setColor(
                0x22000000
        );


        for (int y = 0; y < height; y += 4) {

            canvas.drawRect(
                    0,
                    y,
                    width,
                    y + 1,
                    paint
            );

        }


        paint.setAlpha(255);


        // Tiếp tục animation
        postInvalidateOnAnimation();
    }


    /**
     * Dừng hiệu ứng ngay lập tức
     */
    public void dungGlitch() {

        dangGlitch = false;

        bitmapGoc = null;


        for (int i = 0; i < getChildCount(); i++) {

            getChildAt(i).setAlpha(1f);

        }


        invalidate();
    }


    /**
     * Kiểm tra hiệu ứng đang chạy
     */
    public boolean dangGlitch() {

        return dangGlitch;

    }
}