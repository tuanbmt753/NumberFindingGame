package com.example.numberfindinggame.repository;

import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
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

    public interface DangHoatDongCallback {
        void onResult(Boolean dangHoatDong);
    }
}
