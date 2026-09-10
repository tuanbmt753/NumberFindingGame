package com.example.numberfindinggame.activity.music;


import android.content.Context;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.numberfindinggame.manager.MusicManager;
import com.example.numberfindinggame.manager.SoundManager;


public class AppLifecycleObserver
        implements DefaultLifecycleObserver {

    private final Context context;

    public AppLifecycleObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onStart(
            LifecycleOwner owner) {

        MusicManager.resume(
                MyApplication.getContext());
        SoundManager.setEnable(context, true);

        // App vào foreground
        MyApplication app =
                (MyApplication) context.getApplicationContext();

        app.batDauTheoDoiOnline();
    }

    @Override
    public void onStop(
            LifecycleOwner owner) {

        MusicManager.pause();
        SoundManager.setEnable(context, false);

        // App xuống background
        MyApplication app =
                (MyApplication) context.getApplicationContext();

        app.dungTheoDoiOnline();

    }
}

