package com.example.numberfindinggame.repository;

import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.MaKhoiPhuc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

public class MaKhoiPhucRepository {

    public static void themMaKhoiPhuc(
            MaKhoiPhuc maKhoiPhuc,
            OnCompleteListener<Void> listener
    ) {

        FirebaseManager.MaKhoiPhuc()
                .child(maKhoiPhuc.getMaNguoiDung())
                .setValue(maKhoiPhuc)
                .addOnCompleteListener(listener);

    }

    public static void layMaKhoiPhuc(
            String maNguoiDung,
            ValueEventListener listener
    ) {

        FirebaseManager.MaKhoiPhuc()
                .child(maNguoiDung)
                .addListenerForSingleValueEvent(listener);

    }

    public static void xoaMaKhoiPhuc(
            String maNguoiDung,
            OnCompleteListener<Void> listener
    ) {

        FirebaseManager.MaKhoiPhuc()
                .child(maNguoiDung)
                .removeValue()
                .addOnCompleteListener(listener);

    }

    public static void theoDoiMaKhoiPhuc(
            String maNguoiDung,
            ValueEventListener listener
    ) {

        FirebaseManager.MaKhoiPhuc()
                .child(maNguoiDung)
                .addValueEventListener(listener);

    }

}
