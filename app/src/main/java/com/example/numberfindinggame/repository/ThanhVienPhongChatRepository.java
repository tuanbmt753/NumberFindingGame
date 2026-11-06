
package com.example.numberfindinggame.repository;

import com.example.numberfindinggame.constant.LoaiPhongChat;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.PhongGhat;
import com.example.numberfindinggame.model.ThanhVienPhongChat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class ThanhVienPhongChatRepository {

    public interface OnTimHoacTaoPhongChatListener {
        void onSuccess(String maPhongChat);

        void onFailure(String loi);
    }

    /**
     * Tìm phòng chat bạn bè đã tồn tại.
     * Nếu chưa có thì tạo phòng mới và thêm thành viên.
     * <p>
     * maNguoiDungList: Danh sách mã của đúng 2 người dùng.
     */
    public void timHoacTaoPhongChat(
            List<String> maNguoiDungList,
            String loaiPhongChat,
            OnTimHoacTaoPhongChatListener listener
    ) {

        if (maNguoiDungList == null
                || maNguoiDungList.size() != 2
                || !LoaiPhongChat.FRIEND.equals(loaiPhongChat)) {

            if (listener != null) {
                listener.onFailure(
                        "Danh sách thành viên hoặc loại phòng chat không hợp lệ"
                );
            }
            return;
        }

        Set<String> maNguoiDungSet = new HashSet<>();

        for (String maNguoiDung : maNguoiDungList) {
            if (maNguoiDung == null
                    || maNguoiDung.trim().isEmpty()) {

                if (listener != null) {
                    listener.onFailure(
                            "Mã người dùng không được để trống"
                    );
                }
                return;
            }

            maNguoiDungSet.add(maNguoiDung.trim());
        }

        if (maNguoiDungSet.size() != 2) {
            if (listener != null) {
                listener.onFailure(
                        "Hai thành viên phải là hai người dùng khác nhau"
                );
            }
            return;
        }

        // Bước 1: Lấy danh sách phòng chat
        FirebaseManager.phongChat()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(
                                    DataSnapshot phongSnapshot
                            ) {

                                // Bước 2: Lấy danh sách thành viên
                                FirebaseManager.thanhVienPhongChat()
                                        .addListenerForSingleValueEvent(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(
                                                            DataSnapshot thanhVienSnapshot
                                                    ) {

                                                        // Lưu thành viên theo mã phòng
                                                        Map<String, Set<String>>
                                                                thanhVienTheoPhong =
                                                                new HashMap<>();

                                                        for (DataSnapshot item
                                                                : thanhVienSnapshot.getChildren()) {

                                                            ThanhVienPhongChat tv =
                                                                    item.getValue(
                                                                            ThanhVienPhongChat.class
                                                                    );

                                                            if (tv == null
                                                                    || tv.getMaPhongChat() == null
                                                                    || tv.getMaNguoiDung() == null) {
                                                                continue;
                                                            }

                                                            thanhVienTheoPhong
                                                                    .computeIfAbsent(
                                                                            tv.getMaPhongChat(),
                                                                            k -> new HashSet<>()
                                                                    )
                                                                    .add(
                                                                            tv.getMaNguoiDung()
                                                                    );
                                                        }

                                                        // Bước 3: Tìm phòng FRIEND
                                                        // có đúng hai thành viên cần tìm
                                                        for (DataSnapshot item
                                                                : phongSnapshot.getChildren()) {

                                                            PhongGhat phong =
                                                                    item.getValue(
                                                                            PhongGhat.class
                                                                    );

                                                            if (phong == null
                                                                    || !LoaiPhongChat.FRIEND
                                                                    .equals(phong.getLoaiPhongChat())) {
                                                                continue;
                                                            }

                                                            String maPhong =
                                                                    phong.getMaPhongChat();

                                                            if (maPhong == null) {
                                                                maPhong = item.getKey();
                                                            }

                                                            if (maPhong == null) {
                                                                continue;
                                                            }

                                                            Set<String> thanhVien =
                                                                    thanhVienTheoPhong.get(
                                                                            maPhong
                                                                    );

                                                            if (thanhVien != null
                                                                    && thanhVien.size() == 2
                                                                    && thanhVien.containsAll(
                                                                    maNguoiDungSet
                                                            )) {

                                                                // Đã tồn tại phòng chat
                                                                if (listener != null) {
                                                                    listener.onSuccess(
                                                                            maPhong
                                                                    );
                                                                }
                                                                return;
                                                            }
                                                        }

                                                        // Bước 4: Chưa có phòng
                                                        // => tạo phòng mới
                                                        taoPhongChatVaThemThanhVien(
                                                                new ArrayList<>(
                                                                        maNguoiDungSet
                                                                ),
                                                                loaiPhongChat,
                                                                listener
                                                        );
                                                    }

                                                    @Override
                                                    public void onCancelled(
                                                            DatabaseError error
                                                    ) {
                                                        if (listener != null) {
                                                            listener.onFailure(
                                                                    error.getMessage()
                                                            );
                                                        }
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onCancelled(
                                    DatabaseError error
                            ) {
                                if (listener != null) {
                                    listener.onFailure(
                                            error.getMessage()
                                    );
                                }
                            }
                        }
                );
    }

    /**
     * Tạo phòng chat mới và thêm thành viên vào phòng.
     */
    private void taoPhongChatVaThemThanhVien(
            List<String> maNguoiDungList,
            String loaiPhongChat,
            OnTimHoacTaoPhongChatListener listener
    ) {

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

        long ngayTao = System.currentTimeMillis();

        PhongGhat phongChat = new PhongGhat(
                maPhongChat,
                loaiPhongChat,
                ngayTao
        );

        // Lưu phòng chat trước
        ref.setValue(phongChat)
                .addOnSuccessListener(unused -> {

                    // Sau đó thêm cả hai thành viên
                    themDanhSachThanhVien(
                            maPhongChat,
                            maNguoiDungList,
                            new OnThemThanhVienPhongChatListener() {

                                @Override
                                public void onSuccess() {
                                    if (listener != null) {
                                        listener.onSuccess(
                                                maPhongChat
                                        );
                                    }
                                }

                                @Override
                                public void onFailure(String loi) {

                                    // Xóa phòng nếu không thể thêm thành viên
                                    ref.removeValue();

                                    if (listener != null) {
                                        listener.onFailure(loi);
                                    }
                                }
                            }
                    );
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onFailure(
                                e.getMessage() != null
                                        ? e.getMessage()
                                        : "Tạo phòng chat thất bại"
                        );
                    }
                });
    }

    public interface OnThemThanhVienPhongChatListener {
        void onSuccess();

        void onFailure(String loi);
    }

    /**
     * Thêm danh sách thành viên vào một phòng chat.
     */
    public void themDanhSachThanhVien(
            String maPhongChat,
            List<String> maNguoiDungList,
            OnThemThanhVienPhongChatListener listener
    ) {

        if (maPhongChat == null
                || maPhongChat.trim().isEmpty()
                || maNguoiDungList == null
                || maNguoiDungList.isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Thông tin phòng chat hoặc thành viên không hợp lệ"
                );
            }
            return;
        }

        DatabaseReference ref =
                FirebaseManager.thanhVienPhongChat();

        long ngayTao = System.currentTimeMillis();

        Map<String, Object> duLieu = new HashMap<>();

        for (String maNguoiDung : maNguoiDungList) {

            if (maNguoiDung == null
                    || maNguoiDung.trim().isEmpty()) {

                if (listener != null) {
                    listener.onFailure(
                            "Mã người dùng không hợp lệ"
                    );
                }
                return;
            }

            String maThanhVien = ref.push().getKey();

            if (maThanhVien == null) {
                if (listener != null) {
                    listener.onFailure(
                            "Không thể tạo mã thành viên"
                    );
                }
                return;
            }

            ThanhVienPhongChat thanhVien =
                    new ThanhVienPhongChat(
                            maThanhVien,
                            maPhongChat,
                            maNguoiDung.trim(),
                            ngayTao
                    );

            duLieu.put(maThanhVien, thanhVien);
        }

        // Ghi cả danh sách trong một lần cập nhật
        ref.updateChildren(duLieu)
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
                                        : "Thêm thành viên thất bại"
                        );
                    }
                });
    }


    /**
     * Xóa phòng chat và toàn bộ thành viên của phòng
     * dựa trên danh sách người dùng và loại phòng chat.
     */
    public void xoaPhongChat(
            List<String> maNguoiDungList,
            String loaiPhongChat,
            OnXoaPhongChatListener listener
    ) {

        if (maNguoiDungList == null
                || maNguoiDungList.size() != 2
                || loaiPhongChat == null
                || loaiPhongChat.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure(
                        "Thông tin phòng chat không hợp lệ"
                );
            }
            return;
        }

        Set<String> maNguoiDungSet = new HashSet<>();

        for (String maNguoiDung : maNguoiDungList) {
            if (maNguoiDung == null
                    || maNguoiDung.trim().isEmpty()) {

                if (listener != null) {
                    listener.onFailure(
                            "Mã người dùng không được để trống"
                    );
                }
                return;
            }

            maNguoiDungSet.add(maNguoiDung.trim());
        }

        if (maNguoiDungSet.size() != 2) {
            if (listener != null) {
                listener.onFailure(
                        "Hai mã người dùng phải khác nhau"
                );
            }
            return;
        }

        // Lấy danh sách phòng chat
        FirebaseManager.phongChat()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(
                                    DataSnapshot phongSnapshot
                            ) {

                                // Lấy danh sách thành viên phòng chat
                                FirebaseManager.thanhVienPhongChat()
                                        .addListenerForSingleValueEvent(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(
                                                            DataSnapshot thanhVienSnapshot
                                                    ) {

                                                        Map<String, Set<String>>
                                                                thanhVienTheoPhong =
                                                                new HashMap<>();

                                                        // Gom thành viên theo mã phòng
                                                        for (DataSnapshot item
                                                                : thanhVienSnapshot.getChildren()) {

                                                            ThanhVienPhongChat tv =
                                                                    item.getValue(
                                                                            ThanhVienPhongChat.class
                                                                    );

                                                            if (tv == null
                                                                    || tv.getMaPhongChat() == null
                                                                    || tv.getMaNguoiDung() == null) {
                                                                continue;
                                                            }

                                                            thanhVienTheoPhong
                                                                    .computeIfAbsent(
                                                                            tv.getMaPhongChat(),
                                                                            k -> new HashSet<>()
                                                                    )
                                                                    .add(
                                                                            tv.getMaNguoiDung()
                                                                    );
                                                        }

                                                        List<String> maPhongCanXoa =
                                                                new ArrayList<>();

                                                        // Tìm phòng phù hợp
                                                        for (DataSnapshot item
                                                                : phongSnapshot.getChildren()) {

                                                            PhongGhat phong =
                                                                    item.getValue(
                                                                            PhongGhat.class
                                                                    );

                                                            if (phong == null
                                                                    || !loaiPhongChat.equals(
                                                                    phong.getLoaiPhongChat()
                                                            )) {
                                                                continue;
                                                            }

                                                            String maPhongChat =
                                                                    phong.getMaPhongChat();

                                                            if (maPhongChat == null) {
                                                                maPhongChat = item.getKey();
                                                            }

                                                            if (maPhongChat == null) {
                                                                continue;
                                                            }

                                                            Set<String> thanhVien =
                                                                    thanhVienTheoPhong.get(
                                                                            maPhongChat
                                                                    );

                                                            if (thanhVien != null
                                                                    && thanhVien.size() == 2
                                                                    && thanhVien.containsAll(
                                                                    maNguoiDungSet
                                                            )) {

                                                                maPhongCanXoa.add(
                                                                        maPhongChat
                                                                );
                                                            }
                                                        }

                                                        if (maPhongCanXoa.isEmpty()) {
                                                            if (listener != null) {
                                                                listener.onFailure(
                                                                        "Không tìm thấy phòng chat phù hợp"
                                                                );
                                                            }
                                                            return;
                                                        }

                                                        // Xóa toàn bộ phòng phù hợp
                                                        DatabaseReference root =
                                                                FirebaseManager.phongChat()
                                                                        .getParent();

                                                        Map<String, Object> updates =
                                                                new HashMap<>();

                                                        for (String maPhongChat
                                                                : maPhongCanXoa) {

                                                            updates.put(
                                                                    "PhongChat/" + maPhongChat,
                                                                    null
                                                            );

                                                            for (DataSnapshot item
                                                                    : thanhVienSnapshot.getChildren()) {

                                                                ThanhVienPhongChat tv =
                                                                        item.getValue(
                                                                                ThanhVienPhongChat.class
                                                                        );

                                                                if (tv != null
                                                                        && maPhongChat.equals(
                                                                        tv.getMaPhongChat()
                                                                )) {

                                                                    updates.put(
                                                                            "ThanhVienPhongChat/"
                                                                                    + item.getKey(),
                                                                            null
                                                                    );
                                                                }
                                                            }
                                                        }

                                                        // Xóa đồng thời hai loại dữ liệu
                                                        root.updateChildren(updates)
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

                                                    @Override
                                                    public void onCancelled(
                                                            DatabaseError error
                                                    ) {
                                                        if (listener != null) {
                                                            listener.onFailure(
                                                                    error.getMessage()
                                                            );
                                                        }
                                                    }
                                                }
                                        );
                            }

                            @Override
                            public void onCancelled(
                                    DatabaseError error
                            ) {
                                if (listener != null) {
                                    listener.onFailure(
                                            error.getMessage()
                                    );
                                }
                            }
                        }
                );
    }

    /**
     * Callback xóa phòng chat
     */
    public interface OnXoaPhongChatListener {
        void onSuccess();

        void onFailure(String loi);
    }

}
