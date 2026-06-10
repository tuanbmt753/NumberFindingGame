package com.example.numberfindinggame.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.adapter.ThietBiDangNhapAdapter;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ListView lvThietBiDangNhap;
    private ArrayList<ThietBiDangNhap> dsThietBiDangNhap = new ArrayList<>();
    private ThietBiDangNhapAdapter thietBiDangNhapAdapter;

    private ValueEventListener dsThietBiListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        setControl();
        setEvent();
    }

    private void setEvent() {
        thietBiDangNhapAdapter = new ThietBiDangNhapAdapter(this, dsThietBiDangNhap);
        lvThietBiDangNhap.setAdapter(thietBiDangNhapAdapter);

        khoiTao();
    }

    private void khoiTao() {
        dsThietBiListener =
                ThietBiDangNhapHelper.theoDoiDanhSachThietBi(
                        SessionManager.getUserId(this),
                        danhSach -> {

                            dsThietBiDangNhap.clear();
                            dsThietBiDangNhap.addAll(danhSach);

                            thietBiDangNhapAdapter.notifyDataSetChanged();

                            ListViewHelper
                                    .setListViewHeightBasedOnChildren(
                                            lvThietBiDangNhap
                                    );
                        }
                );
    }

    private void setControl() {
        lvThietBiDangNhap = findViewById(R.id.lvThietBiDangNhap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ThietBiDangNhapHelper.stopTheoDoiDanhSachThietBi(
                SessionManager.getUserId(this),
                dsThietBiListener
        );
    }

}