package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.callback.DanhSachThietBiCallback;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThietBiDangNhapRepository {
    public void luuThietBiDangNhap(
            ThietBiDangNhap thietBi,
            OnCompleteListener<Void> listener
    ) {

        Map<String, Object> data = new HashMap<>();
        data.put("tenThietBi", thietBi.getTenThietBi());
        data.put("ngayTao", thietBi.getNgayTao());
        data.put("ngayCapNhatCuoi", thietBi.getNgayCapNhatCuoi());
        data.put("dangHoatDong", thietBi.getDangHoatDong());

        FirebaseManager.ThietBiDangNhap()
                .child(thietBi.getMaNguoiDung())
                .child(thietBi.getMaThietBi())
                .setValue(data)
                .addOnCompleteListener(listener);
    }

    public void layThietBi(
            String maNguoiDung,
            String maThietBi,
            ValueEventListener listener
    ) {
        FirebaseManager.ThietBiDangNhap()
                .child(maNguoiDung)
                .child(maThietBi)
                .addListenerForSingleValueEvent(listener);
    }

    public void capNhatLanDangNhapCuoi(
            String maNguoiDung,
            String maThietBi
    ) {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "ngayCapNhatCuoi",
                System.currentTimeMillis()
        );

        data.put(
                "dangHoatDong",
                true
        );

        FirebaseManager.ThietBiDangNhap()
                .child(maNguoiDung)
                .child(maThietBi)
                .updateChildren(data);
    }

    public void kiemTraDangHoatDong(
            String maNguoiDung,
            String maThietBi,
            DangHoatDongCallback callback
    ) {

        FirebaseManager.ThietBiDangNhap()
                .child(maNguoiDung)
                .child(maThietBi)
                .child("dangHoatDong")
                .get()
                .addOnSuccessListener(snapshot -> {

                    Boolean dangHoatDong =
                            snapshot.getValue(Boolean.class);

                    callback.onResult(dangHoatDong);

                })
                .addOnFailureListener(e -> {

                    callback.onResult(null);

                });
    }


    public ValueEventListener theoDoiDangHoatDong(
            String maNguoiDung,
            String maThietBi,
            DangHoatDongCallback callback
    ) {

        DatabaseReference ref =
                FirebaseManager.ThietBiDangNhap()
                        .child(maNguoiDung)
                        .child(maThietBi);


        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onResult(null);
                    return;
                }

                Boolean dangHoatDong =
                        snapshot.child("dangHoatDong")
                                .getValue(Boolean.class);

                callback.onResult(dangHoatDong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult(null);
            }
        };

        ref.addValueEventListener(listener);

        return listener;
    }

    public ValueEventListener theoDoiDanhSachThietBi(
            String maNguoiDung,
            DanhSachThietBiCallback callback
    ) {

        ValueEventListener listener =
                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot
                    ) {

                        List<ThietBiDangNhap> danhSach =
                                new ArrayList<>();

                        for (DataSnapshot item : snapshot.getChildren()) {

                            ThietBiDangNhap thietBi =
                                    item.getValue(
                                            ThietBiDangNhap.class
                                    );

                            if (thietBi != null) {

                                // Lấy từ key Firebase
                                thietBi.setMaNguoiDung(
                                        maNguoiDung
                                );

                                thietBi.setMaThietBi(
                                        item.getKey()
                                );

                                danhSach.add(thietBi);
                            }
                        }

                        callback.onResult(danhSach);
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error
                    ) {

                        callback.onResult(
                                new ArrayList<>()
                        );
                    }
                };

        FirebaseManager.ThietBiDangNhap()
                .child(maNguoiDung)
                .addValueEventListener(listener);

        return listener;
    }

    public void voHieuHoaThietBi(
            String maNguoiDung,
            String maThietBi,
            OnCompleteListener<Void> listener
    ) {

        FirebaseManager.ThietBiDangNhap()
                .child(maNguoiDung)
                .child(maThietBi)
                .child("dangHoatDong")
                .setValue(false)
                .addOnCompleteListener(listener);
    }


    public interface DangHoatDongCallback {
        void onResult(Boolean dangHoatDong);
    }
}
