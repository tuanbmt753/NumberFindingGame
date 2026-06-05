package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class NguoiDungRepository {

    public void kiemTraTonTai(
            String username,
            String email,
            String phone,
            OnCheckListener listener
    ) {
        FirebaseManager.nguoiDung()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot item : snapshot.getChildren()) {

                            NguoiDung nd = item.getValue(NguoiDung.class);

                            if (nd == null) continue;

                            if (username.equalsIgnoreCase(nd.getTenNguoiDung())) {
                                listener.onUsernameExists();
                                return;
                            }

                            if (email.equalsIgnoreCase(nd.getEmail())) {
                                listener.onEmailExists();
                                return;
                            }

                            if (phone.equals(nd.getPhone())) {
                                listener.onPhoneExists();
                                return;
                            }
                        }

                        listener.onSuccess();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void themNguoiDung(
            NguoiDung nguoiDung,
            OnCompleteListener<Void> listener
    ) {
        FirebaseManager.nguoiDung()
                .child(nguoiDung.getMaNguoiDung())
                .setValue(nguoiDung)
                .addOnCompleteListener(listener);
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

    public void dangNhap(
            String email,
            String matKhau,
            OnLoginListener listener
    ) {
        FirebaseManager.nguoiDung()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot item : snapshot.getChildren()) {

                            NguoiDung nd = item.getValue(NguoiDung.class);

                            if (nd == null) continue;

                            if (email.equalsIgnoreCase(nd.getEmail())
                                    && matKhau.equals(nd.getMatKhau())) {

                                listener.onSuccess(nd);
                                return;
                            }
                        }

                        listener.onFailed();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFailed();
                    }
                });
    }
}