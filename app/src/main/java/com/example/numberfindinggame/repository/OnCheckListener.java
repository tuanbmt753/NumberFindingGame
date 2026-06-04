package com.example.numberfindinggame.repository;

public interface OnCheckListener {

    void onUsernameExists();

    void onEmailExists();

    void onPhoneExists();

    void onSuccess();
}