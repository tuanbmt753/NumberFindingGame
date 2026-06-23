package com.example.numberfindinggame.activity.music;

import android.app.Application;
import android.content.Context;

import com.cloudinary.android.MediaManager;

import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    private static Context context;

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
                        new AppLifecycleObserver()
                );
    }

    public static Context getContext() {
        return context;
    }
}