package com.example.numberfindinggame.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
}
