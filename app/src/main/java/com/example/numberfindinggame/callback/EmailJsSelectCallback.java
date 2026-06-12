package com.example.numberfindinggame.repository;

import com.example.numberfindinggame.model.Emailjs;

public interface EmailJsSelectCallback {
    void onSuccess(Emailjs emailjs, String key);
    void onFailure(String message);
}