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
import com.example.numberfindinggame.activity.BanBe.BanBeActivity;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.constant.TrangThaiBanBe;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.BanBe;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.BanBeRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;

import java.util.List;


public class LoiMoiKetBanAdapter
        extends ArrayAdapter<BanBe> {

    private final Context context;
    private final List<BanBe> dsBanBe;

    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();
    private BanBeRepository banBeRepository = new BanBeRepository();
    private String maNguoiDung = null;

    public LoiMoiKetBanAdapter(
            Context context,
            List<BanBe> dsBanBe
    ) {
        super(
                context,
                R.layout.item_loi_moi_ket_ban,
                dsBanBe
        );

        this.context = context;
        this.dsBanBe = dsBanBe;
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
                            R.layout.item_loi_moi_ket_ban,
                            parent,
                            false
                    );
        }

        BanBe banBe = dsBanBe.get(position);


        ImageView imgAvatar = convertView.findViewById(R.id.imgAvatar);
        TextView txtTenNguoiDung = convertView.findViewById(R.id.txtTenNguoiDung);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThai);

        ImageView imgChanNhan = convertView.findViewById(R.id.imgChanNhan);
        ImageView imgTuChoi = convertView.findViewById(R.id.imgTuChoi);

        MaterialCardView cardBanBe = convertView.findViewById(R.id.cardBanBe);

        NguoiDung nguoiDung = banBe.getNguoiDung1();

        if (nguoiDung == null) {

            txtTenNguoiDung.setText("Người dùng không tồn tại");
            txtTrangThai.setText("⚫ Offline");
            txtTrangThai.setTextColor(
                    context.getResources().getColor(
                            R.color.black
                    )
            );

            imgAvatar.setImageResource(
                    R.drawable.avatar_default
            );

        } else {

            String tenNguoiDung =
                    nguoiDung.getTenNguoiDung();

            if (tenNguoiDung == null
                    || tenNguoiDung.trim().isEmpty()) {

                tenNguoiDung = "Người dùng";
            }

            txtTenNguoiDung.setText(
                    tenNguoiDung
            );

            long dangNhapCuoi =
                    nguoiDung.getDangNhapCuoi();

            long hienTai =
                    System.currentTimeMillis();

            long khoangCach =
                    hienTai - dangNhapCuoi;

            if (khoangCach <= 60 * 1000) {

                txtTrangThai.setText("🟢 Online");

                txtTrangThai.setTextColor(
                        context.getResources().getColor(
                                R.color.green
                        )
                );

            } else {

                txtTrangThai.setText("⚫ Offline");

                txtTrangThai.setTextColor(
                        context.getResources().getColor(
                                R.color.black
                        )
                );
            }

            try {

                String hinhDaiDien =
                        nguoiDung.getHinhDaiDien();

                if (hinhDaiDien != null
                        && !hinhDaiDien.isEmpty()) {

                    byte[] byteArrayHinh =
                            chuyenStringSangByte(
                                    hinhDaiDien
                            );

                    imgAvatar.setImageBitmap(
                            chuyenByteSangBitMap(
                                    byteArrayHinh
                            )
                    );

                } else {

                    imgAvatar.setImageResource(
                            R.drawable.avatar_default
                    );
                }

            } catch (Exception exception) {

                imgAvatar.setImageResource(
                        R.drawable.avatar_default
                );
            }
        }

        imgChanNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                dongYLoiMoi(banBe);
            }
        });

        imgTuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);
                tuChoiLoiMoi(banBe);
            }
        });

        if (banBe.getTrangThai().equals(TrangThaiBanBe.LOI_MOI_KET_BAN.getGiaTri())) {
            cardBanBe.setVisibility(View.VISIBLE);
        } else {
            cardBanBe.setVisibility(View.GONE);
        }

        cardBanBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);

                Intent intent = new Intent(context, ThongTinNguoiDungActivity.class);
                intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.LOI_MOI_KET_BAN);
                intent.putExtra(IntentKey.MA_NGUOI_DUNG, banBe.getMaNguoiDung1());

                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }


    private void dongYLoiMoi(BanBe banBe) {
        banBeRepository.dongYLoiMoi(
                banBe.getMaBanBe(),
                new BanBeRepository.OnUpdateBanBeListener() {

                    @Override
                    public void onSuccess() {

                        MessageHelper.success(
                                (Activity) context,
                                "Đã đồng ý lời mời kết bạn"
                        );
                        banBeRepository.donDepDuLieuBanBe(
                                banBe.getMaNguoiDung1(),
                                banBe.getMaNguoiDung2(),
                                task -> {

                                    if (task.isSuccessful()) {
                                        // Dọn dữ liệu thành công

                                    }
                                }
                        );
                        banBe.setTrangThai(TrangThaiBanBe.DONG_Y.getGiaTri());
                        dsBanBe.remove(banBe);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {

                        MessageHelper.error(
                                (Activity) context,
                                "Đồng ý lời mời thất bại"
                        );
                    }
                }
        );
    }

    private void tuChoiLoiMoi(BanBe banBe) {
        banBeRepository.tuChoiLoiMoi(
                banBe.getMaBanBe(),
                new BanBeRepository.OnUpdateBanBeListener() {

                    @Override
                    public void onSuccess() {

                        MessageHelper.error(
                                (Activity) context,
                                "Đã hủy lời mời kết bạn"
                        );
                        banBe.setTrangThai(TrangThaiBanBe.TU_CHOI.getGiaTri());
                        dsBanBe.remove(banBe);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {

                        MessageHelper.error(
                                (Activity) context,
                                "Hủy lời mời thất bại"
                        );
                    }
                }
        );
    }

}
