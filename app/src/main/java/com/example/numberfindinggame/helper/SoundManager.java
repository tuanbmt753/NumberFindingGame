package com.example.numberfindinggame.helper;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundManager {

    private static SoundPool soundPool;

    private static int buttonSound;

    public static void init(
            Context context) {

        AudioAttributes audioAttributes =
                new AudioAttributes.Builder()
                        .setUsage(
                                AudioAttributes.USAGE_GAME)
                        .setContentType(
                                AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();

        soundPool =
                new SoundPool.Builder()
                        .setMaxStreams(5)
                        .setAudioAttributes(
                                audioAttributes)
                        .build();

        SessionManagerSound session =
                new SessionManagerSound(
                        context);

        buttonSound =
                soundPool.load(
                        context,
                        session.getCurrentSound(),
                        1);

    }

    // Đổi hiệu ứng

    public static void changeSound(
            Context context,
            int soundResId) {

        SessionManagerSound session =
                new SessionManagerSound(
                        context);

        int currentSound =
                session.getCurrentSound();

        if (currentSound
                == soundResId) {

            return;

        }

        session.setCurrentSound(
                soundResId);

        buttonSound =
                soundPool.load(
                        context,
                        soundResId,
                        1);

    }

    // Âm lượng

    public static void setVolume(
            Context context,
            int percent) {

        if (percent < 0) {

            percent = 0;

        }

        if (percent > 100) {

            percent = 100;

        }

        SessionManagerSound session =
                new SessionManagerSound(
                        context);

        session.setVolume(
                percent);

    }

    // Bật tắt

    public static void setEnable(
            Context context,
            boolean enable) {

        SessionManagerSound session =
                new SessionManagerSound(
                        context);

        session.setEnable(
                enable);

    }

    // Phát hiệu ứng

    public static void playButton(
            Context context) {

        if (soundPool == null) {

            return;

        }

        SessionManagerSound session =
                new SessionManagerSound(
                        context);

        if (!session.isEnable()) {

            return;

        }

        float volume =
                session.getVolume()
                        / 100f;

        soundPool.play(
                buttonSound,
                volume,
                volume,
                1,
                0,
                1f);

    }

    // Giải phóng

    public static void release() {

        if (soundPool != null) {

            soundPool.release();

            soundPool = null;

        }

    }

}