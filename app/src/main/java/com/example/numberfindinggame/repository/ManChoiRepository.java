package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.ManChoi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ManChoiRepository {

    public interface OnCompleteListener {
        void onSuccess();

        void onFailed(String error);
    }

    public static void themHoacCapNhat(ManChoi manChoi,
                                       OnCompleteListener listener) {

        FirebaseManager.ManChoi()
                .child(manChoi.getMaNguoiDung())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // Chưa có dữ liệu -> thêm mới
                        if (!snapshot.exists()) {
                            luuManChoi(manChoi, listener);
                            return;
                        }

                        // Đã có dữ liệu
                        ManChoi manChoiCu = snapshot.getValue(ManChoi.class);

                        if (manChoiCu != null
                                && manChoi.getManChoi() > manChoiCu.getManChoi()) {

                            // Màn mới lớn hơn -> cập nhật
                            luuManChoi(manChoi, listener);

                        } else {

                            // Không cập nhật
                            listener.onSuccess();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFailed(error.getMessage());
                    }
                });
    }

    /**
     * Lưu dữ liệu lên Firebase
     */
    private static void luuManChoi(ManChoi manChoi,
                                   OnCompleteListener listener) {

        FirebaseManager.ManChoi()
                .child(manChoi.getMaNguoiDung())
                .setValue(manChoi)
                .addOnSuccessListener(unused -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailed(e.getMessage()));
    }

    public static void layTheoMaNguoiDung(String maNguoiDung,
                                          OnGetListener listener) {

        FirebaseManager.ManChoi()
                .child(maNguoiDung)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            ManChoi manChoi = snapshot.getValue(ManChoi.class);
                            listener.onSuccess(manChoi);
                        } else {
                            listener.onSuccess(null);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFailed(error.getMessage());
                    }
                });
    }

    public interface OnGetListener {
        void onSuccess(ManChoi manChoi);

        void onFailed(String error);
    }

}

