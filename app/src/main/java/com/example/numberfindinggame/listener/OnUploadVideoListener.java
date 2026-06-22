package com.example.numberfindinggame.listener;

public interface OnUploadVideoListener {

    void onProgress(int progress);

    void onSuccess(String videoUrl);

    void onFailed(String message);

}