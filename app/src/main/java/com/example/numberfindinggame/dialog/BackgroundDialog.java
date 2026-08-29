package com.example.numberfindinggame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.adapter.BackgroundAdapter;
import com.example.numberfindinggame.model.BackgroundItem;
import com.google.android.material.card.MaterialCardView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BackgroundDialog {

    private final Dialog dialog;
    private PlayerView playerView;
    private ExoPlayer exoPlayer;

    private GridView gridBackground;
    private List<BackgroundItem> list = new ArrayList<>();
    private BackgroundAdapter adapter;

    private Context context;
    private BackgroundDialogCallback callback;
    private BackgroundItem item = null;

    private MaterialCardView cardChonHinhNen;
    private TextView txtChonHinhNen;

    public BackgroundDialog(
            Context context,
            BackgroundDialogCallback callback
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_background);
        this.context = context;
        this.callback = callback;

        setControl();
        setEvent();


    }

    private void setEvent() {
        if (item != null) {
            txtChonHinhNen.setText("✔️");
        } else {
            txtChonHinhNen.setText("✖️");
        }

        exoPlayer = new ExoPlayer.Builder(context).build();
        playerView.setPlayer(exoPlayer);

        khoiTao();

        adapter = new BackgroundAdapter(context, list);
        gridBackground.setAdapter(adapter);

        gridBackground.setOnItemClickListener(
                (parent,
                 view,
                 position,
                 id) -> {
                    item = list.get(position);
                    phatVideoNen(item.getResId());

                    if (item != null) {
                        txtChonHinhNen.setText("✔️");
                    } else {
                        txtChonHinhNen.setText("✖️");
                    }
                    adapter.onSelectItem(item);
                    adapter.notifyDataSetChanged();

//                    BackgroundItem item = list.get(position);
//                    dialog.dismiss();
//
//                    if (callback != null) {
//
//                        callback.onSelect(item);
//
//                    }

                });

        cardChonHinhNen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item != null) {
                    dismiss();
                    if (callback != null) {
                        callback.onSelect(item);
                    }
                } else {
                    dismiss();
                }
            }
        });
    }

    private void khoiTao() {
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            String tenFile = field.getName();

            if (tenFile.startsWith("bg")) {
                try {

                    int resId = field.getInt(null);
                    list.add(new BackgroundItem(resId, tenFile));

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    private void setControl() {
        gridBackground = dialog.findViewById(R.id.gridBackground);

        playerView = dialog.findViewById(R.id.playerView);
        cardChonHinhNen = dialog.findViewById(R.id.cardChonHinhNen);
        txtChonHinhNen = dialog.findViewById(R.id.txtChonHinhNen);


    }

    public void show() {
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }


    private void phatVideoNen(int redID) {

        Uri uri = Uri.parse(
                "android.resource://"
                        + context.getPackageName()
                        + "/"
                        + redID);

        MediaItem mediaItem = MediaItem.fromUri(uri);

        exoPlayer.setMediaItem(mediaItem);

        // lặp vô hạn
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

//        // tắt tiếng nếu muốn
//        exoPlayer.setVolume(0f);

        exoPlayer.prepare();

        exoPlayer.play();
    }


    private void releasePlayer() {

        if (exoPlayer != null) {

            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (playerView != null) {
            playerView.setPlayer(null);
        }
    }

    public void dismiss() {

        releasePlayer();

        dialog.dismiss();
    }

}