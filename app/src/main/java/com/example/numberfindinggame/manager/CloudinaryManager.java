package com.example.numberfindinggame.helper;

import android.content.Context;
import android.net.Uri;

import com.google.rpc.ErrorInfo;

import java.util.Map;

public class CloudinaryManager {

    private static final String UPLOAD_PRESET =

            "android_video";



    public static String uploadVideo(

            Context context,

            Uri uri,

            OnUploadVideoListener listener

    ){

        return MediaManager

                .get()

                .upload(uri)

                .unsigned(

                        UPLOAD_PRESET

                )

                .callback(

                        new UploadCallback() {

                            @Override

                            public void onStart(

                                    String requestId

                            ) {

                            }



                            @Override

                            public void onProgress(

                                    String requestId,

                                    long bytes,

                                    long totalBytes

                            ) {

                                int progress =

                                        (int)

                                                (

                                                        bytes

                                                                *

                                                                100

                                                                /

                                                                totalBytes

                                                );



                                listener.onProgress(

                                        progress

                                );

                            }



                            @Override

                            public void onSuccess(

                                    String requestId,

                                    Map resultData

                            ) {

                                String url =

                                        resultData

                                                .get(

                                                        "secure_url"

                                                )

                                                .toString();



                                listener.onSuccess(

                                        url

                                );

                            }



                            @Override

                            public void onError(

                                    String requestId,

                                    ErrorInfo error

                            ) {

                                listener.onFailed(

                                        error

                                                .getDescription()

                                );

                            }



                            @Override

                            public void onReschedule(

                                    String requestId,

                                    ErrorInfo error

                            ) {

                            }

                        }

                )

                .dispatch();

    }

}