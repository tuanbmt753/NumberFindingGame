package com.example.numberfindinggame.activity.BanBe;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.activity.nguoidung.ThongTinNguoiDungActivity;
import com.example.numberfindinggame.adapter.BieuCamAdapter;
import com.example.numberfindinggame.adapter.ManMotAdapter;
import com.example.numberfindinggame.adapter.TinNhanAdapter;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.constant.LoaiTinNhan;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.helper.HinhAnhHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.NetworkHelper;
import com.example.numberfindinggame.manager.SoundManager;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.model.TimSo;
import com.example.numberfindinggame.model.TinNhan;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.repository.TinNhanRepository;
import com.example.numberfindinggame.session.SessionManager;
import com.example.numberfindinggame.utils.LoadingDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TinNhanActivity extends AppCompatActivity {
    private TextView txtTenNguoiDung;
    private EditText edtTinNhan;
    private ImageView imgQuayLai;
    private MaterialCardView cardGuiTinNhan;
    private RecyclerView recyclerViewTinNhan;
    private TinNhanAdapter tinNhanAdapter;
    private List<TinNhan> tinNhanList = new ArrayList<>();
    private String maNguoiDung;
    private Context context;
    private String maPhongChat;
    private String maBanBe;

    private NguoiDungRepository nguoiDungRepository;
    private TinNhanRepository tinNhanRepository;

    private Query tinNhanQuery;
    private ValueEventListener tinNhanValueListener;

    private MaterialCardView cardHinhAnh, cardBieuCam, cardQuayLai;
    private ActivityResultLauncher<String> pickImageLauncher;
    private Uri imageUri;
    private byte[] byteArrayHinh = new byte[0];


    private RecyclerView recyclerViewBieuCam;
    private BieuCamAdapter bieuCamAdapter;
    private List<Integer> danhSachBieuCam;

    private LinearLayout linearLayoutTinNhan, linearLayoutQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tin_nhan);

        setControl();
        setEvent();

    }

    private void setEvent() {
        context = TinNhanActivity.this;
        nguoiDungRepository = new NguoiDungRepository();
        tinNhanRepository = new TinNhanRepository();
        maNguoiDung = SessionManager.getUserId(context);

        if (getIntent().hasExtra(IntentKey.MA_PHONG_CHAT)) {
            maPhongChat = getIntent().getStringExtra(IntentKey.MA_PHONG_CHAT);

            if (getIntent().hasExtra(IntentKey.MA_BAN_BE)) {
                maBanBe = getIntent().getStringExtra(IntentKey.MA_BAN_BE);
                layThongTinNguoiDung(maBanBe);
                //MessageHelper.success((Activity) context, maNguoiDung);
            }

        }

        recyclerViewBieuCam.setVisibility(View.GONE);

        khoiTao();
        khoiTaoRecyclerViewBieuCam();
        dieuChinhChieuCaoRecyclerView();

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                imageUri = uri;

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    bitmap = HinhAnhHelper.decodeSampledBitmapFromUri(this, uri, 512, 512);
                    Bitmap resizedBitmap = HinhAnhHelper.resizeBitmap(bitmap, 512);

                    byteArrayHinh = HinhAnhHelper.getBytesFromUri(this, uri);

                    // Nếu > 1MB
                    if (byteArrayHinh.length > 1024 * 1024) {
                        byteArrayHinh = HinhAnhHelper.compressToMaxSize(resizedBitmap, 1);
                    }
                    themTinNhan(HinhAnhHelper.chuyenByteSangChuoi(byteArrayHinh));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        cardHinhAnh.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });


        imgQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(TinNhanActivity.this);

                Intent intent = new Intent(TinNhanActivity.this, BanBeActivity.class);

                startActivity(intent);
                finish();
            }
        });

        linearLayoutQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(TinNhanActivity.this);

                Intent intent = new Intent(TinNhanActivity.this, BanBeActivity.class);

                startActivity(intent);
                finish();
            }
        });

        cardQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundManager.playButton(TinNhanActivity.this);

                Intent intent = new Intent(TinNhanActivity.this, BanBeActivity.class);

                startActivity(intent);
                finish();
            }
        });

        cardGuiTinNhan.setVisibility(View.GONE);

        cardGuiTinNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themTinNhan();
            }
        });

        edtTinNhan.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtTinNhan.getText().length() > 0) {
                    cardGuiTinNhan.setVisibility(View.VISIBLE);
                } else {
                    cardGuiTinNhan.setVisibility(View.GONE);
                }
            }
        });

        cardBieuCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewBieuCam.setVisibility(View.VISIBLE);
            }
        });


        edtTinNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewBieuCam.setVisibility(View.GONE);
            }
        });

        linearLayoutTinNhan.setOnClickListener(v -> {

            if (recyclerViewBieuCam.getVisibility() == View.VISIBLE) {

                recyclerViewBieuCam.setVisibility(View.GONE);

            }

        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (recyclerViewBieuCam != null
                    && recyclerViewBieuCam.getVisibility() == View.VISIBLE
                    && recyclerViewTinNhan != null) {

                int[] viTri = new int[2];

                recyclerViewTinNhan.getLocationOnScreen(viTri);

                float x = event.getRawX();
                float y = event.getRawY();

                boolean chamTrongRecyclerView =
                        x >= viTri[0]
                                && x <= viTri[0] + recyclerViewTinNhan.getWidth()
                                && y >= viTri[1]
                                && y <= viTri[1] + recyclerViewTinNhan.getHeight();

                if (chamTrongRecyclerView) {

                    recyclerViewBieuCam.setVisibility(View.GONE);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    private void dieuChinhChieuCaoRecyclerView() {

        recyclerViewBieuCam.post(() -> {

            // Lấy chiều cao màn hình
            int chieuCaoManHinh = getResources()
                    .getDisplayMetrics().heightPixels;

            // Chiều cao RecyclerView bằng 1/2 màn hình
            int chieuCaoRecyclerView = chieuCaoManHinh / 2;

            // Cập nhật chiều cao
            ViewGroup.LayoutParams params =
                    recyclerViewBieuCam.getLayoutParams();

            params.height = chieuCaoRecyclerView;

            recyclerViewBieuCam.setLayoutParams(params);
        });
    }

    private void langNgheDanhSachTinNhan() {
        if (maPhongChat == null || maPhongChat.trim().isEmpty()) {
            return;
        }

        ngungLangNgheTinNhan();

        // Tránh đăng ký trùng listener
        if (tinNhanValueListener != null) {
            return;
        }

        tinNhanQuery = FirebaseManager.tinNhan().orderByChild("maPhongChat").equalTo(maPhongChat);

        tinNhanValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                tinNhanList.clear();

                for (com.google.firebase.database.DataSnapshot item : snapshot.getChildren()) {

                    TinNhan tinNhan = item.getValue(TinNhan.class);

                    if (tinNhan != null) {
                        tinNhan.setMaTinNhan(item.getKey());
                        tinNhanList.add(tinNhan);

                        tinNhanRepository.xoaTinNhan(
                                tinNhanList,
                                new TinNhanRepository.OnTinNhanListener() {

                                    @Override
                                    public void onSuccess(String maTinNhan) {
                                        // Xóa thành công
                                    }

                                    @Override
                                    public void onFailure(String loi) {
                                        Log.e("XOA_TIN_NHAN", loi);
                                    }
                                }
                        );
                    }
                }

                java.util.Collections.sort(tinNhanList, java.util.Comparator.comparingLong(TinNhan::getNgayTao));

                tinNhanAdapter.notifyDataSetChanged();

                if (!tinNhanList.isEmpty()) {
                    recyclerViewTinNhan.scrollToPosition(tinNhanList.size() - 1);
                }
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                MessageHelper.error(TinNhanActivity.this, error.getMessage());
            }
        };

        tinNhanQuery.addValueEventListener(tinNhanValueListener);
    }

    private void ngungLangNgheTinNhan() {
        if (tinNhanQuery != null && tinNhanValueListener != null) {
            tinNhanQuery.removeEventListener(tinNhanValueListener);
        }

        // Cho phép đăng ký lại khi Activity được mở lại
        tinNhanValueListener = null;
        tinNhanQuery = null;
    }

    private void themTinNhan() {
        tinNhanRepository.themTinNhan(maNguoiDung, maPhongChat, edtTinNhan.getText().toString().trim(), LoaiTinNhan.TEXT, new TinNhanRepository.OnTinNhanListener() {

            @Override
            public void onSuccess(String maTinNhan) {
                // Thêm tin nhắn thành công
                // Có thể cập nhật giao diện tại đây
                //MessageHelper.success((Activity) context, "Thêm tin nhắn thành công!");
                edtTinNhan.setText("");
            }

            @Override
            public void onFailure(String loi) {
                // Xử lý lỗi
                MessageHelper.error((Activity) context, loi);
            }
        });
    }

    private void themTinNhan(String imgHinhAnh) {
        tinNhanRepository.themTinNhan(maNguoiDung, maPhongChat, imgHinhAnh, LoaiTinNhan.IMAGE, new TinNhanRepository.OnTinNhanListener() {

            @Override
            public void onSuccess(String maTinNhan) {
                // Thêm tin nhắn thành công
                // Có thể cập nhật giao diện tại đây
                //MessageHelper.success((Activity) context, "Thêm tin nhắn thành công!");
                edtTinNhan.setText("");
            }

            @Override
            public void onFailure(String loi) {
                // Xử lý lỗi
                MessageHelper.error((Activity) context, loi);
            }
        });
    }


    private void khoiTao() {
        tinNhanAdapter = new TinNhanAdapter(this, tinNhanList);
        tinNhanAdapter.setRecyclerViewBieuCam(recyclerViewBieuCam);

        recyclerViewTinNhan.setLayoutManager(new GridLayoutManager(this, 1));

        recyclerViewTinNhan.setAdapter(tinNhanAdapter);


        //langNgheDanhSachTinNhan();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Khi quay lại ứng dụng, tiếp tục nhận tin nhắn realtime
        langNgheDanhSachTinNhan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Khi rời khỏi màn hình, ngừng lắng nghe Firebase
        ngungLangNgheTinNhan();
    }

    private void khoiTaoRecyclerViewBieuCam() {

        danhSachBieuCam = new ArrayList<>();

        // Duyệt từ stitch_1 đến stitch_40
        for (int i = 1; i <= 40; i++) {

            String tenHinhAnh = "stitch_" + i;

            int resourceId = getResources()
                    .getIdentifier(
                            tenHinhAnh,
                            "drawable",
                            getPackageName()
                    );

            // Chỉ thêm hình ảnh tồn tại
            if (resourceId != 0) {
                danhSachBieuCam.add(resourceId);
            }
        }

        // Hiển thị 4 cột
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 4);

        recyclerViewBieuCam.setLayoutManager(
                gridLayoutManager
        );

        bieuCamAdapter = new BieuCamAdapter(
                this,
                danhSachBieuCam
        );
        bieuCamAdapter.setMaPhongChat(maPhongChat);
        bieuCamAdapter.setRecyclerViewBieuCam(recyclerViewBieuCam);


        recyclerViewBieuCam.setAdapter(
                bieuCamAdapter
        );

        // Tối ưu khi kích thước RecyclerView không thay đổi
        recyclerViewBieuCam.setHasFixedSize(true);
    }

    private void setControl() {
        txtTenNguoiDung = findViewById(R.id.txtTenNguoiDung);
        edtTinNhan = findViewById(R.id.edtTinNhan);

        imgQuayLai = findViewById(R.id.imgQuayLai);
        cardGuiTinNhan = findViewById(R.id.cardGuiTinNhan);

        recyclerViewTinNhan = findViewById(R.id.recyclerViewTinNhan);
        cardHinhAnh = findViewById(R.id.cardHinhAnh);
        cardBieuCam = findViewById(R.id.cardBieuCam);
        cardQuayLai = findViewById(R.id.cardQuayLai);

        recyclerViewBieuCam = findViewById(R.id.recyclerViewBieuCam);

        linearLayoutTinNhan = findViewById(R.id.linearLayoutTinNhan);
        linearLayoutQuayLai = findViewById(R.id.linearLayoutQuayLai);


    }

    private void layThongTinNguoiDung(String maNguoiDung) {
        if (!NetworkHelper.isConnected(context)) {

            MessageHelper.error((Activity) context, "Không có kết nối Internet");

            return;
        }

        LoadingDialog loading = new LoadingDialog(context);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(maNguoiDung, task -> {

            if (task.isSuccessful() && task.getResult().exists()) {
                NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);

                txtTenNguoiDung.setText(nguoiDung.getTenNguoiDung());
                loading.dismiss();

            }
        });
    }
}