package com.example.numberfindinggame.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.session.SessionManagerSetting;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.example.numberfindinggame.utils.DateUtils;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ThietBiDangNhapAdapter
        extends ArrayAdapter<ThietBiDangNhap> {

    private final Context context;
    private final List<ThietBiDangNhap> dsThietBi;

    private NguoiDungRepository repository = new NguoiDungRepository();
    private ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();
    private String emailNguoiDung;

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
                ""
                        + thietBi.getTenThietBi()
        );

        txtMaThietBi.setText(
                "Mã thiết bị: "
                        + thietBi.getMaThietBi()
        );

        txtNgayDangNhap.setText(
                ""
                        + DateUtils.format(thietBi.getNgayCapNhatCuoi())
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


        repository.layEmailTheoMaNguoiDung(
                thietBi.getMaNguoiDung(),
                new NguoiDungRepository.OnGetEmailListener() {

                    @Override
                    public void onSuccess(String email) {

                        if (email != null) {
                            emailNguoiDung = email;
                        }
                    }

                    @Override
                    public void onFailed(String message) {

                    }
                }
        );

        cardDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity activity = (Activity) context;
                SoundManager.playButton(context);

                if (!NetworkHelper.isConnected(context)) {

                    MessageHelper.error(
                            (Activity) context,
                            "Không có kết nối Internet"
                    );

                    return;
                }

                String setting = SessionManagerSetting.getSetting(context);
                if (setting != null && !setting.isEmpty()) {

                    new ConfirmDialog(
                            context,
                            "Xác nhận",
                            "Bạn có muốn đăng xuất từ xa khỏi thiết bị này không?",
                            new ConfirmDialog.ConfirmCallback() {

                                @Override
                                public void onYes() {
                                    SoundManager.playButton(context);
                                    LoadingDialog loading =
                                            new LoadingDialog(context);
                                    loading.setMessage("Đang vô hiệu hóa thiết bị...");
                                    loading.show();

                                    voHieuHoaThietBi(context, thietBi);

                                    loading.dismiss();

                                }

                                @Override
                                public void onNo() {
                                    SoundManager.playButton(context);
                                }
                            }
                    ).show();

                } else {
                    new ConfirmDialog(
                            context,
                            "Xác nhận",
                            "Bạn phải xác thực email " + emailNguoiDung + " để thực hiện đăng xuất từ xa khỏi thiết bị này?",
                            new ConfirmDialog.ConfirmCallback() {

                                @Override
                                public void onYes() {
                                    SoundManager.playButton(context);
                                    Intent intent = new Intent(context, XacThucEmailActivity.class);
                                    intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_XUAT_TU_XA);
                                    intent.putExtra(IntentKey.EMAIL, emailNguoiDung);

                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }

                                @Override
                                public void onNo() {
                                    SoundManager.playButton(context);
                                }
                            }
                    ).show();


                }


            }
        });

        cardXoaThietBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity activity = (Activity) context;
                SoundManager.playButton(context);
                if (!NetworkHelper.isConnected(context)) {

                    MessageHelper.error(
                            (Activity) context,
                            "Không có kết nối Internet"
                    );

                    return;
                }


                String setting = SessionManagerSetting.getSetting(context);
                if (setting != null && !setting.isEmpty()) {

                    new ConfirmDialog(
                            context,
                            "Xác nhận",
                            "Bạn có muốn đăng xuất từ xa khỏi thiết bị này không?",
                            new ConfirmDialog.ConfirmCallback() {

                                @Override
                                public void onYes() {
                                    SoundManager.playButton(context);
                                    LoadingDialog loading =
                                            new LoadingDialog(context);
                                    loading.setMessage("Đang xóa thiết bị...");
                                    loading.show();
                                    xoaThietBi(context, thietBi);
                                    loading.dismiss();
                                }

                                @Override
                                public void onNo() {
                                    SoundManager.playButton(context);
                                }
                            }
                    ).show();


                } else {
                    new ConfirmDialog(
                            context,
                            "Xác nhận",
                            "Bạn phải xác thực email " + emailNguoiDung + " để xóa thiết bị này?",
                            new ConfirmDialog.ConfirmCallback() {

                                @Override
                                public void onYes() {
                                    SoundManager.playButton(context);
                                    Intent intent = new Intent(context, XacThucEmailActivity.class);
                                    intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.DANG_XUAT_TU_XA);
                                    intent.putExtra(IntentKey.EMAIL, emailNguoiDung);

                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                }

                                @Override
                                public void onNo() {
                                    SoundManager.playButton(context);
                                }
                            }
                    ).show();

                }


            }
        });

        return convertView;
    }

    private void voHieuHoaThietBi(Context context, ThietBiDangNhap thietBi) {

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

    private void xoaThietBi(Context context, ThietBiDangNhap thietBi) {

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
