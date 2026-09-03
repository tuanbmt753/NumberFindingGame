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
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.utils.LoadingDialog;

public class XemAnhNenTinhActivity extends AppCompatActivity {

    private LinearLayout layoutBottom;
    private ImageView imgHinhNen;
    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private String maNguoiDung;
    private byte[] byteArrayHinh = new byte[0];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_anh_nen_tinh);

        setControl();
        setEvent();

    }

    private void setEvent() {
        maNguoiDung = SessionManager.getUserId(XemAnhNenTinhActivity.this);
        layThongTinNguoiDung(maNguoiDung);

        layoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(XemAnhNenTinhActivity.this
                        , ThongTinNguoiDungActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void layThongTinNguoiDung(String maNguoiDung) {
        if (!NetworkHelper.isConnected(XemAnhNenTinhActivity.this)) {

            MessageHelper.error(
                    XemAnhNenTinhActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(XemAnhNenTinhActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                maNguoiDung,
                task -> {

                    if (task.isSuccessful() && task.getResult().exists()) {
                        NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);


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

                        loading.dismiss();
                    }
                }
        );
    }


    private void setControl() {
        layoutBottom = findViewById(R.id.layoutBottom);
        imgHinhNen = findViewById(R.id.imgHinhNen);
    }


}