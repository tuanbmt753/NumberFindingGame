package com.example.numberfindinggame.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class FirebaseManager {
    private static final FirebaseDatabase database =
            FirebaseDatabase.getInstance(
                    "https://numberfindinggame-default-rtdb.asia-southeast1.firebasedatabase.app"
            );

    public static DatabaseReference getNguoiDungRef() {
        return database.getReference("NguoiDung");
    }
}
