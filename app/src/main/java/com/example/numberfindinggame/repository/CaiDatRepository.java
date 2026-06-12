package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.CaiDat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CaiDatRepository {

    public static void luuCaiDat(
            CaiDat caiDat,
            OnCompleteListener<Void> listener
    ) {

        if (caiDat == null ||
                caiDat.getMaNguoiDung() == null ||
                caiDat.getMaNguoiDung().trim().isEmpty()) {
            return;
        }

        DatabaseReference ref =
                FirebaseManager.CaiDat()
                        .child(caiDat.getMaNguoiDung());

        caiDat.setNgayCapNhap(System.currentTimeMillis());

        ref.setValue(caiDat)
                .addOnCompleteListener(listener);
    }

    public static void layCaiDat(
            String maNguoiDung,
            CaiDatCallback callback
    ) {

        FirebaseManager.CaiDat()
                .child(maNguoiDung)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                if (!snapshot.exists()) {
                                    callback.onFailure(
                                            "Không tìm thấy cài đặt"
                                    );
                                    return;
                                }

                                CaiDat caiDat =
                                        snapshot.getValue(
                                                CaiDat.class
                                        );

                                callback.onSuccess(caiDat);
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {
                                callback.onFailure(
                                        error.getMessage()
                                );
                            }
                        }
                );
    }

    public static void taoMacDinhNeuChuaCo(
            String maNguoiDung,
            OnCompleteListener<Void> listener
    ) {

        FirebaseManager.CaiDat()
                .child(maNguoiDung)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                if (snapshot.exists()) {

                                    if (listener != null) {
                                        listener.onComplete(
                                                Tasks.forResult(null)
                                        );
                                    }

                                    return;
                                }

                                long now =
                                        System.currentTimeMillis();

                                CaiDat caiDat =
                                        new CaiDat(
                                                maNguoiDung,
                                                100,
                                                100,
                                                false,
                                                false,
                                                now,
                                                now
                                        );

                                FirebaseManager.CaiDat()
                                        .child(maNguoiDung)
                                        .setValue(caiDat)
                                        .addOnCompleteListener(listener);
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
}