
package com.example.numberfindinggame.repository;

import com.example.numberfindinggame.constant.LoaiTinNhan;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.TinNhan;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TinNhanRepository {

    // ==================== CALLBACK ====================

    public interface OnTinNhanListener {
        void onSuccess(String maTinNhan);

        void onFailure(String loi);
    }

    // ==================== THÊM TIN NHẮN ====================

    public void themTinNhan(
            String maNguoiDung,
            String maPhongChat,
            String noiDung,
            String loaiTinNhan,
            OnTinNhanListener listener
    ) {

        // Kiểm tra dữ liệu đầu vào
        if (maNguoiDung == null
                || maNguoiDung.trim().isEmpty()
                || maPhongChat == null
                || maPhongChat.trim().isEmpty()
                || noiDung == null
                || noiDung.trim().isEmpty()
                || loaiTinNhan == null
                || loaiTinNhan.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Thông tin tin nhắn không hợp lệ"
                );
            }

            return;
        }

        // Kiểm tra loại tin nhắn
        if (!LoaiTinNhan.TEXT.equals(loaiTinNhan)
                && !LoaiTinNhan.IMAGE.equals(loaiTinNhan)
                && !LoaiTinNhan.VIDEO.equals(loaiTinNhan)
                && !LoaiTinNhan.SYSTEM_IMAGE.equals(loaiTinNhan)
        ) {

            if (listener != null) {
                listener.onFailure(
                        "Loại tin nhắn không hợp lệ"
                );
            }

            return;
        }

        // Lấy đường dẫn Firebase
        DatabaseReference ref =
                FirebaseManager.tinNhan();

        // Tạo mã tin nhắn tự động
        String maTinNhan = ref.push().getKey();

        if (maTinNhan == null) {
            if (listener != null) {
                listener.onFailure(
                        "Không thể tạo mã tin nhắn"
                );
            }

            return;
        }

        // Tạo đối tượng tin nhắn
        TinNhan tinNhan = new TinNhan(
                maTinNhan,
                maNguoiDung,
                maPhongChat,
                noiDung,
                loaiTinNhan,
                System.currentTimeMillis()
        );

        // Lưu dữ liệu lên Firebase
        ref.child(maTinNhan)
                .setValue(tinNhan)
                .addOnSuccessListener(unused -> {
                    if (listener != null) {
                        listener.onSuccess(maTinNhan);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(
                                e.getMessage() != null
                                        ? e.getMessage()
                                        : "Thêm tin nhắn thất bại"
                        );
                    }
                });
    }

    // ==================== XÓA TIN NHẮN ====================

    public void xoaTinNhan(
            String maTinNhan,
            OnTinNhanListener listener
    ) {

        // Kiểm tra mã tin nhắn
        if (maTinNhan == null
                || maTinNhan.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Mã tin nhắn không hợp lệ"
                );
            }

            return;
        }

        // Xóa tin nhắn theo mã
        DatabaseReference ref =
                FirebaseManager.tinNhan()
                        .child(maTinNhan);

        ref.removeValue()
                .addOnSuccessListener(unused -> {
                    if (listener != null) {
                        listener.onSuccess(maTinNhan);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(
                                e.getMessage() != null
                                        ? e.getMessage()
                                        : "Xóa tin nhắn thất bại"
                        );
                    }
                });
    }


    public void xoaTinNhan(
            List<TinNhan> tinNhanList,
            OnTinNhanListener listener
    ) {

        // Kiểm tra danh sách
        if (tinNhanList == null || tinNhanList.isEmpty()) {
            return;
        }

        // Nếu không vượt quá 50 thì không cần xóa
        if (tinNhanList.size() <= 50) {
            return;
        }

        // Sắp xếp tin nhắn theo thời gian cũ nhất đến mới nhất
        Collections.sort(
                tinNhanList,
                (a, b) -> Long.compare(
                        a.getNgayTao(),
                        b.getNgayTao()
                )
        );

        // Số lượng tin nhắn cần xóa
        int soTinNhanCanXoa = tinNhanList.size() - 50;

        // Xóa những tin nhắn cũ nhất
        for (int i = 0; i < soTinNhanCanXoa; i++) {

            TinNhan tinNhan = tinNhanList.get(i);

            if (tinNhan != null && tinNhan.getMaTinNhan() != null) {

                xoaTinNhan(
                        tinNhan.getMaTinNhan(),
                        null
                );
            }
        }
    }

    public void xoaTinNhanTheoMaPhongChat(
            String maPhongChat,
            OnTinNhanListener listener
    ) {

        // Kiểm tra mã phòng chat
        if (maPhongChat == null
                || maPhongChat.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure("Mã phòng chat không hợp lệ");
            }

            return;
        }

        FirebaseManager.tinNhan()
                .get()
                .addOnSuccessListener(snapshot -> {

                    // Kiểm tra dữ liệu
                    if (!snapshot.exists()) {

                        if (listener != null) {
                            listener.onSuccess(null);
                        }

                        return;
                    }

                    // Tạo danh sách các mã tin nhắn cần xóa
                    Map<String, Object> updates = new HashMap<>();

                    for (DataSnapshot data : snapshot.getChildren()) {

                        TinNhan tinNhan = data.getValue(TinNhan.class);

                        if (tinNhan == null) {
                            continue;
                        }

                        if (maPhongChat.equals(tinNhan.getMaPhongChat())) {

                            updates.put(data.getKey(), null);
                        }
                    }

                    // Không có tin nhắn cần xóa
                    if (updates.isEmpty()) {

                        if (listener != null) {
                            listener.onSuccess(null);
                        }

                        return;
                    }

                    // Xóa các tin nhắn phù hợp
                    FirebaseManager.tinNhan()
                            .updateChildren(updates)
                            .addOnSuccessListener(unused -> {

                                if (listener != null) {
                                    listener.onSuccess(null);
                                }

                            })
                            .addOnFailureListener(e -> {

                                if (listener != null) {
                                    listener.onFailure(e.getMessage());
                                }

                            });

                })
                .addOnFailureListener(e -> {

                    if (listener != null) {
                        listener.onFailure(e.getMessage());
                    }

                });
    }


    public interface OnTinNhanRealtimeListener {
        void onDanhSachThayDoi(List<TinNhan> danhSach);

        void onLoi(String loi);
    }

    public Query langNgheTinNhan(
            String maPhongChat,
            OnTinNhanRealtimeListener listener
    ) {
        if (maPhongChat == null || maPhongChat.trim().isEmpty()) {
            if (listener != null) {
                listener.onLoi("Mã phòng chat không hợp lệ");
            }
            return null;
        }

        Query query = FirebaseManager.tinNhan()
                .orderByChild("maPhongChat")
                .equalTo(maPhongChat);

        Map<String, TinNhan> danhSach = new HashMap<>();

        query.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                danhSach.clear();

                for (DataSnapshot item : snapshot.getChildren()) {
                    TinNhan tinNhan = item.getValue(TinNhan.class);

                    if (tinNhan != null) {
                        tinNhan.setMaTinNhan(item.getKey());
                        danhSach.put(item.getKey(), tinNhan);
                    }
                }

                List<TinNhan> ketQua = new ArrayList<>(danhSach.values());

                Collections.sort(
                        ketQua,
                        Comparator.comparingLong(TinNhan::getNgayTao)
                );

                if (listener != null) {
                    listener.onDanhSachThayDoi(ketQua);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                if (listener != null) {
                    listener.onLoi(error.getMessage());
                }
            }
        });

        return query;
    }


    // ==================== SỬA TIN NHẮN ====================
    public void suaTinNhan(
            String maTinNhan,
            String noiDung,
            OnTinNhanListener listener
    ) {

        // Kiểm tra mã tin nhắn
        if (maTinNhan == null
                || maTinNhan.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Mã tin nhắn không hợp lệ"
                );
            }

            return;
        }

        // Kiểm tra nội dung
        // Cho phép noiDung = null để thu hồi tin nhắn
        if (noiDung != null
                && noiDung.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Nội dung tin nhắn không hợp lệ"
                );
            }

            return;
        }

        // Lấy đường dẫn tin nhắn theo mã
        DatabaseReference ref =
                FirebaseManager.tinNhan()
                        .child(maTinNhan);

        // Chỉ cập nhật trường noiDung
        Map<String, Object> capNhat = new HashMap<>();

        capNhat.put("noiDung", noiDung);

        ref.updateChildren(capNhat)
                .addOnSuccessListener(unused -> {

                    if (listener != null) {
                        listener.onSuccess(maTinNhan);
                    }

                })
                .addOnFailureListener(e -> {

                    if (listener != null) {
                        listener.onFailure(
                                e.getMessage() != null
                                        ? e.getMessage()
                                        : "Sửa tin nhắn thất bại"
                        );
                    }

                });
    }

}


