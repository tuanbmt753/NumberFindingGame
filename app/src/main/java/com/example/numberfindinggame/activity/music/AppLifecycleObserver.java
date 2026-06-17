package com.example.numberfindinggame.activity.music;


import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.numberfindinggame.helper.MusicManager;


public class AppLifecycleObserver
        implements DefaultLifecycleObserver {

    @Override
    public void onStart(
            LifecycleOwner owner) {

        MusicManager.resume(
                MyApplication.getContext());

    }

    @Override
    public void onStop(
            LifecycleOwner owner) {

        MusicManager.pause();

    }
}

