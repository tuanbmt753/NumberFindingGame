package com.example.numberfindinggame.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MusicFileHelper {

    private static final String PREFIX = "";

    private static final String FOLDER_NAME =
            "NumberFindingGame";


    // Lưu nhạc
    public static boolean saveMusic(
            Context context,
            Uri uri
    ) {

        try {

            String fileName =
                    getFileName(
                            context,
                            uri
                    );

            if (fileName == null) {

                return false;

            }

            // Chỉ nhận mp3
            if (!fileName
                    .toLowerCase()
                    .endsWith(".mp3")) {

                return false;

            }


            String nameWithoutExt =
                    fileName.substring(
                            0,
                            fileName.lastIndexOf(".")
                    );


            nameWithoutExt =
                    nameWithoutExt
                            .trim()
                            .replace(" ", "_");


            String newName = nameWithoutExt
                    + ".mp3";


            File folder =
                    getMusicFolder(
                            context
                    );


            if (!folder.exists()) {

                folder.mkdirs();

            }


            File destFile =
                    new File(
                            folder,
                            newName
                    );


            InputStream is =
                    context
                            .getContentResolver()
                            .openInputStream(uri);


            FileOutputStream fos =
                    new FileOutputStream(
                            destFile
                    );


            byte[] buffer =
                    new byte[4096];


            int len;


            while ((len = is.read(buffer)) != -1) {

                fos.write(
                        buffer,
                        0,
                        len
                );

            }


            fos.flush();

            fos.close();

            is.close();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;

        }

    }


    // Lấy tên file
    public static String getFileName(
            Context context,
            Uri uri
    ) {

        String result = null;


        Cursor cursor =
                context
                        .getContentResolver()
                        .query(
                                uri,
                                null,
                                null,
                                null,
                                null
                        );


        if (cursor != null) {

            int index =
                    cursor.getColumnIndex(
                            OpenableColumns.DISPLAY_NAME
                    );


            if (cursor.moveToFirst()) {

                result =
                        cursor.getString(
                                index
                        );

            }

            cursor.close();

        }


        return result;

    }


    // Lấy thư mục nhạc
    public static File getMusicFolder(
            Context context
    ) {

        return new File(

                context.getExternalFilesDir(

                        Environment.DIRECTORY_MUSIC

                ),

                FOLDER_NAME

        );

    }


    // Lấy tất cả nhạc nền
    public static List<File> getAllMusic(
            Context context
    ) {

        List<File> list =
                new ArrayList<>();


        File folder =
                getMusicFolder(
                        context
                );


        if (!folder.exists()) {

            return list;

        }


        File[] files =
                folder.listFiles();


        if (files == null) {

            return list;

        }


        for (File file : files) {

            String name =
                    file.getName();


            if (
                    name.endsWith(
                            ".mp3"
                    )

            ) {

                list.add(
                        file
                );

            }

        }


        return list;

    }


    // Lấy đường dẫn thư mục
    public static String getFolderPath(
            Context context
    ) {

        return getMusicFolder(
                context
        ).getAbsolutePath();

    }

}