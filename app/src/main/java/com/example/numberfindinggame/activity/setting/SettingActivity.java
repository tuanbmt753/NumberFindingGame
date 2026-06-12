package com.example.numberfindinggame.activity.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
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
import com.example.numberfindinggame.callback.CaiDatCallback;
import com.example.numberfindinggame.constant.ActivityType;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.ListViewHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.CaiDat;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.ThietBiDangNhap;
import com.example.numberfindinggame.repository.CaiDatRepository;
import com.example.numberfindinggame.repository.ThietBiDangNhapRepository;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.graphics.Color;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ListView lvThietBiDangNhap;
    private ArrayList<ThietBiDangNhap> dsThietBiDangNhap = new ArrayList<>();
    private ThietBiDangNhapAdapter thietBiDangNhapAdapter;

    private ValueEventListener dsThietBiListener;

    private ThietBiDangNhapRepository thietBiDangNhapRepository = new ThietBiDangNhapRepository();

    private TextView txtQuayLai, txtXacThucEmailDongMo, txtMaKhoiPhucDongMo;
    private SeekBar seekBarBackground, seekBarEffect;

    private MaterialCardView cardXacThucEmailDongMo, cardMaKhoiPhucDongMo;

    private CaiDat caiDat;

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
                        "✅ " + text + " .⚠️ Hành động này chỉ có tác dụng ở màn hình Setting, khi thoát ứng dụng bạn sẽ cần phải xác thực lại để có thể đăng xuất, và xóa thiết bị! ",
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

        cardMaKhoiPhucDongMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuCaiDat(0, 1, 0, 0);
            }
        });

        seekBarBackground.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {


                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {

                        // Bắt đầu kéo
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {

                        luuCaiDat(0, 0, 1, 0);
                    }
                }
        );

        seekBarEffect.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {


                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {

                        // Bắt đầu kéo
                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {

                        luuCaiDat(0, 0, 0, 1);
                    }
                }
        );

        cardXacThucEmailDongMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luuCaiDat(1, 0, 0, 0);

            }
        });

    }

    private void luuCaiDat(int xacThuc, int maKhoiPhuc, int amThanhNen, int amThanhHieuUng) {
        CaiDatRepository.layCaiDat(
                SessionManager.getUserId(this),
                new CaiDatCallback() {
                    @Override
                    public void onSuccess(CaiDat caiDat) {
                        CaiDat caiDat2 = new CaiDat(caiDat);

                        Log.d("CAIDAT", caiDat.toString());

                        if (maKhoiPhuc > 0) {
                            if (caiDat.getMaKhoiPhuc() == true) {
                                caiDat2.setMaKhoiPhuc(false);
                            } else {
                                caiDat2.setMaKhoiPhuc(true);
                            }
                        }

                        if (xacThuc > 0) {
                            if (caiDat.getXacThucEmail() == true) {
                                caiDat2.setXacThucEmail(false);
                            } else {
                                caiDat2.setXacThucEmail(true);
                            }
                        }

                        if (amThanhNen > 0) {
                            caiDat2.setAmThanhNen(seekBarBackground.getProgress());
                        }
                        if (amThanhHieuUng > 0) {
                            caiDat2.setAmThanhHieuUng(seekBarEffect.getProgress());
                        }

                        CaiDatRepository.luuCaiDat(
                                caiDat2,
                                task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("CAIDAT", "Lưu thành công");
                                    } else {
                                        Log.d("CAIDAT", "Lưu thất bại");
                                    }
                                }
                        );

                        layCaiDat();


                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("CAIDAT", message);
                    }
                }
        );


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

        taoCaiDatMatDinh();
        layCaiDat();
    }

    private void layCaiDat() {
        CaiDatRepository.layCaiDat(
                SessionManager.getUserId(this),
                new CaiDatCallback() {
                    @Override
                    public void onSuccess(CaiDat caiDat) {
                        caiDat = new CaiDat(caiDat);

                        Log.d("CAIDAT", caiDat.toString());

                        int amThanhNen = caiDat.getAmThanhNen();
                        int amThanhHieuUng = caiDat.getAmThanhHieuUng();

                        boolean xacThucEmail = caiDat.getXacThucEmail();
                        boolean maKhoiPhuc = caiDat.getMaKhoiPhuc();

                        if (xacThucEmail == true) {
                            txtXacThucEmailDongMo.setText("✅");
                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#78C0C6"));
                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                        } else {
                            txtXacThucEmailDongMo.setText("❌");
                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        }

                        if (maKhoiPhuc == true) {
                            txtMaKhoiPhucDongMo.setText("✅");
                            //cardMaKhoiPhucDongMo.setCardBackgroundColor(Color.parseColor("#78C0C6"));
                            //cardXacThucEmailDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

                        } else {
                            txtMaKhoiPhucDongMo.setText("❌");
                            //cardMaKhoiPhucDongMo.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        }

                        seekBarBackground.setProgress(amThanhNen);
                        seekBarEffect.setProgress(amThanhHieuUng);

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e("CAIDAT", message);
                    }
                }
        );
    }

    private void taoCaiDatMatDinh() {
        CaiDatRepository.taoMacDinhNeuChuaCo(
                SessionManager.getUserId(this),
                task -> {

                    if (task.isSuccessful()) {
                        Log.d("CAIDAT", "Đã có hoặc đã tạo mới thành công");
                    } else {
                        Log.e(
                                "CAIDAT",
                                task.getException().getMessage()
                        );
                    }
                }
        );
    }

    private void setControl() {
        lvThietBiDangNhap = findViewById(R.id.lvThietBiDangNhap);
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtXacThucEmailDongMo = findViewById(R.id.txtXacThucEmailDongMo);
        txtMaKhoiPhucDongMo = findViewById(R.id.txtMaKhoiPhucDongMo);

        seekBarEffect = findViewById(R.id.seekBarEffect);
        seekBarBackground = findViewById(R.id.seekBarBackground);

        cardXacThucEmailDongMo = findViewById(R.id.cardXacThucEmailDongMo);
        cardMaKhoiPhucDongMo = findViewById(R.id.cardMaKhoiPhucDongMo);


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