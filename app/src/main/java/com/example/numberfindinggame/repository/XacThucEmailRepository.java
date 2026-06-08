package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.XacThucEmail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class XacThucEmailRepository {

    public interface OnCompleteListener {
        void onSuccess();

        void onFailure(String message);
    }

    public interface OnFindListener {
        void onFound(XacThucEmail xacThucEmail);

        void onNotFound();

        void onFailure(String message);
    }



    // Thêm mã xác thực
    public void them(XacThucEmail xacThucEmail,
                     OnCompleteListener listener) {

        FirebaseManager.nguoiDung()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean tonTai = false;

                        for (DataSnapshot item : snapshot.getChildren()) {

                            NguoiDung nguoiDung =
                                    item.getValue(NguoiDung.class);

                            if (nguoiDung != null
                                    && nguoiDung.getEmail() != null
                                    && nguoiDung.getEmail()
                                    .equalsIgnoreCase(xacThucEmail.getEmail())) {

                                tonTai = true;
                                break;
                            }
                        }

                        if (!tonTai) {

                            if (listener != null) {
                                listener.onFailure(
                                        "Email không tồn tại trong hệ thống"
                                );
                            }

                            return;
                        }

                        FirebaseManager.xacThucEmail()
                                .child(xacThucEmail.getEmail().replace(".", ","))
                                .setValue(xacThucEmail)
                                .addOnSuccessListener(unused -> {
                                    if (listener != null) {
                                        listener.onSuccess();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    if (listener != null) {
                                        listener.onFailure(e.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        if (listener != null) {
                            listener.onFailure(error.getMessage());
                        }
                    }
                });
    }

    public void xoa(String email,
                    OnCompleteListener listener) {

        String key = email.replace(".", ",");

        FirebaseManager.xacThucEmail()
                .child(key)
                .removeValue()
                .addOnSuccessListener(unused -> {

                    if (listener != null) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {

                    if (listener != null) {
                        listener.onFailure(e.getMessage());
                    }
                });
    }

    public void timTheoEmail(String email,
                             OnFindListener listener) {

        String key = email.replace(".", ",");

        FirebaseManager.xacThucEmail()
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            XacThucEmail xacThucEmail =
                                    snapshot.getValue(XacThucEmail.class);

                            if (listener != null) {
                                listener.onFound(xacThucEmail);
                            }

                        } else {

                            if (listener != null) {
                                listener.onNotFound();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        if (listener != null) {
                            listener.onFailure(error.getMessage());
                        }
                    }
                });
    }

}