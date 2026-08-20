package com.example.numberfindinggame.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.example.numberfindinggame.session.SessionManagerMusic;

public class MusicManager {

    private static MediaPlayer mediaPlayer;

    private static float volume = 1f;


    public static void play(
            Context context) {

        SessionManagerMusic session =
                new SessionManagerMusic(
                        context);

        if (!session.isMusicEnabled()) {

            return;

        }


        volume =
                session.getMusicVolume()
                        / 100f;


        if (mediaPlayer == null) {

            String musicPath =
                    session.getCurrentMusicPath();


            // phát file ngoài
            if (!TextUtils.isEmpty(
                    musicPath)) {

                playFromPath(
                        context,
                        musicPath);

                return;

            }


            // phát file R.raw
            int currentMusicResId =
                    session.getCurrentMusic();


            mediaPlayer =
                    MediaPlayer.create(
                            context.getApplicationContext(),

                            currentMusicResId

                    );


            if (mediaPlayer == null) {

                return;

            }


            mediaPlayer.setLooping(
                    true);

            mediaPlayer.setVolume(
                    volume,
                    volume);

        }


        if (!mediaPlayer.isPlaying()) {

            mediaPlayer.start();

        }

    }


    public static void playFromPath(
            Context context,
            String musicPath) {

        SessionManagerMusic session =
                new SessionManagerMusic(
                        context);

        if (!session.isMusicEnabled()) {

            return;

        }


        try {

            volume =
                    session
                            .getMusicVolume()
                            / 100f;


            stop();


            mediaPlayer =
                    new MediaPlayer();


            mediaPlayer.setDataSource(
                    musicPath);


            mediaPlayer.prepare();


            mediaPlayer.setLooping(
                    true);


            mediaPlayer.setVolume(
                    volume,
                    volume);


            mediaPlayer.start();


        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }


    public static void pause() {

        if (mediaPlayer != null
                &&
                mediaPlayer.isPlaying()) {

            mediaPlayer.pause();

        }

    }


    public static void resume(
            Context context) {

        SessionManagerMusic session =
                new SessionManagerMusic(
                        context);


        if (!session.isMusicEnabled()) {

            return;

        }


        volume =
                session
                        .getMusicVolume()
                        / 100f;


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

            }

            catch (Exception e) {

                e.printStackTrace();

            }


            mediaPlayer.release();

            mediaPlayer = null;

        }

    }


    public static boolean isPlaying() {

        return mediaPlayer != null

                &&

                mediaPlayer.isPlaying();

    }


    public static void setEnabled(
            Context context,
            boolean enabled) {

        SessionManagerMusic session =
                new SessionManagerMusic(
                        context);


        session.setMusicEnabled(
                enabled);


        if (enabled) {

            play(context);

        }

        else {

            pause();

        }

    }


    public static boolean isEnabled(
            Context context) {

        return new SessionManagerMusic(
                context)

                .isMusicEnabled();

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
                new SessionManagerMusic(
                        context);


        session.setMusicVolume(
                percent);


        volume =
                percent / 100f;


        if (mediaPlayer != null) {

            mediaPlayer.setVolume(
                    volume,
                    volume);

        }

    }


    public static int getVolume(
            Context context) {

        return new SessionManagerMusic(
                context)

                .getMusicVolume();

    }


    // đổi nhạc R.raw
    public static void changeMusic(
            Context context,
            int musicResId) {

        SessionManagerMusic session =
                new SessionManagerMusic(
                        context);


        session.setCurrentMusic(
                musicResId);


        session.setCurrentMusicPath(
                "");

        stop();


        play(context);

    }


    // đổi nhạc file ngoài
    public static void changeMusic(
            Context context,
            String musicPath) {

        SessionManagerMusic session =
                new SessionManagerMusic(
                        context);


        session.setCurrentMusicPath(
                musicPath);


        stop();


        play(context);

    }


    public static int getCurrentMusic(
            Context context) {

        return new SessionManagerMusic(
                context)

                .getCurrentMusic();

    }


    public static String getCurrentMusicPath(
            Context context) {

        return new SessionManagerMusic(
                context)

                .getCurrentMusicPath();

    }

}