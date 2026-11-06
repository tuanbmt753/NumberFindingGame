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
import com.example.numberfindinggame.activity.BanBe.TinNhanActivity;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.constant.LoaiPhongChat;
import com.example.numberfindinggame.constant.TrangThaiBanBe;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.BanBe;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.BanBeRepository;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.ThanhVienPhongChatRepository;
import com.example.numberfindinggame.repository.TinNhanRepository;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class BanBeAdapter
        extends ArrayAdapter<BanBe> {

    private final Context context;
    private final List<BanBe> dsBanBe;
    private BanBeRepository banBeRepository = new BanBeRepository();
    private String maNguoiDung = null;

    private ThanhVienPhongChatRepository thanhVienPhongChatRepository;

    private TinNhanRepository tinNhanRepository;

    public BanBeAdapter(
            Context context,
            List<BanBe> dsBanBe
    ) {
        super(
                context,
                R.layout.item_ban_be,
                dsBanBe
        );

        this.context = context;
        this.dsBanBe = dsBanBe;
        this.maNguoiDung = SessionManager.getUserId(context);
        thanhVienPhongChatRepository = new ThanhVienPhongChatRepository();
        tinNhanRepository = new TinNhanRepository();

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
                            R.layout.item_ban_be,
                            parent,
                            false
                    );
        }

        BanBe banBe = dsBanBe.get(position);

        ImageView imgAvatar = convertView.findViewById(R.id.imgAvatar);
        TextView txtTenNguoiDung = convertView.findViewById(R.id.txtTenNguoiDung);
        TextView txtTrangThai = convertView.findViewById(R.id.txtTrangThai);

        MaterialCardView cardXoaBanBe = convertView.findViewById(R.id.cardXoaBanBe);
        MaterialCardView cardBanBe = convertView.findViewById(R.id.cardBanBe);
        MaterialCardView cardTinNhan = convertView.findViewById(R.id.cardTinNhan);


        NguoiDung nguoiDung = banBe.getNguoiDung1();

        if (nguoiDung == null) {

            txtTenNguoiDung.setText(
                    "Người dùng không tồn tại"
            );

            txtTrangThai.setText(
                    "⚫ Offline"
            );

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

                txtTrangThai.setText(
                        "🟢 Online"
                );

                txtTrangThai.setTextColor(
                        context.getResources().getColor(
                                R.color.green
                        )
                );

            } else {

                txtTrangThai.setText(
                        "⚫ Offline"
                );

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
        NguoiDung finalNguoiDung = nguoiDung;

        cardXoaBanBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);

                new ConfirmDialog(
                        context,
                        "Xác nhận",
                        "⚠️ Bạn có muốn xóa người bạn này? ",
                        new ConfirmDialog.ConfirmCallback() {
                            @Override
                            public void onYes() {
                                SoundManager.playButton(context);
                                xoaLoiMoi(banBe);

                                List<String> maNguoiDungList = new ArrayList<>();
                                maNguoiDungList.add(maNguoiDung); //Ma nguoi dung
                                maNguoiDungList.add(finalNguoiDung.getMaNguoiDung()); //Ma ban be

                                timHoacTaoPhongChat(maNguoiDungList, true);

                            }

                            @Override
                            public void onNo() {
                                SoundManager.playButton(context);
                            }
                        }
                ).show();
            }
        });

        cardBanBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);

                Intent intent = new Intent(context, ThongTinNguoiDungActivity.class);
                intent.putExtra(IntentKey.ACTIVITY_TYPE, ActivityType.BAN_BE);
                intent.putExtra(IntentKey.MA_NGUOI_DUNG, banBe.getMaNguoiDung1());

                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });


        cardTinNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(context);

                List<String> maNguoiDungList = new ArrayList<>();
                maNguoiDungList.add(maNguoiDung); //Ma nguoi dung
                maNguoiDungList.add(finalNguoiDung.getMaNguoiDung()); //Ma ban be

                timHoacTaoPhongChat(maNguoiDungList);

            }
        });

        return convertView;
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    private void timHoacTaoPhongChat(List<String> maNguoiDungList) {
        thanhVienPhongChatRepository.timHoacTaoPhongChat(
                maNguoiDungList,
                LoaiPhongChat.FRIEND,
                new ThanhVienPhongChatRepository
                        .OnTimHoacTaoPhongChatListener() {

                    @Override
                    public void onSuccess(String maPhongChat) {
                        // Đã tìm thấy phòng cũ hoặc tạo phòng mới thành công.
                        // Dùng maPhongChat để tải tin nhắn hoặc gửi tin nhắn.

                        System.out.println(
                                "Mã phòng chat: " + maPhongChat
                        );

                        Intent intent = new Intent(context, TinNhanActivity.class);

                        intent.putExtra(IntentKey.MA_PHONG_CHAT, maPhongChat);
                        intent.putExtra(IntentKey.MA_BAN_BE, maNguoiDungList.get(1).toString().trim());


                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                    @Override
                    public void onFailure(String loi) {
                        System.out.println(
                                "Lỗi phòng chat: " + loi
                        );
                    }
                }
        );
    }

    private void timHoacTaoPhongChat(List<String> maNguoiDungList, Boolean xoaTinNhan) {
        thanhVienPhongChatRepository.timHoacTaoPhongChat(
                maNguoiDungList,
                LoaiPhongChat.FRIEND,
                new ThanhVienPhongChatRepository
                        .OnTimHoacTaoPhongChatListener() {

                    @Override
                    public void onSuccess(String maPhongChat) {
                        // Đã tìm thấy phòng cũ hoặc tạo phòng mới thành công.
                        // Dùng maPhongChat để tải tin nhắn hoặc gửi tin nhắn.

                        System.out.println(
                                "Mã phòng chat xóa bạn bè: " + maPhongChat
                        );

                        if (xoaTinNhan == true) {

                            xoaTinNhanTheoMaPhongChat(maPhongChat, maNguoiDungList);
                        }
                    }

                    @Override
                    public void onFailure(String loi) {
                        System.out.println(
                                "Lỗi phòng chat: " + loi
                        );
                    }
                }
        );
    }

    private void xoaLoiMoi(BanBe banBe) {
        banBeRepository.xoaLoiMoi(
                banBe.getMaBanBe(),
                new BanBeRepository.OnUpdateBanBeListener() {

                    @Override
                    public void onSuccess() {

                        MessageHelper.success(
                                (Activity) context,
                                "Đã xóa bạn bè thành công"
                        );


                        dsBanBe.remove(banBe);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {

                        MessageHelper.error(
                                (Activity) context,
                                "Xóa lời bạn bè thất bại"
                        );
                    }
                }
        );
    }

    private void xoaTinNhanTheoMaPhongChat(String maPhongChat, List<String> maNguoiDungList) {
        tinNhanRepository.xoaTinNhanTheoMaPhongChat(
                maPhongChat,
                new TinNhanRepository.OnTinNhanListener() {

                    @Override
                    public void onSuccess(String maTinNhan) {

                        // Tiếp tục xóa thành viên phòng chat
                        // Sau đó xóa phòng chat

                        System.out.println(
                                " Xóa tin nhắn Mã phòng chat xóa bạn bè: " + maPhongChat
                        );

                        thanhVienPhongChatRepository.xoaPhongChat(maNguoiDungList, LoaiPhongChat.FRIEND, new ThanhVienPhongChatRepository.OnXoaPhongChatListener() {
                            @Override
                            public void onSuccess() {
                                Log.e("XOA_PHONG_CHAT_THANH_VIEN_PHONG_CHAT", "THANH_CONG" + maPhongChat);
                            }

                            @Override
                            public void onFailure(String loi) {
                                Log.e("XOA_PHONG_CHAT_THANH_VIEN_PHONG_CHAT", loi);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String loi) {

                        Log.e("XOA_PHONG_CHAT", loi);

                    }
                }
        );
    }


}
