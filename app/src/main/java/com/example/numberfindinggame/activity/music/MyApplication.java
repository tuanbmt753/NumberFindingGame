package com.example.numberfindinggame.activity.music;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MyApplication
        extends Application {

    private static Context context;

    @Override
    public void onCreate() {

        super.onCreate();

        context = this;

        ProcessLifecycleOwner
                .get()
                .getLifecycle()
                .addObserver(
                        new AppLifecycleObserver());

    }

    public static Context getContext(){

        return context;
    }

}