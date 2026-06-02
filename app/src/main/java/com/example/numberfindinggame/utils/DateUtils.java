package com.example.numberfindinggame.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String format(long timestamp) {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm:ss",
                        Locale.getDefault());

        return sdf.format(
                new Date(timestamp));
    }
}
