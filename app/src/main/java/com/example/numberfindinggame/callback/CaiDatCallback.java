package com.example.numberfindinggame.callback;

import com.example.numberfindinggame.model.CaiDat;

public interface CaiDatCallback {
    void onSuccess(CaiDat caiDat);

    void onFailure(String message);
}