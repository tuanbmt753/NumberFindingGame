package com.example.numberfindinggame.callback;

import com.example.numberfindinggame.model.Emailjs;

public interface EmailJsSelectCallback {
    void onSuccess(Emailjs emailjs, String key);
    void onFailure(String message);
}