package com.example.numberfindinggame.repository;

public interface FirebaseCallback {
    void onSuccess();

    void onFailure(String error);
}