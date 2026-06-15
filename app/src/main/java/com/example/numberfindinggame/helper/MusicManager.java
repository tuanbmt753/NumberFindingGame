package com.example.numberfindinggame.helper;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.numberfindinggame.R;

public class MusicManager {

    private static MediaPlayer mediaPlayer;

    private static float volume = 1f;

    public static void play(Context context) {

        SessionManagerMusic session =
                new SessionManagerMusic(context);

        if (!session.isMusicEnabled()) {

            return;
        }

        volume =
                session.getMusicVolume() / 100f;

        if (mediaPlayer == null) {

            mediaPlayer =
                    MediaPlayer.create(
                            context.getApplicationContext(),
                            R.raw.nhac_nen);

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

        if (mediaPlayer != null) {

            mediaPlayer.setVolume(
                    volume,
                    volume);

            if (!mediaPlayer.isPlaying()) {

                mediaPlayer.start();
            }

        }

    }

    public static void stop() {

        if (mediaPlayer != null) {

            mediaPlayer.stop();

            mediaPlayer.release();

            mediaPlayer = null;
        }

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

        if (percent < 0)
            percent = 0;

        if (percent > 100)
            percent = 100;

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

}