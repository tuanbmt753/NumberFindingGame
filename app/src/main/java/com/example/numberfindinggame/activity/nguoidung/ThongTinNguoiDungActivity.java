package com.example.numberfindinggame.activity.nguoidung;

import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangBitMap;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenByteSangChuoi;
import static com.example.numberfindinggame.helper.HinhAnhHelper.chuyenStringSangByte;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.activity.home.TrangChuActivity;
import com.example.numberfindinggame.constant.IntentKey;
import com.example.numberfindinggame.dialog.ConfirmDialog;
import com.example.numberfindinggame.helper.DeviceHelper;
import com.example.numberfindinggame.helper.MessageHelper;
import com.example.numberfindinggame.helper.SessionManager;
import com.example.numberfindinggame.helper.SoundManager;
import com.example.numberfindinggame.helper.ThietBiDangNhapHelper;
import com.example.numberfindinggame.model.NguoiDung;
import com.example.numberfindinggame.repository.NguoiDungRepository;
import com.example.numberfindinggame.utils.DateUtils;
import com.example.numberfindinggame.utils.LoadingDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThongTinNguoiDungActivity extends AppCompatActivity {

    private TextView txtQuayLai, txtTenNguoiDung, txtMaNguoiDung, txtEmail, txtUsername, txtNgayTao;
    private NguoiDungRepository nguoiDungRepository = new NguoiDungRepository();

    private ImageView imgAvatar, imgHinhNen;
    private byte[] byteArrayHinh = new byte[0];
    private String maNguoiDung;
    private String themAnh = IntentKey.ANH_DAI_DIEN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_nguoi_dung);

        setControl();
        setEvent();

    }

    private void setEvent() {
        maNguoiDung = SessionManager.getUserId(ThongTinNguoiDungActivity.this);
        ThietBiDangNhapHelper.kiemTraThietBiDangNhap(
                this,
                SessionManager.getUserId(this),
                DeviceHelper.getDeviceId(ThongTinNguoiDungActivity.this)

        );
        layThongTinNguoiDung();

        txtQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ThongTinNguoiDungActivity.this,
                        TrangChuActivity.class
                );
                SoundManager.playButton(ThongTinNguoiDungActivity.this);
                startActivity(intent);
                finish();

            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themAnh = IntentKey.ANH_DAI_DIEN;
                SoundManager.playButton(ThongTinNguoiDungActivity.this);

                //Mở thư viên
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);


            }
        });

        imgHinhNen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themAnh = IntentKey.ANH_NEN;
                SoundManager.playButton(ThongTinNguoiDungActivity.this);

                //Mở thư viên
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);


            }
        });

    }

    private void layThongTinNguoiDung() {
        LoadingDialog loading =
                new LoadingDialog(ThongTinNguoiDungActivity.this);
        loading.setMessage("Đang lấy thông tin người dùng...");
        loading.show();

        nguoiDungRepository.layNguoiDungTheoMa(
                SessionManager.getUserId(ThongTinNguoiDungActivity.this),
                task -> {

                    if (task.isSuccessful() && task.getResult().exists()) {
                        NguoiDung nguoiDung = task.getResult().getValue(NguoiDung.class);

                        txtTenNguoiDung
                                .setText("" + nguoiDung.getTenNguoiDung());
                        txtMaNguoiDung
                                .setText("\uD83C\uDD94 ID: " + nguoiDung.getMaNguoiDung());
                        txtEmail
                                .setText("✉\uFE0F Email: " + nguoiDung.getEmail());
                        txtUsername
                                .setText("\uD83D\uDC64 Username: " + nguoiDung.getTenNguoiDung());
                        txtNgayTao
                                .setText("\uD83D\uDCC5 Ngày tạo: " + DateUtils.format(nguoiDung.getNgayTao()));

                        try {

                            if (!nguoiDung.getHinhDaiDien().isEmpty()) {
                                byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhDaiDien());
                                imgAvatar.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
                            } else {
                                imgAvatar.setImageResource(R.drawable.avatar_default);
                            }

                        } catch (Exception exception) {
                            imgAvatar.setImageResource(R.drawable.avatar_default);
                        }

                        try {

                            if (!nguoiDung.getHinhNen().isEmpty()) {
                                byteArrayHinh = chuyenStringSangByte(nguoiDung.getHinhNen());
                                imgHinhNen.setImageBitmap(chuyenByteSangBitMap(byteArrayHinh));
                            } else {
                                imgHinhNen.setImageResource(R.drawable.background_profile);
                            }

                        } catch (Exception exception) {
                            imgHinhNen.setImageResource(R.drawable.background_profile);
                        }

                        loading.dismiss();


                    }
                }
        );
    }

    private void setControl() {
        txtQuayLai = findViewById(R.id.txtQuayLai);
        txtTenNguoiDung = findViewById(R.id.txtTenNguoiDung);
        txtMaNguoiDung = findViewById(R.id.txtMaNguoiDung);
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtNgayTao = findViewById(R.id.txtNgayTao);

        imgAvatar = findViewById(R.id.imgAvatar);
        imgHinhNen = findViewById(R.id.imgHinhNen);

    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data);

        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null) {

            Uri uri = data.getData();

            try {

                // Đọc ảnh an toàn
                Bitmap bitmap =
                        decodeSampledBitmapFromUri(
                                uri,
                                512,
                                512
                        );

                // Resize giữ tỉ lệ
                Bitmap resizedBitmap =
                        resizeBitmap(bitmap);

                // Hiển thị avatar
                if (themAnh.equals(IntentKey.ANH_DAI_DIEN)) {
                    imgAvatar.setImageBitmap(
                            resizedBitmap);
                }
                if (themAnh.equals(IntentKey.ANH_NEN)) {
                    imgHinhNen.setImageBitmap(
                            resizedBitmap);
                }

                // Chuyển thành byte[]
                byteArrayHinh =
                        compressTo1MB(
                                resizedBitmap
                        );

                Log.d(
                        "Avatar",
                        "Size = "
                                + byteArrayHinh.length / 1024
                                + " KB"
                );

                if (themAnh.equals(IntentKey.ANH_DAI_DIEN)) {
                    capNhapAnhDaiDien(chuyenByteSangChuoi(byteArrayHinh));
                }
                if (themAnh.equals(IntentKey.ANH_NEN)) {
                    capNhapHinhNen(chuyenByteSangChuoi(byteArrayHinh));
                }


            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    private void capNhapHinhNen(String hinhNen) {
        new ConfirmDialog(
                ThongTinNguoiDungActivity.this,
                "Xác nhận",
                "⚠️ Bạn có muốn lấy ảnh này làm ảnh nền? ",
                new ConfirmDialog.ConfirmCallback() {

                    @Override
                    public void onYes() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);

                        nguoiDungRepository.capNhatHinhNen(

                                maNguoiDung,
                                hinhNen,

                                new NguoiDungRepository.OnUpdateListener() {

                                    @Override
                                    public void onSuccess() {

                                        MessageHelper.success(ThongTinNguoiDungActivity.this, "Cập nhật thành công");

                                    }

                                    @Override
                                    public void onFailed(String message) {

                                        MessageHelper.error(ThongTinNguoiDungActivity.this, "" + message);

                                    }

                                }

                        );
                    }

                    @Override
                    public void onNo() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);
                        layThongTinNguoiDung();
                    }
                }
        ).show();
    }

    private Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight)
            throws IOException {

        InputStream input1 = getContentResolver().openInputStream(uri);

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(input1, null, options);

        input1.close();


        options.inSampleSize =
                calculateInSampleSize(
                        options,
                        reqWidth,
                        reqHeight
                );

        options.inJustDecodeBounds = false;

        InputStream input2 =
                getContentResolver().openInputStream(uri);

        Bitmap bitmap =
                BitmapFactory.decodeStream(
                        input2,
                        null,
                        options
                );

        input2.close();

        return bitmap;

    }

    private int calculateInSampleSize(
            BitmapFactory.Options options,
            int reqWidth,
            int reqHeight) {

        int height = options.outHeight;

        int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            int halfHeight = height / 2;

            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {

                inSampleSize *= 2;

            }

        }

        return inSampleSize;

    }


    //giữ tỉ lệ ảnh , cách này để ảnh không bị méo.
    private Bitmap resizeBitmap(Bitmap bitmap) {

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();

        float ratio = Math.min(
                512f / width,
                512f / height
        );

        int newWidth = Math.round(width * ratio);

        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(
                bitmap,
                newWidth,
                newHeight,
                true
        );

    }

    private byte[] compressTo1MB(Bitmap bitmap) {

        ByteArrayOutputStream stream =
                new ByteArrayOutputStream();

        int quality = 100;

        bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                stream
        );

        while (stream.size() > 1024 * 1024
                && quality > 10) {

            stream.reset();

            quality -= 5;

            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    quality,
                    stream
            );

        }

        return stream.toByteArray();

    }

    private void capNhapAnhDaiDien(String maHoaHinhAnh) {

        new ConfirmDialog(
                ThongTinNguoiDungActivity.this,
                "Xác nhận",
                "⚠️ Bạn có muốn lấy ảnh này làm ảnh đại diện? ",
                new ConfirmDialog.ConfirmCallback() {

                    @Override
                    public void onYes() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);

                        nguoiDungRepository.capNhatHinhDaiDien(

                                maNguoiDung,
                                maHoaHinhAnh,

                                new NguoiDungRepository.OnUpdateListener() {

                                    @Override
                                    public void onSuccess() {

                                        MessageHelper.success(ThongTinNguoiDungActivity.this, "Cập nhật thành công");

                                    }

                                    @Override
                                    public void onFailed(String message) {

                                        MessageHelper.error(ThongTinNguoiDungActivity.this, "" + message);

                                    }

                                }

                        );
                    }

                    @Override
                    public void onNo() {
                        SoundManager.playButton(ThongTinNguoiDungActivity.this);
                        layThongTinNguoiDung();
                    }
                }
        ).show();
    }
}