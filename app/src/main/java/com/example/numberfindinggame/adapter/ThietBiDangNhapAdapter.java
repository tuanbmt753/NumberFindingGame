package com.example.numberfindinggame.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ThietBiDangNhapAdapter
        extends ArrayAdapter<ThietBiDangNhap> {

    private final Context context;
    private final List<ThietBiDangNhap> dsThietBi;

    public ThietBiDangNhapAdapter(
            Context context,
            List<ThietBiDangNhap> dsThietBi
    ) {
        super(
                context,
                R.layout.item_thiet_bi_dang_nhap,
                dsThietBi
        );

        this.context = context;
        this.dsThietBi = dsThietBi;
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
                            R.layout.item_thiet_bi_dang_nhap,
                            parent,
                            false
                    );
        }

        TextView txtTenThietBi =
                convertView.findViewById(R.id.txtTenThietBi);

        TextView txtMaThietBi =
                convertView.findViewById(R.id.txtMaThietBi);

        TextView txtNgayDangNhap =
                convertView.findViewById(R.id.txtNgayDangNhap);

        TextView txtNoiDung = convertView.findViewById(R.id.txtNoiDung);

        MaterialCardView cardDangXuat =
                convertView.findViewById(R.id.cardDangXuat);

        MaterialCardView cardXoaThietBi =
                convertView.findViewById(R.id.cardXoaThietBi);

        ThietBiDangNhap thietBi =
                dsThietBi.get(position);

        txtTenThietBi.setText(
                "Tên thiết bị: "
                        + thietBi.getTenThietBi()
        );

        txtMaThietBi.setText(
                "Mã thiết bị: "
                        + thietBi.getMaThietBi()
        );

        txtNgayDangNhap.setText(
                "Ngày đăng nhập: "
                        + thietBi.getNgayTao()
        );

        if (Boolean.FALSE.equals(thietBi.getDangHoatDong())) {

            cardDangXuat.setCardBackgroundColor(
                    Color.parseColor("#FFFFFF")
            );
            txtNoiDung.setText("Đã đăng xuất");
            cardDangXuat.setEnabled(false);

        } else {

            cardDangXuat.setCardBackgroundColor(
                    Color.parseColor("#77BEC4")
            );
            txtNoiDung.setText("Đăng xuất");
            cardDangXuat.setEnabled(true);
        }

        if (thietBi.getMaThietBi().equals(DeviceHelper.getDeviceId(context))) {
            cardDangXuat.setCardBackgroundColor(
                    Color.parseColor("#FFFFFF")
            );
            txtNoiDung.setText("Thiết bị đang dùng");

            cardDangXuat.setEnabled(false);
            cardXoaThietBi.setVisibility(View.GONE);
        }

        cardDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ConfirmDialog(
                        context,
                        "Xác nhận",
                        "Bạn có muốn đăng xuất từ xa khỏi thiết bị này không?",
                        new ConfirmDialog.ConfirmCallback() {

                            @Override
                            public void onYes() {
                                ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();
                                thietBiDangNhapRepository.voHieuHoaThietBi(
                                        thietBi.getMaNguoiDung(),
                                        thietBi.getMaThietBi(),
                                        task -> {

                                            if (task.isSuccessful()) {
                                                MessageHelper.success((Activity) context, "Đã đăng xuất thiết bị");
                                            }


                                        }
                                );
                            }

                            @Override
                            public void onNo() {

                            }
                        }
                ).show();
            }
        });

        cardXoaThietBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ConfirmDialog(
                        context,
                        "Xác nhận",
                        "Bạn có muốn xóa thiết bị này không?",
                        new ConfirmDialog.ConfirmCallback() {

                            @Override
                            public void onYes() {
                                xoaThietBi(context, thietBi);
                            }

                            @Override
                            public void onNo() {

                            }
                        }
                ).show();


            }
        });

        return convertView;
    }

    private void xoaThietBi(Context context, ThietBiDangNhap thietBi) {
        ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();
        thietBiDangNhapRepository.xoaThietBi(
                thietBi.getMaNguoiDung(),
                thietBi.getMaThietBi(),
                task -> {

                    if (task.isSuccessful()) {

                        MessageHelper.success(
                                (Activity) context,
                                "Đã xóa thiết bị"
                        );

                    } else {

                        MessageHelper.error(
                                (Activity) context,
                                "Xóa thiết bị thất bại"
                        );
                    }
                }
        );
    }
}
