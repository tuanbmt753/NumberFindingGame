package com.example.numberfindinggame.adapter;


import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.BanBeRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.session.SessionManager;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ThemBanBeAdapter
        extends ArrayAdapter<NguoiDung> {

    private final Context context;
    private final List<NguoiDung> dsNguoiDung;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private BanBeRepository banBeRepository = new BanBeRepository();
    private String maNguoiDung = null;

    public ThemBanBeAdapter(
            Context context,
            List<NguoiDung> dsNguoiDung
    ) {
        super(
                context,
                R.layout.item_them_ban_be,
                dsNguoiDung
        );

        this.context = context;
        this.dsNguoiDung = dsNguoiDung;
        this.maNguoiDung = SessionManager.getUserId(context);

    }


    @NonNull
    @Override
    public View getView(
            int position,
            View convertView,
            @NonNull ViewGroup parent
    ) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(
                            R.layout.item_them_ban_be,
                            parent,
                            false
                    );
        }

        NguoiDung nguoiDung = dsNguoiDung.get(position);

        ImageView imgAvatar = convertView.findViewById(R.id.imgAvatar);
        TextView txtTenNguoiDung = convertView.findViewById(R.id.txtTenNguoiDung);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThai);
        ImageView imgThemBanBe = convertView.findViewById(R.id.imgThemBanBe);

        MaterialCardView cardBanBe = convertView.findViewById(R.id.cardBanBe);

        txtTenNguoiDung.setText(nguoiDung.getTenNguoiDung());

        long dangNhapCuoi = nguoiDung.getDangNhapCuoi();
        long hienTai = System.currentTimeMillis();
        long khoangCach = hienTai - dangNhapCuoi;
        if (khoangCach <= 60 * 1000) {

            // 🟢 Online
            txtTrangThai.setText("🟢 Online");
            txtTrangThai.setTextColor(context.getResources().getColor(R.color.green));

        } else {

            // ⚫ Offline
            txtTrangThai.setText("⚫ Offline");
            txtTrangThai.setTextColor(context.getResources().getColor(R.color.black));
        }

        try {
            if (!nguoiDung.getHinhDaiDien().equals("")) {
                byte[] byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhDaiDien());
                imgAvatar.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
            } else {
                imgAvatar.setImageResource(R.drawable.avatar_default);
            }
        } catch (Exception exception) {
            imgAvatar.setImageResource(R.drawable.avatar_default);
        }

        imgThemBanBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                guiLoiMoiKetBan(nguoiDung.getMaNguoiDung(), imgThemBanBe);
            }
        });

        cardBanBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);

                Intent intent = new Intent(context, ThongTinNguoiDungActivity.class);
                intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.THEM_BAN_BE);
                intent.putExtra(IntentKey.MA_NGUOI_DUNG, nguoiDung.getMaNguoiDung());

                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        kiemTraTrangThaiBanBe(nguoiDung.getMaNguoiDung(), imgThemBanBe);

        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    private void guiLoiMoiKetBan(String maNguoiDungNhan, ImageView imgThemBanBe) {
        banBeRepository.guiLoiMoiKetBan(
                maNguoiDung,
                maNguoiDungNhan,
                new BanBeRepository.OnGuiLoiMoiListener() {

                    @Override
                    public void onSuccess() {
                        MessageHelper.success((Activity) context, "Đã gửi lời mời kết bạn");
                        imgThemBanBe.setImageResource(R.drawable.ic_thanh_cong);
                        imgThemBanBe.setEnabled(false);
                    }

                    @Override
                    public void onFailure() {
                        MessageHelper.error((Activity) context, "Gửi lời mời kết bạn thất bại");
                    }
                }
        );
    }

    private void kiemTraTrangThaiBanBe(String maNguoiDungNhan, ImageView imgThemBanBe) {
        banBeRepository.kiemTraTrangThaiBanBe(
                maNguoiDung,
                maNguoiDungNhan,
                new BanBeRepository.OnCheckTrangThaiBanBeListener() {

                    @Override
                    public void onResult(boolean ketQua) {

                        if (ketQua) {
                            // DongY hoặc LoiMoiKetBan
                            Log.d("BAN_BE", "Đã gửi / đã có quan hệ");
                            imgThemBanBe.setImageResource(R.drawable.ic_thanh_cong);
                            imgThemBanBe.setEnabled(false);

                        } else {
                            // TuChoi hoặc chưa có
                            Log.d("BAN_BE", "Chưa có hoặc đã từ chối");
                            imgThemBanBe.setImageResource(R.drawable.ic_them_ban_be);
                            imgThemBanBe.setEnabled(true);
                        }
                    }
                }
        );
    }

}
