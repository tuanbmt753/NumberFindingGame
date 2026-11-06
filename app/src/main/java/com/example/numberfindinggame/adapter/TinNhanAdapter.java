
package com.example.numberfindinggame.adapter;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.constant.LoaiTinNhan;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.TinNhan;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.TinNhanRepository;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.utils.DateUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;
import java.util.List;

public class TinNhanAdapter extends RecyclerView.Adapter<TinNhanAdapter.ViewHolder> {

    private Context context;
    private List<TinNhan> tinNhanList;

    private String maNguoiDung;

    private NguoiDungRepository nguoiDungRepository;
    private TinNhanRepository tinNhanRepository;

    private RecyclerView recyclerViewBieuCam;


    public TinNhanAdapter(Context context, List<TinNhan> tinNhanList) {

        this.context = context;
        this.tinNhanList = tinNhanList;

        maNguoiDung = SessionManager.getUserId(context);

        nguoiDungRepository = new NguoiDungRepository();
        tinNhanRepository = new TinNhanRepository();

    }

    public RecyclerView getRecyclerViewBieuCam() {
        return recyclerViewBieuCam;
    }

    public void setRecyclerViewBieuCam(RecyclerView recyclerViewBieuCam) {
        this.recyclerViewBieuCam = recyclerViewBieuCam;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_tin_nhan, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        TinNhan tinNhan = tinNhanList.get(position);

        // Kiểm tra người gửi
        boolean laTinNhanCuaMinh =
                maNguoiDung != null &&
                        maNguoiDung.equals(tinNhan.getMaNguoiDung());

        // Reset trạng thái ViewHolder
        holder.imgAvatar.setVisibility(View.GONE);

        holder.linearLayoutTinNhan.setVisibility(View.GONE);
        holder.linearLayoutImgTinNhan.setVisibility(View.GONE);

// Căn nội dung trái/phải
        holder.linearLayoutNoiDungTinNhan.setGravity(
                laTinNhanCuaMinh ? Gravity.END : Gravity.START
        );

// Xác định có hiển thị avatar hay không
        boolean hienAvatar = false;

// Tin nhắn của người khác
        if (!laTinNhanCuaMinh) {

            // Tin nhắn đầu tiên
            if (position == 0) {

                hienAvatar = true;

            } else {

                TinNhan tinNhanTruoc = tinNhanList.get(position - 1);

                boolean cungNguoiGui =
                        tinNhan.getMaNguoiDung() != null
                                && tinNhan.getMaNguoiDung().equals(
                                tinNhanTruoc.getMaNguoiDung()
                        );

                boolean cungNgay = laCungNgay(
                        tinNhan.getNgayTao(),
                        tinNhanTruoc.getNgayTao()
                );

                // Cùng người gửi, cùng ngày thì ẩn avatar
                // Khác người gửi hoặc khác ngày thì hiện avatar
                hienAvatar = !cungNguoiGui || !cungNgay;
            }
        }

        // Cập nhật trạng thái avatar
        holder.imgAvatar.setVisibility(
                hienAvatar ? View.VISIBLE : View.GONE
        );

        // Xử lý background và avatar
        if (laTinNhanCuaMinh) {

            holder.linearLayoutTinNhan.setBackgroundResource(
                    R.drawable.bg_tin_nhan2
            );

        } else {

            holder.linearLayoutTinNhan.setBackgroundResource(
                    R.drawable.bg_tin_nhan
            );

            holder.imgAvatar.setImageResource(
                    R.drawable.avatar_default
            );

            holder.imgAvatar.setVisibility(View.VISIBLE);
            holder.imgAvatar.setImageResource(0);

            // Chỉ tải avatar khi cần hiển thị
            if (hienAvatar) {

                layThongTinNguoiDung(
                        tinNhan.getMaNguoiDung(),
                        holder.imgAvatar
                );

            }

        }

        // Xử lý loại tin nhắn
        boolean hienGio = hienThiGio(position);

        if (LoaiTinNhan.TEXT.equals(tinNhan.getLoaiTinNhan())) {

            holder.linearLayoutTinNhan.setVisibility(View.VISIBLE);

            holder.txtTinNhan.setText(tinNhan.getNoiDung());

            holder.txtThoiGian.setVisibility(
                    hienGio ? View.VISIBLE : View.GONE
            );

            holder.txtThoiGian.setText(
                    DateUtils.formatTime(tinNhan.getNgayTao())
            );

        } else if (LoaiTinNhan.IMAGE.equals(tinNhan.getLoaiTinNhan())) {

            holder.linearLayoutImgTinNhan.setVisibility(View.VISIBLE);

            holder.txtThoiGian2.setVisibility(
                    hienGio ? View.VISIBLE : View.GONE
            );

            holder.txtThoiGian2.setText(
                    DateUtils.formatTime(tinNhan.getNgayTao())
            );

            xemHinhAnh(
                    holder.imgTinNhan,
                    tinNhan.getNoiDung()
            );
        } else if (LoaiTinNhan.SYSTEM_IMAGE.equals(
                tinNhan.getLoaiTinNhan()
        )) {

            // Hiển thị khu vực hình ảnh
            holder.linearLayoutImgTinNhan.setVisibility(View.VISIBLE);

            // Không hiển thị khung tin nhắn văn bản
            holder.linearLayoutTinNhan.setVisibility(View.GONE);

            // Hiển thị thời gian
            holder.txtThoiGian2.setVisibility(
                    hienGio ? View.VISIBLE : View.GONE
            );

            holder.txtThoiGian2.setText(
                    DateUtils.formatTime(tinNhan.getNgayTao())
            );

            // Hiển thị biểu cảm Stitch
            xemBieuCam(
                    holder.imgTinNhan,
                    tinNhan.getNoiDung()
            );
        }


        // Ẩn thời gian mặc định
        holder.txtThoiGianTinNhan.setVisibility(View.GONE);

        // Tin nhắn đầu tiên luôn hiển thị thời gian
        if (position == 0) {

            holder.txtThoiGianTinNhan.setVisibility(View.VISIBLE);

            holder.txtThoiGianTinNhan.setText(
                    DateUtils.format(tinNhan.getNgayTao())
            );

        } else {

            TinNhan tinNhanTruoc = tinNhanList.get(position - 1);

            Calendar ngayHienTai = Calendar.getInstance();
            ngayHienTai.setTimeInMillis(tinNhan.getNgayTao());

            Calendar ngayTruoc = Calendar.getInstance();
            ngayTruoc.setTimeInMillis(tinNhanTruoc.getNgayTao());

            boolean cungNgay =
                    ngayHienTai.get(Calendar.YEAR)
                            == ngayTruoc.get(Calendar.YEAR)
                            &&
                            ngayHienTai.get(Calendar.DAY_OF_YEAR)
                                    == ngayTruoc.get(Calendar.DAY_OF_YEAR);

            // Nếu khác ngày thì hiện thời gian
            if (!cungNgay) {

                holder.txtThoiGianTinNhan.setVisibility(View.VISIBLE);

                holder.txtThoiGianTinNhan.setText(
                        DateUtils.format(tinNhan.getNgayTao())
                );

            }
        }


        // Chức năng thu hồi tin nhắn khi nhấn giữ
        holder.linearLayoutNoiDungTinNhan
                .setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        SoundManager.playButton(context);
                        // Chỉ thu hồi tin nhắn của mình
                        if (tinNhan.getMaNguoiDung() != null
                                && tinNhan.getMaNguoiDung().equals(maNguoiDung)) {
                            new ConfirmDialog(
                                    context,
                                    "Xác nhận",
                                    "⚠️ Bạn có muốn thu hồi tin nhắn này?",
                                    new ConfirmDialog.ConfirmCallback() {

                                        @Override
                                        public void onYes() {

                                            SoundManager.playButton(context);

                                            thuHoiTinNhan(tinNhan);
                                        }

                                        @Override
                                        public void onNo() {

                                            SoundManager.playButton(context);
                                        }
                                    }
                            ).show();
                        }

                        // Đã xử lý sự kiện nhấn giữ
                        return true;
                    }
                });

        // Hiển thị tin nhắn đã thu hồi
        String noiDung = tinNhan.getNoiDung();

        if (noiDung == null) {

            holder.txtTinNhan.setText(
                    "Tin nhắn đã bị thu hồi"
            );

            holder.txtTinNhan.setTextColor(
                    Color.GRAY
            );

            holder.txtTinNhan.setTypeface(
                    ResourcesCompat.getFont(context, R.font.sono),
                    Typeface.ITALIC
            );

        } else {

            holder.txtTinNhan.setText(noiDung);

            holder.txtTinNhan.setTextColor(
                    Color.BLACK
            );

            holder.txtTinNhan.setTypeface(
                    ResourcesCompat.getFont(context, R.font.sono),
                    Typeface.NORMAL
            );
        }

        holder.linearLayoutTinNhanTong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewBieuCam.setVisibility(View.GONE);
            }
        });

    }

    //Hàm xem biểu cảm
    private void xemBieuCam(
            ImageView imgHinh,
            String noiDung
    ) {

        try {

            if (noiDung == null || noiDung.trim().isEmpty()) {

                imgHinh.setImageResource(R.drawable.no_img);

                return;
            }

            // Chuyển String thành resource ID
            int resourceId = Integer.parseInt(noiDung);

            // Kiểm tra resource có tồn tại không
            if (resourceId != 0) {

                imgHinh.setImageResource(resourceId);

            } else {

                imgHinh.setImageResource(R.drawable.no_img);
            }

        } catch (Exception e) {

            imgHinh.setImageResource(R.drawable.no_img);
        }
    }


    //Hàm thu hồi tin nhắn
    private void thuHoiTinNhan(TinNhan tinNhan) {
        tinNhanRepository.suaTinNhan(
                tinNhan.getMaTinNhan(),
                null,
                new TinNhanRepository.OnTinNhanListener() {

                    @Override
                    public void onSuccess(String maTinNhan) {

                        MessageHelper.success(
                                (Activity) context,
                                "Đã thu hồi tin nhắn"
                        );

                    }

                    @Override
                    public void onFailure(String loi) {

                        MessageHelper.error(
                                (Activity) context,
                                loi
                        );

                    }
                }
        );
    }


    //Ham hiển thị giờ
    private boolean hienThiGio(int position) {

        TinNhan tinNhan = tinNhanList.get(position);

        // Tin nhắn cuối cùng luôn hiển thị giờ
        if (position == tinNhanList.size() - 1) {
            return true;
        }

        TinNhan tinNhanTiepTheo = tinNhanList.get(position + 1);

        boolean cungNguoiGui =
                tinNhan.getMaNguoiDung() != null
                        && tinNhan.getMaNguoiDung().equals(
                        tinNhanTiepTheo.getMaNguoiDung()
                );

        boolean cungGio = laCungGio(
                tinNhan.getNgayTao(),
                tinNhanTiepTheo.getNgayTao()
        );

        // Cùng người gửi, cùng giờ thì ẩn giờ ở tin nhắn hiện tại
        return !cungNguoiGui || !cungGio;
    }

    //Hàm kiểm tra cùng giờ
    private boolean laCungGio(long timestamp1, long timestamp2) {

        Calendar ngay1 = Calendar.getInstance();
        ngay1.setTimeInMillis(timestamp1);

        Calendar ngay2 = Calendar.getInstance();
        ngay2.setTimeInMillis(timestamp2);

        return ngay1.get(Calendar.YEAR) == ngay2.get(Calendar.YEAR)
                && ngay1.get(Calendar.DAY_OF_YEAR) == ngay2.get(Calendar.DAY_OF_YEAR)
                && ngay1.get(Calendar.HOUR_OF_DAY) == ngay2.get(Calendar.HOUR_OF_DAY)
                && ngay1.get(Calendar.MINUTE) == ngay2.get(Calendar.MINUTE);
    }

    //Hàm kiểm tra cùng ngày
    private boolean laCungNgay(
            long timestamp1,
            long timestamp2
    ) {

        Calendar ngay1 = Calendar.getInstance();
        ngay1.setTimeInMillis(timestamp1);

        Calendar ngay2 = Calendar.getInstance();
        ngay2.setTimeInMillis(timestamp2);

        return ngay1.get(Calendar.YEAR)
                == ngay2.get(Calendar.YEAR)

                && ngay1.get(Calendar.DAY_OF_YEAR)
                == ngay2.get(Calendar.DAY_OF_YEAR);
    }

    // Hiển thị hình ảnh
    private void xemHinhAnh(ImageView imgHinh, String hinh) {

        try {

            if (hinh != null && !hinh.trim().isEmpty()) {

                byte[] byteArrayHinh = chuyenStringSangByte(hinh);

                imgHinh.setImageBitmap(
                        chuyenByteSangBitMap(byteArrayHinh)
                );

            } else {

                imgHinh.setImageResource(
                        R.drawable.no_img
                );
            }

        } catch (Exception exception) {

            imgHinh.setImageResource(
                    R.drawable.no_img
            );
        }
    }

    // Lấy thông tin người dùng
    private void layThongTinNguoiDung(
            String maNguoiDung,
            ImageView imgHinh
    ) {

        if (maNguoiDung == null || maNguoiDung.trim().isEmpty()) {
            return;
        }

        if (!NetworkHelper.isConnected(context)) {

            MessageHelper.error(
                    (Activity) context,
                    "Không có kết nối Internet"
            );

            return;
        }

        // Đánh dấu ImageView đang tải avatar của ai
        imgHinh.setTag(maNguoiDung);

        nguoiDungRepository.layNguoiDungTheoMa(
                maNguoiDung,
                task -> {

                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().exists()) {

                        // Kiểm tra ViewHolder có bị tái sử dụng không
                        if (!maNguoiDung.equals(imgHinh.getTag())) {
                            return;
                        }

                        NguoiDung nguoiDung =
                                task.getResult().getValue(NguoiDung.class);

                        if (nguoiDung != null) {

                            xemHinhAnh(
                                    imgHinh,
                                    nguoiDung.getHinhDaiDien()
                            );
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return tinNhanList != null ? tinNhanList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTinNhan, txtThoiGian, txtThoiGian2, txtThoiGianTinNhan;

        ShapeableImageView imgAvatar, imgTinNhan;

        LinearLayout linearLayoutTinNhan;
        LinearLayout linearLayoutImgTinNhan;
        LinearLayout linearLayoutNoiDungTinNhan;
        LinearLayout linearLayoutTinNhanTong;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtTinNhan = itemView.findViewById(R.id.txtTinNhan);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtThoiGian2 = itemView.findViewById(R.id.txtThoiGian2);
            txtThoiGianTinNhan = itemView.findViewById(R.id.txtThoiGianTinNhan);

            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgTinNhan = itemView.findViewById(R.id.imgTinNhan);

            linearLayoutTinNhan = itemView.findViewById(
                    R.id.linearLayoutTinNhan
            );

            linearLayoutImgTinNhan = itemView.findViewById(
                    R.id.linearLayoutImgTinNhan
            );

            linearLayoutNoiDungTinNhan = itemView.findViewById(
                    R.id.linearLayoutNoiDungTinNhan
            );
            linearLayoutTinNhanTong = itemView.findViewById(
                    R.id.linearLayoutTinNhanTong
            );
        }
    }
}
