package com.example.numberfindinggame.activity.music;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cloudinary.android.MediaManager;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.session.SessionManager;

import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    private static Context context;
    private String maNguoiDung = null;
    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();

    private Handler handler = new Handler(Looper.getMainLooper());

    private static final long THOI_GIAN_CAP_NHAT = 30 * 1000;

    private boolean dangChay = false;

    @Override
    public void onCreate() {

        super.onCreate();

        context = this;

        // ⭐ THÊM CLOUDINARY INIT Ở ĐÂY
        Map config = new HashMap();
        config.put("cloud_name", "dpacjldtr");

        MediaManager.init(this, config);

        ProcessLifecycleOwner
                .get()
                .getLifecycle()
                .addObserver(
                        new AppLifecycleObserver(
                                this
                        )
                );
    }

    public static Context getContext() {
        return context;
    }

    private final Runnable capNhatDangNhapCuoiRunnable = new Runnable() {
        @Override
        public void run() {

            if (!dangChay) {
                return;
            }

            maNguoiDung = SessionManager.getUserId(MyApplication.this);

            if (maNguoiDung != null && !maNguoiDung.isEmpty()) {

                nguoiDungRepository.capNhatDangNhapCuoi(
                        maNguoiDung
                );
            }

            // 30 giây sau cập nhật tiếp
            handler.postDelayed(
                    this,
                    THOI_GIAN_CAP_NHAT
            );
        }
    };

    // App vào foreground
    public void batDauTheoDoiOnline() {

        if (dangChay) {
            return;
        }

        dangChay = true;

        // Cập nhật ngay lập tức
        capNhatDangNhapCuoiRunnable.run();
    }

    // App xuống background
    public void dungTheoDoiOnline() {

        dangChay = false;

        handler.removeCallbacks(
                capNhatDangNhapCuoiRunnable
        );
    }
}