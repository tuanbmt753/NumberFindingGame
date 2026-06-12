package com.example.numberfindinggame.repository;

import androidx.annotation.NonNull;

import com.example.numberfindinggame.callback.EmailCallback;
import com.example.numberfindinggame.callback.EmailJsSelectCallback;
import com.example.numberfindinggame.callback.FirebaseCallback;
import com.example.numberfindinggame.firebase.FirebaseManager;
import com.example.numberfindinggame.model.Emailjs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EmailjsRepository {

    private static final String EMAILJS_URL =
            "https://api.emailjs.com/api/v1.0/email/send";

    private final OkHttpClient client =
            new OkHttpClient();


    public void themEmailJs(
            String serviceID,
            String templateID,
            String publicKey,
            String email,
            FirebaseCallback callback
    ) {

        FirebaseManager.Emailjs()
                .orderByChild("email")
                .equalTo(email)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                if (snapshot.exists()) {
                                    callback.onFailure(
                                            "Email đã tồn tại"
                                    );
                                    return;
                                }

                                String key =
                                        FirebaseManager.Emailjs()
                                                .push()
                                                .getKey();

                                Emailjs emailjs =
                                        new Emailjs(
                                                serviceID,
                                                templateID,
                                                publicKey,
                                                email,
                                                System.currentTimeMillis()
                                        );

                                emailjs.setSuDung(System.currentTimeMillis());

                                FirebaseManager.Emailjs()
                                        .child(key)
                                        .setValue(emailjs)
                                        .addOnSuccessListener(
                                                unused ->
                                                        callback.onSuccess()
                                        )
                                        .addOnFailureListener(
                                                e ->
                                                        callback.onFailure(
                                                                e.getMessage()
                                                        )
                                        );
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                callback.onFailure(
                                        error.getMessage()
                                );
                            }
                        });
    }

    public void sendOTP(
            String emailNguoiNhan,
            String otp,
            String service_id,
            String template_id,
            String user_id,
            EmailCallback callback
    ) {

        try {

            JSONObject templateParams =
                    new JSONObject();

            templateParams.put(
                    "to_email",
                    emailNguoiNhan
            );

            templateParams.put(
                    "otp",
                    otp
            );

            JSONObject body =
                    new JSONObject();

            // Thay bằng thông tin thật của bạn
            body.put(
                    "service_id",
                    service_id
            );

            body.put(
                    "template_id",
                    template_id
            );

            body.put(
                    "user_id",
                    user_id
            );

            body.put(
                    "template_params",
                    templateParams
            );

            android.util.Log.d(
                    "EMAILJS",
                    "REQUEST = " + body.toString()
            );

            RequestBody requestBody =
                    RequestBody.create(
                            body.toString(),
                            MediaType.parse(
                                    "application/json"
                            )
                    );

            Request request =
                    new Request.Builder()
                            .url(
                                    "https://api.emailjs.com/api/v1.0/email/send"
                            )
                            .post(requestBody)
                            .build();

            client.newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(
                                @NonNull Call call,
                                @NonNull IOException e
                        ) {

                            android.util.Log.e(
                                    "EMAILJS",
                                    e.getMessage(),
                                    e
                            );

                            callback.onFailure(
                                    e.getMessage()
                            );
                        }

                        @Override
                        public void onResponse(
                                @NonNull Call call,
                                @NonNull Response response
                        ) throws IOException {

                            String result =
                                    response.body() != null
                                            ? response.body().string()
                                            : "";

                            android.util.Log.d(
                                    "EMAILJS",
                                    "CODE = "
                                            + response.code()
                            );

                            android.util.Log.d(
                                    "EMAILJS",
                                    "BODY = "
                                            + result
                            );

                            if (response.isSuccessful()) {

                                callback.onSuccess();

                            } else {

                                callback.onFailure(
                                        "Code: "
                                                + response.code()
                                                + " - "
                                                + result
                                );
                            }

                            response.close();
                        }
                    });

        } catch (Exception e) {

            callback.onFailure(
                    e.getMessage()
            );
        }
    }

    public void layEmailJsSuDungNhoNhat(
            EmailJsSelectCallback callback
    ) {

        FirebaseManager.Emailjs()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                if (!snapshot.exists()) {
                                    callback.onFailure(
                                            "Không tìm thấy EmailJS"
                                    );
                                    return;
                                }

                                Emailjs emailJsMin = null;
                                String keyMin = null;
                                long suDungMin = Long.MAX_VALUE;

                                for (DataSnapshot item : snapshot.getChildren()) {

                                    Emailjs emailjs =
                                            item.getValue(Emailjs.class);

                                    if (emailjs == null) {
                                        continue;
                                    }

                                    if (emailjs.getSuDung() < suDungMin) {

                                        suDungMin =
                                                emailjs.getSuDung();

                                        emailJsMin =
                                                emailjs;

                                        keyMin =
                                                item.getKey();
                                    }
                                }

                                if (emailJsMin != null) {

                                    callback.onSuccess(
                                            emailJsMin,
                                            keyMin
                                    );

                                } else {

                                    callback.onFailure(
                                            "Không tìm thấy EmailJS"
                                    );
                                }
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                                callback.onFailure(
                                        error.getMessage()
                                );
                            }
                        }
                );
    }


    public void capNhatSuDung(
            String key,
            FirebaseCallback callback
    ) {

        FirebaseManager.Emailjs()
                .child(key)
                .child("suDung")
                .setValue(System.currentTimeMillis())
                .addOnSuccessListener(
                        unused -> callback.onSuccess()
                )
                .addOnFailureListener(
                        e -> callback.onFailure(
                                e.getMessage()
                        )
                );
    }


}