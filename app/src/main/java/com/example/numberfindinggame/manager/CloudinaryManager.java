package com.example.numberfindinggame.manager;

import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.numberfindinggame.listener.OnUploadVideoListener;

import java.util.Map;

public class CloudinaryManager {

    private static final String UPLOAD_PRESET =
            "android_video";

    /**
     * Upload video lên Cloudinary
     *
     * @return requestId để có thể hủy upload
     */
    public static String uploadVideo(

            Uri uri,

            OnUploadVideoListener listener

    ) {

        return MediaManager
                .get()
                .upload(uri)
                .unsigned(UPLOAD_PRESET)
                .option("resource_type", "video")

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

                                                                * 100

                                                                / totalBytes

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

                                        error.getDescription()

                                );

                            }


                            /**
                             * Cloudinary tự retry khi mất mạng.
                             * Hàm này được gọi khi upload bị tạm hoãn
                             * và sẽ upload lại sau.
                             */
                            @Override

                            public void onReschedule(

                                    String requestId,

                                    ErrorInfo error

                            ) {

                                listener.onFailed(

                                        "Mất kết nối, đang thử lại..."

                                );

                            }

                        }

                )

                .dispatch();

    }


    /**
     * Hủy upload
     */
    public static void cancelUpload(

            String requestId

    ) {

        MediaManager

                .get()

                .cancelRequest(

                        requestId

                );

    }

}