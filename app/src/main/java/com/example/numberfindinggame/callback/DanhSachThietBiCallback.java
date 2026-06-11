package com.example.numberfindinggame.callback;

import com.example.numberfindinggame.model.ThietBiDangNhap;

import java.util.List;

public interface DanhSachThietBiCallback {
    void onResult(List<ThietBiDangNhap> danhSach);
}