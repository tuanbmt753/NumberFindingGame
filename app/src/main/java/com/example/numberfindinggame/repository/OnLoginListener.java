package com.example.numberfindinggame.repository;

import com.example.numberfindinggame.model.NguoiDung;

public interface OnLoginListener {
    void onSuccess(NguoiDung nguoiDung);

    void onFailed();
}
