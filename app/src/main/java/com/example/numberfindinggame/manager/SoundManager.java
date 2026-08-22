package com.example.numberfindinggame.manager;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.session.SessionManagerSound;

public class SoundManager {

    private static SoundPool soundPool;

    // load sẵn

    private static int click1;

    private static int click2;

    private static int click3;
    private static int click4;

    private static int win;

    private static int lose;

    private static int coin;

    private static int electric;

    // âm thanh nút đang dùng

    private static int currentButtonSound;

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
                        .setMaxStreams(10)
                        .setAudioAttributes(
                                audioAttributes)
                        .build();


        // Load một lần

        click1 =
                soundPool.load(
                        context,
                        R.raw.click_1,
                        1);

        click2 =
                soundPool.load(
                        context,
                        R.raw.click_2,
                        1);

        click3 =
                soundPool.load(
                        context,
                        R.raw.click_3,
                        1);

        click4 =
                soundPool.load(
                        context,
                        R.raw.click_4,
                        1);

        win =
                soundPool.load(
                        context,
                        R.raw.win,
                        1);

        lose =
                soundPool.load(
                        context,
                        R.raw.lose,
                        1);

        coin =
                soundPool.load(
                        context,
                        R.raw.coin,
                        1);

        electric =
                soundPool.load(
                        context,
                        R.raw.electric,
                        1);


        // lấy âm thanh đã lưu

        SessionManagerSound session =
                new SessionManagerSound(
                        context);

        changeButtonSound(
                context,
                session.getCurrentSound());

    }


    // Đổi hiệu ứng nút
    public static void changeButtonSound(
            Context context,
            int soundResId) {

        SessionManagerSound session =
                new SessionManagerSound(context);

        session.setCurrentSound(soundResId);

        if (soundResId == R.raw.click_1) {

            currentButtonSound = click1;

        } else if (soundResId == R.raw.click_2) {

            currentButtonSound = click2;


        } else if (soundResId == R.raw.click_3) {

            currentButtonSound = click3;


        } else if (soundResId == R.raw.click_4) {

            currentButtonSound = click4;

        } else {

            currentButtonSound = click1;

        }

    }


    // Phát hiệu ứng nút
    public static void playButton(
            Context context) {

        play(
                context,
                currentButtonSound);

    }


// Phát thắng

    public static void playWin(
            Context context) {

        play(
                context,
                win);

    }


// Phát thua

    public static void playLose(
            Context context) {

        play(
                context,
                lose);

    }


// Phát coin

    public static void playCoin(
            Context context) {

        play(
                context,
                coin);

    }

    public static void playElectric(
            Context context) {

        play(
                context,
                electric);

    }


// Hàm phát chung

    private static void play(
            Context context,
            int soundId) {

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
                soundId,
                volume,
                volume,
                1,
                0,
                1f);

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


    public static void release() {

        if (soundPool != null) {

            soundPool.release();

            soundPool = null;

        }

    }

    //Lấy nhạc hiện tại
    public static int getCurrentSound(
            Context context) {

        return new SessionManagerSound(
                context)
                .getCurrentSound();

    }

}