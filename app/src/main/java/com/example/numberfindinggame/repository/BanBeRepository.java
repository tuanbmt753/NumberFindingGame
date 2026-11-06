
package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.constant.TrangThaiBanBe;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.BanBe;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanBeRepository {

    // =========================================================
    // 1. GỬI LỜI MỜI KẾT BẠN
    // =========================================================

    public void guiLoiMoiKetBan(
            String maNguoiDungGui,
            String maNguoiDungNhan,
            OnGuiLoiMoiListener listener
    ) {

        // Kiểm tra listener
        if (listener == null) {
            return;
        }

        // Kiểm tra mã người dùng
        if (maNguoiDungGui == null
                || maNguoiDungGui.trim().isEmpty()
                || maNguoiDungNhan == null
                || maNguoiDungNhan.trim().isEmpty()) {

            listener.onFailure();
            return;
        }

        // Không được tự gửi lời mời cho chính mình
        if (maNguoiDungGui.equals(maNguoiDungNhan)) {
            listener.onFailure();
            return;
        }

        // Kiểm tra quan hệ đã tồn tại hay chưa
        kiemTraTonTaiBanBe(
                maNguoiDungGui,
                maNguoiDungNhan,
                new OnCheckBanBeListener() {

                    @Override
                    public void onResult(
                            boolean tonTai,
                            BanBe banBe
                    ) {

                        /*
                         * ==================================================
                         * ĐÃ TỒN TẠI QUAN HỆ
                         * ==================================================
                         */
                        if (tonTai) {

                            // Không tìm thấy dữ liệu quan hệ
                            if (banBe == null) {
                                listener.onFailure();
                                return;
                            }

                            String trangThai =
                                    banBe.getTrangThai();

                            /*
                             * ==============================================
                             * QUAN HỆ ĐANG BỊ TỪ CHỐI
                             * ==============================================
                             */
                            if (TrangThaiBanBe.TU_CHOI
                                    .getGiaTri()
                                    .equals(trangThai)) {

                                String maBanBe =
                                        banBe.getMaBanBe();

                                if (maBanBe == null
                                        || maBanBe.trim().isEmpty()) {

                                    listener.onFailure();
                                    return;
                                }

                                DatabaseReference ref =
                                        FirebaseManager.banBe()
                                                .child(maBanBe);

                                Map<String, Object> capNhat =
                                        new HashMap<>();

                                capNhat.put(
                                        "trangThai",
                                        TrangThaiBanBe.LOI_MOI_KET_BAN
                                                .getGiaTri()
                                );

                                capNhat.put(
                                        "ngayTao",
                                        System.currentTimeMillis()
                                );

                                ref.updateChildren(capNhat)
                                        .addOnSuccessListener(unused -> {

                                            // Gửi lại lời mời thành công
                                            listener.onSuccess();

                                        })
                                        .addOnFailureListener(e -> {

                                            // Cập nhật thất bại
                                            listener.onFailure();

                                        });

                                return;
                            }

                            /*
                             * ==============================================
                             * ĐANG CÓ LỜI MỜI
                             * ==============================================
                             */
                            if (TrangThaiBanBe.LOI_MOI_KET_BAN
                                    .getGiaTri()
                                    .equals(trangThai)) {

                                // Đã gửi lời mời rồi
                                listener.onFailure();
                                return;
                            }

                            /*
                             * ==============================================
                             * ĐÃ LÀ BẠN
                             * ==============================================
                             */
                            if (TrangThaiBanBe.DONG_Y
                                    .getGiaTri()
                                    .equals(trangThai)) {

                                // Đã là bạn
                                listener.onFailure();
                                return;
                            }

                            // Trạng thái không xác định
                            listener.onFailure();
                            return;
                        }

                        /*
                         * ==================================================
                         * CHƯA TỒN TẠI QUAN HỆ
                         * ==================================================
                         */

                        DatabaseReference ref =
                                FirebaseManager.banBe().push();

                        String maBanBe = ref.getKey();

                        if (maBanBe == null) {
                            listener.onFailure();
                            return;
                        }

                        BanBe moiQuanHe = new BanBe();

                        moiQuanHe.setMaBanBe(
                                maBanBe
                        );

                        moiQuanHe.setMaNguoiDung1(
                                maNguoiDungGui
                        );

                        moiQuanHe.setMaNguoiDung2(
                                maNguoiDungNhan
                        );

                        moiQuanHe.setTrangThai(
                                TrangThaiBanBe.LOI_MOI_KET_BAN
                                        .getGiaTri()
                        );

                        moiQuanHe.setNgayTao(
                                System.currentTimeMillis()
                        );

                        ref.setValue(moiQuanHe)
                                .addOnSuccessListener(unused -> {

                                    // Tạo lời mời thành công
                                    listener.onSuccess();

                                })
                                .addOnFailureListener(e -> {

                                    // Tạo lời mời thất bại
                                    listener.onFailure();

                                });
                    }
                }
        );
    }


    // =========================================================
    // 2. KIỂM TRA TỒN TẠI QUAN HỆ BẠN BÈ
    // =========================================================

    public void kiemTraTonTaiBanBe(
            String maNguoiDung1,
            String maNguoiDung2,
            OnCheckBanBeListener listener
    ) {

        FirebaseManager.banBe()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                for (DataSnapshot item
                                        : snapshot.getChildren()) {

                                    BanBe banBe =
                                            item.getValue(BanBe.class);

                                    if (banBe == null) {
                                        continue;
                                    }

                                    String user1 =
                                            banBe.getMaNguoiDung1();

                                    String user2 =
                                            banBe.getMaNguoiDung2();

                                    if (user1 == null
                                            || user2 == null) {
                                        continue;
                                    }

                                    boolean dungChieu =
                                            user1.equals(maNguoiDung1)
                                                    && user2.equals(maNguoiDung2);

                                    boolean nguocChieu =
                                            user1.equals(maNguoiDung2)
                                                    && user2.equals(maNguoiDung1);

                                    if (dungChieu || nguocChieu) {

                                        if (listener != null) {
                                            listener.onResult(
                                                    true,
                                                    banBe
                                            );
                                        }

                                        return;
                                    }
                                }

                                if (listener != null) {
                                    listener.onResult(
                                            false,
                                            null
                                    );
                                }
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                if (listener != null) {
                                    listener.onResult(
                                            false,
                                            null
                                    );
                                }
                            }
                        }
                );
    }


    // =========================================================
    // 3. ĐỒNG Ý LỜI MỜI
    // =========================================================

    public void dongYLoiMoi(
            String maBanBe,
            OnUpdateBanBeListener listener
    ) {

        if (listener == null) {
            return;
        }

        if (maBanBe == null
                || maBanBe.trim().isEmpty()) {

            listener.onFailure();
            return;
        }

        FirebaseManager.banBe()
                .child(maBanBe)
                .child("trangThai")
                .setValue(
                        TrangThaiBanBe.DONG_Y
                                .getGiaTri()
                )
                .addOnSuccessListener(unused -> {

                    // Lưu dữ liệu thành công
                    listener.onSuccess();

                })
                .addOnFailureListener(e -> {

                    // Lưu dữ liệu thất bại
                    listener.onFailure();

                });
    }


    // =========================================================
    // 4. TỪ CHỐI LỜI MỜI
    // =========================================================

    public void tuChoiLoiMoi(
            String maBanBe,
            OnUpdateBanBeListener listener
    ) {

        if (listener == null) {
            return;
        }

        if (maBanBe == null
                || maBanBe.trim().isEmpty()) {

            listener.onFailure();
            return;
        }

        FirebaseManager.banBe()
                .child(maBanBe)
                .child("trangThai")
                .setValue(
                        TrangThaiBanBe.TU_CHOI
                                .getGiaTri()
                )
                .addOnSuccessListener(unused -> {

                    // Lưu dữ liệu thành công
                    listener.onSuccess();

                })
                .addOnFailureListener(e -> {

                    // Lưu dữ liệu thất bại
                    listener.onFailure();

                });
    }


    // =========================================================
    // 5. HỦY LỜI MỜI
    // =========================================================

    public void huyLoiMoi(
            String maBanBe
    ) {

        if (maBanBe == null
                || maBanBe.trim().isEmpty()) {

            return;
        }

        FirebaseManager.banBe()
                .child(maBanBe)
                .removeValue();
    }


    // =========================================================
    // 6. LẤY DANH SÁCH BẠN BÈ
    // =========================================================


    public void layDanhSachBanBe(
            String maNguoiDung,
            OnGetBanBeListener listener
    ) {

        if (maNguoiDung == null
                || maNguoiDung.trim().isEmpty()) {

            if (listener != null) {
                listener.onResult(new ArrayList<>());
            }

            return;
        }

        FirebaseManager.banBe()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                List<BanBe> danhSach =
                                        new ArrayList<>();

                                // Bước 1: Lấy toàn bộ quan hệ bạn bè
                                for (DataSnapshot item
                                        : snapshot.getChildren()) {

                                    BanBe banBe =
                                            item.getValue(BanBe.class);

                                    if (banBe == null) {
                                        continue;
                                    }

                                    // Chỉ lấy quan hệ đã đồng ý
                                    if (!TrangThaiBanBe.DONG_Y
                                            .getGiaTri()
                                            .equals(banBe.getTrangThai())) {
                                        continue;
                                    }

                                    String user1 =
                                            banBe.getMaNguoiDung1();

                                    String user2 =
                                            banBe.getMaNguoiDung2();

                                    // Chỉ lấy quan hệ có người dùng hiện tại
                                    if (maNguoiDung.equals(user1)
                                            || maNguoiDung.equals(user2)) {

                                        danhSach.add(banBe);
                                    }
                                }

                                // Bước 2: Chuẩn hóa thứ tự mã người dùng
                                for (BanBe banBe : danhSach) {

                                    String user1 =
                                            banBe.getMaNguoiDung1();

                                    String user2 =
                                            banBe.getMaNguoiDung2();

                                    // Nếu người dùng hiện tại nằm ở user1,
                                    // đổi chỗ để user1 luôn là mã bạn bè
                                    if (maNguoiDung.equals(user1)) {

                                        banBe.setMaNguoiDung1(user2);
                                        banBe.setMaNguoiDung2(user1);
                                    }
                                }

                                // Bước 3: Trả danh sách đã chuẩn hóa
                                if (listener != null) {
                                    listener.onResult(danhSach);
                                }
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                if (listener != null) {
                                    listener.onResult(new ArrayList<>());
                                }
                            }
                        }
                );
    }


    // =========================================================
    // 7. KIỂM TRA TRẠNG THÁI BẠN BÈ
    // =========================================================
    public void kiemTraTrangThaiBanBe(
            String maNguoiDungGui,
            String maNguoiDungNhan,
            OnCheckTrangThaiBanBeListener listener
    ) {

        if (listener == null) {
            return;
        }

        // Kiểm tra dữ liệu đầu vào
        if (maNguoiDungGui == null
                || maNguoiDungGui.trim().isEmpty()
                || maNguoiDungNhan == null
                || maNguoiDungNhan.trim().isEmpty()) {

            listener.onResult(false);
            return;
        }

        // Không kiểm tra chính mình
        if (maNguoiDungGui.equals(maNguoiDungNhan)) {
            listener.onResult(false);
            return;
        }

        FirebaseManager.banBe()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                for (DataSnapshot item :
                                        snapshot.getChildren()) {

                                    BanBe banBe =
                                            item.getValue(BanBe.class);

                                    if (banBe == null) {
                                        continue;
                                    }

                                    String nguoiDung1 =
                                            banBe.getMaNguoiDung1();

                                    String nguoiDung2 =
                                            banBe.getMaNguoiDung2();

                                    // Kiểm tra quan hệ giữa 2 người
                                    boolean dungNguoi =
                                            (maNguoiDungGui.equals(nguoiDung1)
                                                    && maNguoiDungNhan.equals(nguoiDung2))
                                                    ||
                                                    (maNguoiDungGui.equals(nguoiDung2)
                                                            && maNguoiDungNhan.equals(nguoiDung1));

                                    if (!dungNguoi) {
                                        continue;
                                    }

                                    String trangThai =
                                            banBe.getTrangThai();

                                    // Đồng ý
                                    if (TrangThaiBanBe.DONG_Y
                                            .getGiaTri()
                                            .equals(trangThai)) {

                                        listener.onResult(true);
                                        return;
                                    }

                                    // Đang có lời mời kết bạn
                                    if (TrangThaiBanBe.LOI_MOI_KET_BAN
                                            .getGiaTri()
                                            .equals(trangThai)) {

                                        listener.onResult(true);
                                        return;
                                    }

                                    // Từ chối
                                    if (TrangThaiBanBe.TU_CHOI
                                            .getGiaTri()
                                            .equals(trangThai)) {

                                        listener.onResult(false);
                                        return;
                                    }
                                }

                                // Chưa tồn tại quan hệ
                                listener.onResult(false);
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                listener.onResult(false);
                            }
                        }
                );
    }

    // =========================================================
    // 7. LẤY DANH SÁCH LỜI MỜI KẾT BẠN
    // =========================================================
    public void layDanhSachLoiMoiKetBan(
            String maNguoiDung,
            OnGetBanBeListener listener
    ) {

        if (maNguoiDung == null
                || maNguoiDung.trim().isEmpty()) {

            if (listener != null) {
                listener.onResult(
                        new ArrayList<>()
                );
            }

            return;
        }

        FirebaseManager.banBe()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                List<BanBe> danhSach =
                                        new ArrayList<>();

                                for (DataSnapshot item
                                        : snapshot.getChildren()) {

                                    BanBe banBe =
                                            item.getValue(BanBe.class);

                                    if (banBe == null) {
                                        continue;
                                    }

                                    // Chỉ lấy lời mời kết bạn
                                    if (!TrangThaiBanBe.LOI_MOI_KET_BAN
                                            .getGiaTri()
                                            .equals(
                                                    banBe.getTrangThai()
                                            )) {

                                        continue;
                                    }

                                    String user2 =
                                            banBe.getMaNguoiDung2();

                                    // Người nhận lời mời phải là tôi
                                    if (maNguoiDung.equals(user2)) {

                                        danhSach.add(banBe);
                                    }
                                }

                                if (listener != null) {
                                    listener.onResult(
                                            danhSach
                                    );
                                }
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                if (listener != null) {
                                    listener.onResult(
                                            new ArrayList<>()
                                    );
                                }
                            }
                        }
                );
    }

    public void donDepDuLieuBanBe(
            String maNguoiDung1,
            String maNguoiDung2,
            OnCompleteListener<Void> listener
    ) {

        if (maNguoiDung1 == null || maNguoiDung2 == null) {
            if (listener != null) {
                listener.onComplete(
                        Tasks.forException(
                                new Exception(
                                        "Mã người dùng không được null"
                                )
                        )
                );
            }
            return;
        }

        FirebaseManager.banBe()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                List<BanBe> danhSach = new ArrayList<>();

                                /*
                                 * Tìm tất cả dữ liệu BanBe
                                 * của 2 người dùng, không quan tâm chiều.
                                 */
                                for (DataSnapshot item :
                                        snapshot.getChildren()) {

                                    BanBe banBe =
                                            item.getValue(BanBe.class);

                                    if (banBe == null) {
                                        continue;
                                    }

                                    String nd1 =
                                            banBe.getMaNguoiDung1();

                                    String nd2 =
                                            banBe.getMaNguoiDung2();

                                    if (nd1 == null || nd2 == null) {
                                        continue;
                                    }

                                    /*
                                     * Kiểm tra 2 chiều:
                                     *
                                     * A -> B
                                     * hoặc
                                     * B -> A
                                     */
                                    boolean dungCap =
                                            (
                                                    nd1.equals(maNguoiDung1)
                                                            &&
                                                            nd2.equals(maNguoiDung2)
                                            )
                                                    ||
                                                    (
                                                            nd1.equals(maNguoiDung2)
                                                                    &&
                                                                    nd2.equals(maNguoiDung1)
                                                    );

                                    if (dungCap) {

                                        banBe.setMaBanBe(
                                                item.getKey()
                                        );

                                        danhSach.add(banBe);
                                    }
                                }

                                /*
                                 * Có 0 hoặc 1 dữ liệu
                                 * thì không cần dọn.
                                 */
                                if (danhSach.size() <= 1) {

                                    if (listener != null) {
                                        listener.onComplete(
                                                Tasks.forResult(null)
                                        );
                                    }

                                    return;
                                }

                                /*
                                 * Tìm dữ liệu có ngayTao NHỎ NHẤT
                                 *
                                 * Ví dụ:
                                 *
                                 * 15/09/2025 -> giữ
                                 * 16/09/2025 -> xóa
                                 */
                                BanBe duLieuCuNhat = null;

                                for (BanBe banBe : danhSach) {

                                    if (duLieuCuNhat == null) {

                                        duLieuCuNhat = banBe;

                                    } else if (
                                            banBe.getNgayTao()
                                                    <
                                                    duLieuCuNhat.getNgayTao()
                                    ) {

                                        duLieuCuNhat = banBe;
                                    }
                                }

                                /*
                                 * Tạo danh sách dữ liệu cần xóa.
                                 *
                                 * Giữ lại duy nhất dữ liệu
                                 * có ngayTao nhỏ nhất.
                                 */
                                Map<String, Object> updates =
                                        new HashMap<>();

                                for (BanBe banBe : danhSach) {

                                    if (banBe.getMaBanBe() == null) {
                                        continue;
                                    }

                                    if (!banBe.getMaBanBe().equals(
                                            duLieuCuNhat.getMaBanBe()
                                    )) {

                                        updates.put(
                                                banBe.getMaBanBe(),
                                                null
                                        );
                                    }
                                }

                                /*
                                 * Không có gì cần xóa
                                 */
                                if (updates.isEmpty()) {

                                    if (listener != null) {
                                        listener.onComplete(
                                                Tasks.forResult(null)
                                        );
                                    }

                                    return;
                                }

                                /*
                                 * Xóa toàn bộ dữ liệu trùng
                                 */
                                FirebaseManager.banBe()
                                        .updateChildren(updates)
                                        .addOnCompleteListener(
                                                listener
                                        );
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                if (listener != null) {
                                    listener.onComplete(
                                            Tasks.forException(
                                                    error.toException()
                                            )
                                    );
                                }
                            }
                        }
                );
    }


    public void xoaLoiMoi(
            String maBanBe,
            OnUpdateBanBeListener listener
    ) {

        if (maBanBe == null
                || maBanBe.trim().isEmpty()) {

            if (listener != null) {
                listener.onFailure();
            }

            return;
        }

        FirebaseManager.banBe()
                .child(maBanBe)
                .removeValue()
                .addOnSuccessListener(unused -> {

                    if (listener != null) {
                        listener.onSuccess();
                    }

                })
                .addOnFailureListener(e -> {

                    if (listener != null) {
                        listener.onFailure();
                    }

                });
    }


    public void demSoLoiMoiKetBan(
            String maNguoiDung,
            OnCountLoiMoiListener listener
    ) {

        if (maNguoiDung == null
                || maNguoiDung.trim().isEmpty()) {

            if (listener != null) {
                listener.onResult(0);
            }

            return;
        }

        FirebaseManager.banBe()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                int soLuong = 0;

                                for (DataSnapshot item
                                        : snapshot.getChildren()) {

                                    BanBe banBe =
                                            item.getValue(BanBe.class);

                                    if (banBe == null) {
                                        continue;
                                    }

                                    // Chỉ lấy lời mời kết bạn
                                    if (!TrangThaiBanBe.LOI_MOI_KET_BAN
                                            .getGiaTri()
                                            .equals(
                                                    banBe.getTrangThai()
                                            )) {

                                        continue;
                                    }

                                    String user2 =
                                            banBe.getMaNguoiDung2();

                                    // Người nhận lời mời là tôi
                                    if (maNguoiDung.equals(user2)) {

                                        soLuong++;
                                    }
                                }

                                if (listener != null) {
                                    listener.onResult(soLuong);
                                }
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                if (listener != null) {
                                    listener.onResult(0);
                                }
                            }
                        }
                );
    }

    // =========================================================
    // INTERFACE KIỂM TRA TỒN TẠI
    // =========================================================

    public interface OnCheckBanBeListener {

        void onResult(
                boolean tonTai,
                BanBe banBe
        );
    }


    // =========================================================
    // INTERFACE LẤY DANH SÁCH
    // =========================================================

    public interface OnGetBanBeListener {

        void onResult(
                List<BanBe> danhSach
        );
    }


    public interface OnGuiLoiMoiListener {

        void onSuccess();

        void onFailure();
    }

    public interface OnCheckTrangThaiBanBeListener {

        void onResult(boolean ketQua);
    }

    public interface OnUpdateBanBeListener {

        void onSuccess();

        void onFailure();
    }

    public interface OnCountLoiMoiListener {

        void onResult(int soLuong);

    }

}

