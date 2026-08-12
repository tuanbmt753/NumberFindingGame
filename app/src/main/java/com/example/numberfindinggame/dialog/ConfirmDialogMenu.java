package com.example.numberfindinggame.dialog;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.activity.manchoi.ManChoiActivity;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.activity.setting.SettingActivity;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;

public class ConfirmDialogMenu {

    private final Dialog dialog;
    private MaterialCardView cardTrangChu, cardMap, cardCaiDat, cardTaiKhoan, cardThoat;

    private Context context;

    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private ImageView imgAvatar, imgHinhNen;
    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private byte[] byteArrayHinh = new byte[0];
    private String maNguoiDung;

    private TextView txtTenNguoiDung;
    private Integer dangChoi = 0;

    public ConfirmDialogMenu(
            Context context
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(
                R.layout.layout_dialog_confirm_menu
        );

        dialog.setCancelable(false);
        this.context = context;

        setControl();
        setEvent();

    }

    public ConfirmDialogMenu(
            Context context, Integer dangChoi
    ) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(
                R.layout.layout_dialog_confirm_menu
        );

        dialog.setCancelable(false);
        this.context = context;
        this.dangChoi = dangChoi;

        setControl();
        setEvent();

    }

    private void setEvent() {
        exoPlayer = new ExoPlayer.Builder(context).build();
        playerView.setPlayer(exoPlayer);
        maNguoiDung = SessionManager.getUserId(context);
        layThongTinNguoiDung();

        cardTrangChu.setOnClickListener(v -> {
            chuyenManHinh(TrangChuActivity.class);
        });

        cardMap.setOnClickListener(v -> {
            chuyenManHinh(ManChoiActivity.class);
        });

        cardCaiDat.setOnClickListener(v -> {
            chuyenManHinh(SettingActivity.class);

        });

        cardTaiKhoan.setOnClickListener(v -> {
            chuyenManHinh(ThongTinNguoiDungActivity.class);
        });

        cardThoat.setOnClickListener(v -> {

            dismiss();
            SoundManager.playButton(context);

        });
    }

    private void chuyenManHinh(Class<?> dichDen) {
        if (dangChoi == 1) {
            new ConfirmDialog(
                    context,
                    "⚠️Xác nhận",
                    "Bạn đang trong trận, bạn có muốn thực hiện hành động này không? " +
                            "Thực hiện hành động sau sẽ mất tiến độ chơi hiện tại!",
                    new ConfirmDialog.ConfirmCallback() {

                        @Override
                        public void onYes() {
                            dismiss();
                            SoundManager.playButton(context);

                            Intent intent = new Intent(context, dichDen);
                            SoundManager.playButton(context);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }

                        @Override
                        public void onNo() {

                        }
                    }
            ).show();
        } else {
            dismiss();
            SoundManager.playButton(context);

            Intent intent = new Intent(context, dichDen);
            SoundManager.playButton(context);
            context.startActivity(intent);
            ((Activity) context).finish();
        }

    }

    private void layThongTinNguoiDung() {
        if (!NetworkHelper.isConnected(context)) {

            MessageHelper.error(
                    (Activity) context,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(context);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                maNguoiDung,
                task -> {

                    if (task.isSuccessful() && task.getResult().exists()) {
                        NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);
                        txtTenNguoiDung.setText(nguoiDung.getTenNguoiDung());

                        try {

                            if (!nguoiDung.getHinhDaiDien().isEmpty()) {
                                byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhDaiDien());
                                imgAvatar.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
                            } else {
                                imgAvatar.setImageResource(R.drawable.avatar_default);
                            }

                        } catch (Exception exception) {
                            imgAvatar.setImageResource(R.drawable.avatar_default);
                        }

                        if (isInteger(nguoiDung.getHinhNen()) == false) {

                            getPlayableVideoUrl(nguoiDung.getHinhNen(), nguoiDung);


                        } else {

                            phatVideoNen(Integer.parseInt(nguoiDung.getHinhNen()));
                            imgHinhNen.setVisibility(View.GONE);
                            playerView.setVisibility(View.VISIBLE);
                        }

                        loading.dismiss();


                    }
                }
        );
    }


    private void phatVideoNen(int redID) {

        Uri uri = Uri.parse(
                "android.resource://"
                        + context.getPackageName()
                        + "/"
                        + redID);

        MediaItem mediaItem =
                MediaItem.fromUri(uri);

        exoPlayer.setMediaItem(mediaItem);

        // lặp vô hạn
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

//        // tắt tiếng nếu muốn
//        exoPlayer.setVolume(0f);

        exoPlayer.prepare();

        exoPlayer.play();
    }

    private void getPlayableVideoUrl(String url, NguoiDung nguoiDung) {
        String oldPrefix =
                "https://res.cloudinary.com/dpacjldtr/video/upload";

        String newPrefix =
                "https://res.cloudinary.com/dpacjldtr/video/upload/f_mp4";

        if (url != null && url.startsWith(oldPrefix)) {

            imgHinhNen.setVisibility(View.GONE);
            playerView.setVisibility(View.VISIBLE);

            String videoUrl = url.replaceFirst(
                    oldPrefix,
                    newPrefix
            );

            MediaItem mediaItem =
                    MediaItem.fromUri(
                            videoUrl
                    );

            exoPlayer.setMediaItem(
                    mediaItem
            );
            // lặp vô hạn
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

//            // tắt tiếng nếu muốn
//            exoPlayer.setVolume(0f);

            exoPlayer.prepare();

            exoPlayer.play();

        } else {
            try {

                if (!nguoiDung.getHinhNen().isEmpty()) {
                    byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhNen());
                    imgHinhNen.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
                } else {
                    imgHinhNen.setImageResource(R.drawable.background_profile);
                }

            } catch (Exception exception) {
                imgHinhNen.setImageResource(R.drawable.background_profile);
            }

            imgHinhNen.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.GONE);
        }


    }

    private void setControl() {
        cardTrangChu = dialog.findViewById(R.id.cardTrangChu);
        cardMap = dialog.findViewById(R.id.cardMap);
        cardCaiDat = dialog.findViewById(R.id.cardCaiDat);
        cardTaiKhoan = dialog.findViewById(R.id.cardTaiKhoan);
        cardThoat = dialog.findViewById(R.id.cardThoat);

        imgAvatar = dialog.findViewById(R.id.imgAvatar);
        imgHinhNen = dialog.findViewById(R.id.imgHinhNen);

        playerView = dialog.findViewById(R.id.playerView);

        txtTenNguoiDung = dialog.findViewById(R.id.txtTenNguoiDung);

    }

    public static boolean isInteger(String s) {

        if (s == null || s.trim().isEmpty()) {

            return false;

        }

        try {

            Integer.parseInt(s);

            return true;

        } catch (NumberFormatException e) {

            return false;

        }

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