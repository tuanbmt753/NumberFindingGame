package com.example.numberfindinggame.callback;

public interface FirebaseCallback {
    void onSuccess();

    void onFailure(String error);
}