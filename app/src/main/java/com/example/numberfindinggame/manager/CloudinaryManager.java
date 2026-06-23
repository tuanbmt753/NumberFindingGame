package com.example.numberfindinggame.manager;

import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.numberfindinggame.listener.OnUploadVideoListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloudinaryManager {

    private static final String UPLOAD_PRESET =
            "android_video";

    private static final String CLOUD_NAME =
            "dpacjldtr";

    private static final String API_KEY =
            "817952821416255";

    private static final String API_SECRET =
            "skCCWPBzKhRd8kAFx_lhqKW4CsM";

    /**
     * Upload video
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


    /**
     * Xóa video bằng URL
     */
    public static void deleteVideo(

            String videoUrl,

            OnDeleteListener listener

    ) {

        String publicId =

                getPublicIdFromUrl(

                        videoUrl

                );

        if (publicId == null) {

            listener.onFailed(

                    "Không lấy được public_id"

            );

            return;

        }

        long timestamp =

                System.currentTimeMillis()

                        / 1000;


        String dataToSign =

                "public_id="

                        + publicId

                        + "&timestamp="

                        + timestamp

                        + API_SECRET;


        String signature =

                sha1(

                        dataToSign

                );


        OkHttpClient client =

                new OkHttpClient();


        RequestBody body =

                new FormBody.Builder()

                        .add(

                                "public_id",

                                publicId

                        )

                        .add(

                                "api_key",

                                API_KEY

                        )

                        .add(

                                "timestamp",

                                String.valueOf(

                                        timestamp

                                )

                        )

                        .add(

                                "signature",

                                signature

                        )
                        .build();


        Request request =

                new Request.Builder()

                        .url(

                                "https://api.cloudinary.com/v1_1/"

                                        + CLOUD_NAME

                                        + "/video/destroy"

                        )

                        .post(

                                body

                        )

                        .build();


        client.newCall(

                        request

                )

                .enqueue(

                        new Callback() {

                            @Override

                            public void onFailure(

                                    Call call,

                                    IOException e

                            ) {

                                listener.onFailed(

                                        e.getMessage()

                                );

                            }

                            @Override

                            public void onResponse(

                                    Call call,

                                    Response response

                            )

                                    throws IOException {

                                if (

                                        response.isSuccessful()

                                ) {

                                    listener.onSuccess();

                                } else {

                                    String error =

                                            response.body()

                                                    != null

                                                    ?

                                                    response.body()

                                                            .string()

                                                    :

                                                    "Delete thất bại";


                                    listener.onFailed(

                                            error

                                    );

                                }

                            }

                        }

                );

    }


    /**
     * Lấy public_id từ URL
     */
    private static String getPublicIdFromUrl(

            String url

    ) {

        if (

                url == null

        ) {

            return null;

        }


        String keyword =

                "/upload/";


        int start =

                url.indexOf(

                        keyword

                );


        if (

                start == -1

        ) {

            return null;

        }


        String path =

                url.substring(

                        start

                                +

                                keyword.length()

                );


        if (

                path.matches(

                        "^v\\d+/.*"

                )

        ) {

            int slash =

                    path.indexOf(

                            "/"

                    );

            path =

                    path.substring(

                            slash + 1

                    );

        }


        int dot =

                path.lastIndexOf(

                        "."

                );


        if (

                dot != -1

        ) {

            path =

                    path.substring(

                            0,

                            dot

                    );

        }

        return path;

    }


    /**
     * SHA1
     */
    private static String sha1(

            String input

    ) {

        try {

            MessageDigest md =

                    MessageDigest.getInstance(

                            "SHA-1"

                    );


            byte[] bytes =

                    md.digest(

                            input.getBytes(

                                    StandardCharsets.UTF_8

                            )

                    );


            StringBuilder sb =

                    new StringBuilder();


            for (

                    byte b

                    :

                    bytes

            ) {

                sb.append(

                        String.format(

                                "%02x",

                                b

                        )

                );

            }

            return sb.toString();

        } catch (

                Exception e

        ) {

            return "";

        }

    }


    public interface OnDeleteListener {

        void onSuccess();

        void onFailed(

                String error

        );

    }

}