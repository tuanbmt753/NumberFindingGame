package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.constant.LoginType;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.helper.PasswordHelper;
import com.example.numberfindinggame.model.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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

        // Hash mật khẩu trước khi lưu
        String hashedPassword =
                PasswordHelper.hashPassword(
                        nguoiDung.getMatKhau()
                );

        nguoiDung.setMatKhau(hashedPassword);

        FirebaseManager.nguoiDung()
                .child(nguoiDung.getMaNguoiDung())
                .setValue(nguoiDung)
                .addOnCompleteListener(listener);
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

        String hashedPassword =
                PasswordHelper.hashPassword(matKhau);

        FirebaseManager.nguoiDung()
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot item : snapshot.getChildren()) {

                            NguoiDung nd = item.getValue(NguoiDung.class);

                            if (nd == null) continue;

                            if (LoginType.LOCAL.equals(nd.getLoaiDangNhap())
                                    && hashedPassword.equals(nd.getMatKhau())) {

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

    public void doiMatKhau(
            String email,
            String matKhau,
            OnCompleteListener<Void> listener
    ) {

        String hashedPassword =
                PasswordHelper.hashPassword(matKhau);

        FirebaseManager.nguoiDung()
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot item : snapshot.getChildren()) {

                            NguoiDung nd =
                                    item.getValue(NguoiDung.class);

                            if (nd == null) continue;

                            if (LoginType.LOCAL.equals(nd.getLoaiDangNhap())) {

                                Map<String, Object> updates = new HashMap<>();

                                updates.put(
                                        "matKhau",
                                        hashedPassword
                                );

                                updates.put(
                                        "ngayCapNhat",
                                        System.currentTimeMillis()
                                );

                                item.getRef()
                                        .updateChildren(updates)
                                        .addOnCompleteListener(listener);

                                return;
                            }
                        }

                        listener.onComplete(
                                Tasks.forException(
                                        new Exception("Không tìm thấy tài khoản")
                                )
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        listener.onComplete(
                                Tasks.forException(
                                        error.toException()
                                )
                        );
                    }
                });
    }

}