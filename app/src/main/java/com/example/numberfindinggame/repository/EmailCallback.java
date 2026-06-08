package com.example.numberfindinggame.repository;

public interface EmailCallback {
    void onSuccess();
    void onFailure(String error);
}