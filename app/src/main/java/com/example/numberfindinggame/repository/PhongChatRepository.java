
package com.example.numberfindinggame.repository;

import com.example.numberfindinggame.constant.LoaiPhongChat;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.PhongGhat;
import com.google.firebase.database.DatabaseReference;

public class PhongChatRepository {

    // Callback thêm phòng chat
    public interface OnThemPhongChatListener {
        void onSuccess(String maPhongChat);

        void onFailure(String loi);
    }

    /**
     * Thêm phòng chat mới vào Firebase.
     *
     * @param loaiPhongChat Loại phòng chat
     * @param listener      Callback kết quả
     */
    public void themPhongChat(
            String loaiPhongChat,
            OnThemPhongChatListener listener
    ) {

        // Kiểm tra loại phòng chat
        if (loaiPhongChat == null
                || loaiPhongChat.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Loại phòng chat không được để trống"
                );
            }
            return;
        }

        // Chỉ chấp nhận các loại phòng đã định nghĩa
        if (!LoaiPhongChat.GROUP.equals(loaiPhongChat)
                && !LoaiPhongChat.FRIEND.equals(loaiPhongChat)
                && !LoaiPhongChat.TEAM.equals(loaiPhongChat)
                && !LoaiPhongChat.GENERAL_CHAT.equals(loaiPhongChat)) {

            if (listener != null) {
                listener.onFailure(
                        "Loại phòng chat không hợp lệ"
                );
            }
            return;
        }

        // Tạo mã phòng chat tự động
        DatabaseReference ref =
                FirebaseManager.phongChat().push();

        String maPhongChat = ref.getKey();

        if (maPhongChat == null) {
            if (listener != null) {
                listener.onFailure(
                        "Không thể tạo mã phòng chat"
                );
            }
            return;
        }

        // Tạo dữ liệu phòng chat
        long ngayTao = System.currentTimeMillis();

        PhongGhat phongChat = new PhongGhat(
                maPhongChat,
                loaiPhongChat,
                ngayTao
        );

        // Lưu lên Firebase
        ref.setValue(phongChat)
                .addOnSuccessListener(unused -> {
                    if (listener != null) {
                        listener.onSuccess(maPhongChat);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(
                                e.getMessage() != null
                                        ? e.getMessage()
                                        : "Thêm phòng chat thất bại"
                        );
                    }
                });
    }


    public void xoaPhongChat(
            String maPhongChat,
            OnXoaPhongChatListener listener
    ) {

        // Kiểm tra mã phòng chat
        if (maPhongChat == null
                || maPhongChat.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Mã phòng chat không hợp lệ"
                );
            }

            return;
        }

        // Xóa phòng chat trên Firebase
        FirebaseManager.phongChat()
                .child(maPhongChat)
                .removeValue()
                .addOnSuccessListener(unused -> {

                    if (listener != null) {
                        listener.onSuccess();
                    }

                })
                .addOnFailureListener(e -> {

                    if (listener != null) {
                        listener.onFailure(
                                e.getMessage() != null
                                        ? e.getMessage()
                                        : "Xóa phòng chat thất bại"
                        );
                    }

                });
    }

    public interface OnXoaPhongChatListener {
        void onSuccess();

        void onFailure(String loi);
    }


}
