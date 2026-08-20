package com.example.numberfindinggame.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    private static final FirebaseDatabase database =
            FirebaseDatabase.getInstance(
                    "https://numberfindinggame-default-rtdb.asia-southeast1.firebasedatabase.app"
            );

    public static DatabaseReference nguoiDung() {
        return database.getReference("NguoiDung");
    }

    public static DatabaseReference xacThucEmail() {
        return database.getReference("XacThucEmail");
    }

    public static DatabaseReference Emailjs() {
        return database.getReference("Emailjs");
    }

    public static DatabaseReference ThietBiDangNhap() {
        return database.getReference("ThietBiDangNhap");
    }

    public static DatabaseReference CaiDat() {
        return database.getReference("CaiDat");
    }

    public static DatabaseReference MaKhoiPhuc() {
        return database.getReference("MaKhoiPhuc");
    }

    public static DatabaseReference ManChoi() {
        return database.getReference("ManChoi");
    }
}
