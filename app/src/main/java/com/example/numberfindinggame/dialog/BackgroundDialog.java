package com.example.numberfindinggame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.adapter.BackgroundAdapter;
import com.example.numberfindinggame.constant.DanhSachHinhNenDong;
import com.example.numberfindinggame.constant.LinkType;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.model.BackgroundItem;
import com.example.numberfindinggame.model.NhacNen;
import com.example.numberfindinggame.session.NhacNenDongSession;
import com.google.android.material.card.MaterialCardView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BackgroundDialog {

    private final Dialog dialog;
    private PlayerView playerView;
    private PlayerView playerView2;

    private LinearLayout layoutPlayerView;
    private ExoPlayer exoPlayer;
    private ExoPlayer exoPlayer2;

    private ListView lvBackground;
    private List<BackgroundItem> list = new ArrayList<>();
    private List<BackgroundItem> listGoc = new ArrayList<>();

    private BackgroundAdapter adapter;

    private Context context;
    private BackgroundDialogCallback callback;
    private BackgroundItem item = null;

    private MaterialCardView cardChonHinhNen;
    private MaterialCardView cardThuNhoVideo;
    private TextView txtChonHinhNen;

    private DanhSachHinhNenDong danhSachHinhNenDong;

    private EditText edtTimKiemNhacNen;
    private NhacNenDongSession nhacNenDongSession;
    private ImageView imgThuNhoVideo;

    private boolean dangPhatPlayer2 = false;
    private ExoPlayer exoPlayer3;

    public BackgroundDialog(
            Context context,
            ExoPlayer exoPlayer3,
            BackgroundDialogCallback callback

    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_background);
        this.context = context;
        this.callback = callback;
        this.exoPlayer3 = exoPlayer3;

        exoPlayer3.pause();

        setControl();
        setEvent();
    }

    private void setEvent() {
        layoutPlayerView.setVisibility(View.GONE);
        nhacNenDongSession = new NhacNenDongSession(context);
        danhSachHinhNenDong = new DanhSachHinhNenDong();
        if (item != null) {
            txtChonHinhNen.setText("✔️");
        } else {
            txtChonHinhNen.setText("✖️");
        }

        exoPlayer = new ExoPlayer.Builder(context).build();
        playerView.setPlayer(exoPlayer);

        exoPlayer2 = new ExoPlayer.Builder(context).build();
        playerView2.setPlayer(exoPlayer2);


        khoiTao();

        BackgroundItem backgroundItem = danhSachHinhNenDong.getDsBackgroundItem().get(0);
        phatVideoNen(backgroundItem.getResId());


        cardThuNhoVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dangPhatPlayer2 == false) {
                    dangPhatPlayer2 = true;
                    imgThuNhoVideo.setImageResource(R.drawable.ic_phong_to);
                    chuyenSangPlayer2();

                } else {
                    dangPhatPlayer2 = false;
                    imgThuNhoVideo.setImageResource(R.drawable.ic_thu_nho);
                    chuyenLaiPlayer1();
                }
            }
        });


        lvBackground.setOnItemClickListener(
                (parent, view, position, id) -> {

                    item = list.get(position);

                    if (item != null) {

                        boolean dangPhat2 = dangPhatPlayer2;

                        chuyenLaiPlayer1();
                        phatVideoNen(item.getResId());

                        if (dangPhat2 == true) {
                            chuyenSangPlayer2();
                        }

                        txtChonHinhNen.setText("✔️");

                        adapter.onSelectItem(item);
                        adapter.notifyDataSetChanged();
                    }
                }
        );

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
                    exoPlayer3.play();
                }
            }
        });

        edtTimKiemNhacNen.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        String tuKhoa = s.toString().trim().toLowerCase();

                        list.clear();

                        // Nếu ô tìm kiếm trống
                        if (tuKhoa.isEmpty()) {
                            list.addAll(listGoc);

                        } else {

                            for (BackgroundItem backgroundItem : listGoc) {

                                String name = backgroundItem.getName();

                                if (name == null) {
                                    name = "";
                                }

                                name = name.toLowerCase();

                                // Tìm tương đối
                                if (name.contains(tuKhoa)) {

                                    list.add(backgroundItem);
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();

                        ListViewHelper.setListViewHeightBasedOnChildren(
                                lvBackground
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );
    }

    private void khoiTao() {
        list.clear();
        list.addAll(danhSachHinhNenDong.getDsBackgroundItem());

        listGoc.clear();
        listGoc.addAll(danhSachHinhNenDong.getDsBackgroundItem());

        adapter = new BackgroundAdapter(context, list);
        lvBackground.setAdapter(adapter);

        ListViewHelper
                .setListViewHeightBasedOnChildren(
                        lvBackground
                );
    }

    private void loadLaiDanhSachHinhNenDong() {
        list.clear();
        list.addAll(danhSachHinhNenDong.getDsBackgroundItem());

        listGoc.clear();
        listGoc.addAll(danhSachHinhNenDong.getDsBackgroundItem());

        adapter.notifyDataSetChanged();

        ListViewHelper
                .setListViewHeightBasedOnChildren(
                        lvBackground
                );
    }


    private void setControl() {
        lvBackground = dialog.findViewById(R.id.lvBackground);

        playerView = dialog.findViewById(R.id.playerView);
        playerView2 = dialog.findViewById(R.id.playerView2);

        layoutPlayerView = dialog.findViewById(R.id.layoutPlayerView);


        cardChonHinhNen = dialog.findViewById(R.id.cardChonHinhNen);
        cardThuNhoVideo = dialog.findViewById(R.id.cardThuNhoVideo);

        txtChonHinhNen = dialog.findViewById(R.id.txtChonHinhNen);

        imgThuNhoVideo = dialog.findViewById(R.id.imgThuNhoVideo);

        edtTimKiemNhacNen = dialog.findViewById(R.id.edtTimKiemNhacNen);
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

        // tắt tiếng nếu muốn  exoPlayer.setVolume(0f);
        // Lấy âm lượng đã lưu
        int amLuong = nhacNenDongSession.getAmLuong();
        float volume = amLuong / 100f;
        exoPlayer.setVolume(volume);


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


        if (exoPlayer2 != null) {

            exoPlayer2.stop();
            exoPlayer2.release();
            exoPlayer2 = null;
        }

        if (playerView2 != null) {
            playerView2.setPlayer(null);
        }
    }

    public void dismiss() {

        releasePlayer();

        dialog.dismiss();
    }

    private void chuyenSangPlayer2() {

        if (exoPlayer == null || exoPlayer2 == null) {
            return;
        }

        MediaItem mediaItem = exoPlayer.getCurrentMediaItem();

        if (mediaItem == null) {
            return;
        }

        long currentPosition = exoPlayer.getCurrentPosition();

        // Player chính dừng
        exoPlayer.pause();

        // Hiện PlayerView2
        layoutPlayerView.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);

        // Player2 dùng cùng video
        exoPlayer2.setMediaItem(mediaItem);
        exoPlayer2.setRepeatMode(Player.REPEAT_MODE_ALL);

        // Âm lượng
        int amLuong = nhacNenDongSession.getAmLuong();
        float volume = amLuong / 100f;
        exoPlayer2.setVolume(volume);

        exoPlayer2.prepare();

        // Phát tiếp
        exoPlayer2.seekTo(currentPosition);
        exoPlayer2.play();

        dangPhatPlayer2 = true;
    }

    private void chuyenLaiPlayer1() {

        if (exoPlayer == null || exoPlayer2 == null) {
            return;
        }

        long currentPosition = exoPlayer2.getCurrentPosition();

        // Player2 dừng
        exoPlayer2.pause();

        // Player2 ẩn
        layoutPlayerView.setVisibility(View.GONE);
        playerView.setVisibility(View.VISIBLE);

        // Player chính tiếp tục
        exoPlayer.seekTo(currentPosition);
        exoPlayer.play();

        dangPhatPlayer2 = false;
    }


}