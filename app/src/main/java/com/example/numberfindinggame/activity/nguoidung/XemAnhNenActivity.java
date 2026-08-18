package com.example.numberfindinggame.activity.nguoidung;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.utils.LoadingDialog;

public class XemAnhNenActivity extends AppCompatActivity {

    private LinearLayout layoutBottom;
    private PlayerView playerView;
    private ImageView imgHinhNen;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private String maNguoiDung;

    private ExoPlayer exoPlayer;
    private byte[] byteArrayHinh = new byte[0];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_anh_nen);

        setControl();
        setEvent();

    }

    private void setEvent() {
        maNguoiDung = SessionManager.getUserId(XemAnhNenActivity.this);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        layThongTinNguoiDung(maNguoiDung);

        layoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(XemAnhNenActivity.this
                        , ThongTinNguoiDungActivity.class);
                startActivity(intent);
                finish();

            }
        });

        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (layoutBottom.getVisibility() == View.GONE) {
                    layoutBottom.setVisibility(View.VISIBLE);
                } else if (layoutBottom.getVisibility() == View.VISIBLE) {
                    layoutBottom.setVisibility(View.GONE);
                }

            }
        });


    }

    private void layThongTinNguoiDung(String maNguoiDung) {
        if (!NetworkHelper.isConnected(XemAnhNenActivity.this)) {

            MessageHelper.error(
                    XemAnhNenActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(XemAnhNenActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                maNguoiDung,
                task -> {

                    if (task.isSuccessful() && task.getResult().exists()) {
                        NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);


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
                        + getPackageName()
                        + "/"
                        + redID);

        MediaItem mediaItem =
                MediaItem.fromUri(uri);

        exoPlayer.setMediaItem(mediaItem);

        // lặp vô hạn
        exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

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


    private void setControl() {
        layoutBottom = findViewById(R.id.layoutBottom);
        playerView = findViewById(R.id.playerView);
        imgHinhNen = findViewById(R.id.imgHinhNen);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (exoPlayer != null) {

            exoPlayer.release();

            exoPlayer = null;
        }
    }
}