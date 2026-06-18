package com.example.numberfindinggame.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HinhAnhHelper {
    // chuyen Byte[] Sang Chuoi
    // chuyen Byte[] Sang Chuoi
    public static String chuyenByteSangChuoi(byte[] byteArray) {
        String base64String = android.util.Base64.encodeToString(byteArray, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
        return base64String;
    }

    //chuyen String Sang Byte[]
    public static  byte[] chuyenStringSangByte(String str) {
        byte[] byteArray = android.util.Base64.decode(str, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP | android.util.Base64.URL_SAFE);
        return byteArray;
    }

    //Chuyen byte[] sang bitMap
    public static  Bitmap chuyenByteSangBitMap(byte[] byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

    // Đọc ảnh an toàn, tránh OutOfMemory
    public static Bitmap decodeSampledBitmapFromUri(
            Context context,
            Uri uri,
            int reqWidth,
            int reqHeight)
            throws IOException {

        InputStream input1 =
                context.getContentResolver()
                        .openInputStream(uri);

        BitmapFactory.Options options =
                new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(
                input1,
                null,
                options
        );

        input1.close();


        options.inSampleSize =
                calculateInSampleSize(
                        options,
                        reqWidth,
                        reqHeight
                );

        options.inJustDecodeBounds = false;

        InputStream input2 =
                context.getContentResolver()
                        .openInputStream(uri);

        Bitmap bitmap =
                BitmapFactory.decodeStream(
                        input2,
                        null,
                        options
                );

        input2.close();

        return bitmap;

    }


    public static int calculateInSampleSize(
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


    // Resize giữ tỉ lệ
    public static Bitmap resizeBitmap(
            Bitmap bitmap,
            int maxSize) {

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();

        float ratio = Math.min(
                (float) maxSize / width,
                (float) maxSize / height
        );

        int newWidth =
                Math.round(width * ratio);

        int newHeight =
                Math.round(height * ratio);

        return Bitmap.createScaledBitmap(
                bitmap,
                newWidth,
                newHeight,
                true
        );

    }


    // Nén về <= maxSizeMB
    public static byte[] compressToMaxSize(
            Bitmap bitmap,
            int maxSizeMB) {

        ByteArrayOutputStream stream =
                new ByteArrayOutputStream();

        int maxBytes =
                maxSizeMB * 1024 * 1024;

        int quality = 100;

        bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                stream
        );

        while (stream.size() > maxBytes
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
}
