
package com.example.numberfindinggame.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.constant.LoaiTinNhan;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.repository.TinNhanRepository;
import com.example.numberfindinggame.session.SessionManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class BieuCamAdapter
        extends RecyclerView.Adapter<BieuCamAdapter.BieuCamViewHolder> {

    private final Context context;
    private final List<Integer> danhSachBieuCam;
    private TinNhanRepository tinNhanRepository;
    private String maNguoiDung;
    private String maPhongChat;

    private RecyclerView recyclerViewBieuCam;

    public String getMaPhongChat() {
        return maPhongChat;
    }

    public void setMaPhongChat(String maPhongChat) {
        this.maPhongChat = maPhongChat;
    }

    public BieuCamAdapter(
            Context context,
            List<Integer> danhSachBieuCam
    ) {
        this.context = context;
        this.danhSachBieuCam = danhSachBieuCam;
        tinNhanRepository = new TinNhanRepository();
        maNguoiDung = SessionManager.getUserId(context);
    }

    public RecyclerView getRecyclerViewBieuCam() {
        return recyclerViewBieuCam;
    }

    public void setRecyclerViewBieuCam(RecyclerView recyclerViewBieuCam) {
        this.recyclerViewBieuCam = recyclerViewBieuCam;
    }

    @NonNull
    @Override
    public BieuCamViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_bieu_cam,
                        parent,
                        false
                );

        return new BieuCamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BieuCamViewHolder holder,
            int position
    ) {
        int hinhAnh = danhSachBieuCam.get(position);

        holder.imgBieuCam.setImageResource(hinhAnh);

        // Xử lý khi nhấn vào biểu cảm
        holder.imgBieuCam.setOnClickListener(v -> {

            // Có thể xử lý gửi biểu cảm tại đây
            themTinNhan("" + hinhAnh, LoaiTinNhan.SYSTEM_IMAGE);
            recyclerViewBieuCam.setVisibility(View.GONE);

        });
    }

    @Override
    public int getItemCount() {
        return danhSachBieuCam.size();
    }

    private void themTinNhan(String noiDung, String loaiTinNhan) {
        tinNhanRepository.themTinNhan(maNguoiDung, maPhongChat, noiDung, loaiTinNhan, new TinNhanRepository.OnTinNhanListener() {

            @Override
            public void onSuccess(String maTinNhan) {

            }

            @Override
            public void onFailure(String loi) {
                // Xử lý lỗi
                MessageHelper.error((Activity) context, loi);
            }
        });
    }

    public static class BieuCamViewHolder
            extends RecyclerView.ViewHolder {

        ShapeableImageView imgBieuCam;

        public BieuCamViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            imgBieuCam = itemView.findViewById(
                    R.id.imgBieuCam
            );
        }
    }
}
