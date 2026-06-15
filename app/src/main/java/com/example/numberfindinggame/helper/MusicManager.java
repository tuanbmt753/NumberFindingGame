package com.example.numberfindinggame.helper;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.numberfindinggame.R;

public class MusicManager {

    private static MediaPlayer mediaPlayer;

    // 0f -> tắt tiếng
    // 1f -> âm lượng tối đa
    private static float volume = 1f;

    // Nhạc hiện tại


    public static void play(Context context) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        if (!session.isMusicEnabled()) {

            return;
        }

        volume =
                session.getMusicVolume() / 100f;

        if (mediaPlayer == null) {

            int currentMusicResId =
                    session.getCurrentMusic();

            mediaPlayer =
                    MediaPlayer.create(
                            context.getApplicationContext(),
                            currentMusicResId);

            if (mediaPlayer == null) {

                return;
            }

            mediaPlayer.setLooping(true);

            mediaPlayer.setVolume(
                    volume,
                    volume);
        }

        if (!mediaPlayer.isPlaying()) {

            mediaPlayer.start();
        }

    }


    public static void pause() {

        if (mediaPlayer != null &&
                mediaPlayer.isPlaying()) {

            mediaPlayer.pause();
        }

    }


    public static void resume(
            Context context) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        if (!session.isMusicEnabled()) {

            return;
        }

        volume =
                session.getMusicVolume() / 100f;

        if (mediaPlayer == null) {

            play(context);

            return;
        }

        mediaPlayer.setVolume(
                volume,
                volume);

        if (!mediaPlayer.isPlaying()) {

            mediaPlayer.start();
        }

    }


    public static void stop() {

        if (mediaPlayer != null) {

            try {

                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.stop();
                }

            } catch (Exception e) {

                e.printStackTrace();

            }

            mediaPlayer.release();

            mediaPlayer = null;
        }

    }


    public static boolean isPlaying() {

        return mediaPlayer != null
                && mediaPlayer.isPlaying();

    }


    public static void setEnabled(
            Context context,
            boolean enabled) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        session.setMusicEnabled(enabled);

        if (enabled) {

            play(context);

        } else {

            pause();

        }

    }


    public static boolean isEnabled(
            Context context) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        return session.isMusicEnabled();

    }


    public static void setVolume(
            Context context,
            int percent) {

        if (percent < 0) {

            percent = 0;
        }

        if (percent > 100) {

            percent = 100;
        }

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        session.setMusicVolume(percent);

        volume = percent / 100f;

        if (mediaPlayer != null) {

            mediaPlayer.setVolume(
                    volume,
                    volume);
        }

    }


    public static int getVolume(
            Context context) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        return session.getMusicVolume();

    }


    public static void changeMusic(
            Context context,
            int musicResId) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        int currentMusicResId =
                session.getCurrentMusic();

        if (currentMusicResId
                == musicResId) {

            return;
        }

        session.setCurrentMusic(
                musicResId);

        stop();

        play(context);

    }


    //Lấy nhạc hiện tại
    public static int getCurrentMusic(
            Context context) {

        return new SessionManagerMusic(
                context)
                .getCurrentMusic();

    }

    //Đổi nhạc mà không phát ngay
    public static void setCurrentMusic(
            Context context,
            int musicResId) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        session.setCurrentMusic(
                musicResId);

    }

}