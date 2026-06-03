package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class NguoiDungRepository {

    public void themNguoiDung(NguoiDung nguoiDung) {

        FirebaseManager.nguoiDung()
                .child(nguoiDung.getMaNguoiDung())
                .setValue(nguoiDung);
    }

    public void layNguoiDung(
            String maNguoiDung,
            ValueEventListener listener) {

        FirebaseManager.nguoiDung()
                .child(maNguoiDung)
                .addListenerForSingleValueEvent(listener);
    }

    public void xoaNguoiDung(String maNguoiDung) {

        FirebaseManager.nguoiDung()
                .child(maNguoiDung)
                .removeValue();
    }

    public void capNhatDangNhapCuoi(
            String maNguoiDung) {

        FirebaseManager.nguoiDung()
                .child(maNguoiDung)
                .child("dangNhapCuoi")
                .setValue(System.currentTimeMillis());
    }
}