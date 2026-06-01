package com.example.numberfindinggame;

import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Test Firebase Realtime Database
        FirebaseDatabase firebaseDatabase =
                FirebaseDatabase.getInstance(
                        "https://numberfindinggame-default-rtdb.asia-southeast1.firebasedatabase.app"
                );

        DatabaseReference database = firebaseDatabase.getReference();

        database.child("Test")
                .child("Message")
                .setValue("Xin chao Firebase")
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this,
                            "Ghi dữ liệu thành công",
                            Toast.LENGTH_SHORT).show();

                    Log.d("Firebase", "Ghi thành công");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Lỗi: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    Log.e("Firebase", e.getMessage());
                });
    }
}