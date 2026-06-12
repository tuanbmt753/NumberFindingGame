package com.example.numberfindinggame.activity.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.auth.DangKyActivity;
import com.example.numberfindinggame.activity.auth.DangNhapActivity;
import com.example.numberfindinggame.activity.auth.XacThucEmailActivity;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.adapter.ThietBiDangNhapAdapter;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ListView lvThietBiDangNhap;
    private ArrayList<ThietBiDangNhap> dsThietBiDangNhap = new ArrayList<>();
    private ThietBiDangNhapAdapter thietBiDangNhapAdapter;

    private ValueEventListener dsThietBiListener;

    private ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();

    private TextView txtQuayLai;

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

        if (getIntent().hasExtra(IntentKey.ACTIVITY_TYPE)) {
            String activityType = getIntent().getStringExtra(IntentKey.ACTIVITY_TYPE);
            if (activityType.equals(ActivityType.DANG_XUAT_TU_XA)) {
                String text = getIntent().getStringExtra(IntentKey.TEXT);
                MessageHelper.success(SettingActivity.this, "" + text);

                new ConfirmDialog(
                        SettingActivity.this,
                        "Xác nhận",
                        "✅ "+text+" .⚠️ Hành động này chỉ có tác dụng ở màn hình Setting, khi thoát ứng dụng bạn sẽ cần phải xác thực lại để có thể đăng xuất, và xóa thiết bị! ",
                        new ConfirmDialog.ConfirmCallback() {

                            @Override
                            public void onYes() {

                            }

                            @Override
                            public void onNo() {

                            }
                        }
                ).show();
            }
        }


        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        SettingActivity.this,
                        TrangChuActivity.class
                );
                startActivity(intent);
                finish();

            }
        });
    }

    private void khoiTao() {
        //luuThietBi(SessionManager.getUserId(this));
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
        txtQuayLai = findViewById(R.id.txtQuayLai);
    }

    private void luuThietBi(String maNguoiDung) {
        //String maThietBi = DeviceHelper.getDeviceId(SettingActivity.this);
        String maThietBi = "333333a933f8aaa";

        thietBiDangNhapRepository.layThietBi(
                maNguoiDung,
                maThietBi,
                new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Long ngayHienTai =
                                System.currentTimeMillis();

                        if (!snapshot.exists()) {

                            // Chưa có thiết bị
                            ThietBiDangNhap thietBi =
                                    new ThietBiDangNhap();

                            thietBi.setMaNguoiDung(maNguoiDung);
                            thietBi.setMaThietBi(maThietBi);
                            thietBi.setTenThietBi(
                                    "SamSung A12F5010"
                            );
                            thietBi.setNgayTao(ngayHienTai);
                            thietBi.setNgayCapNhatCuoi(ngayHienTai);
                            thietBi.setDangHoatDong(true);

                            thietBiDangNhapRepository.luuThietBiDangNhap(
                                    thietBi,
                                    task -> {

                                        if (task.isSuccessful()) {
                                            Log.d(
                                                    "THIET_BI",
                                                    "Lưu thành công"
                                            );
                                        }

                                    }
                            );


                        } else {

                            // Đã có thiết bị
                            thietBiDangNhapRepository.capNhatLanDangNhapCuoi(
                                    maNguoiDung,
                                    maThietBi
                            );


                            Log.d(
                                    "THIETBI",
                                    "Đã cập nhật lần đăng nhập cuối"
                            );


                        }

                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error
                    ) {

                    }

                }
        );
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