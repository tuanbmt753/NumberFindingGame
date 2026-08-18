package com.example.numberfindinggame.activity.nguoidung;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.utils.LoadingDialog;

public class XemAnhDaiDienActivity extends AppCompatActivity {

    private LinearLayout layoutBottom;
    private ImageView imgAvatar;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private byte[] byteArrayHinh = new byte[0];
    private String maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xem_anh_dai_dien);

        setControl();
        setEvent();


    }

    private void setEvent() {
        maNguoiDung = SessionManager.getUserId(XemAnhDaiDienActivity.this);
        layThongTinNguoiDung(maNguoiDung);

        layoutBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(XemAnhDaiDienActivity.this
                        , ThongTinNguoiDungActivity.class);
                startActivity(intent);
                finish();

            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
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
        if (!NetworkHelper.isConnected(XemAnhDaiDienActivity.this)) {

            MessageHelper.error(
                    XemAnhDaiDienActivity.this,
                    "Không có kết nối Internet"
            );

            return;
        }

        LoadingDialog loading =
                new LoadingDialog(XemAnhDaiDienActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                maNguoiDung,
                task -> {

                    if (task.isSuccessful() && task.getResult().exists()) {
                        NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);


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


                        loading.dismiss();


                    }
                }
        );
    }

    private void setControl() {
        layoutBottom = findViewById(R.id.layoutBottom);
        imgAvatar = findViewById(R.id.imgAvatar);

    }
}