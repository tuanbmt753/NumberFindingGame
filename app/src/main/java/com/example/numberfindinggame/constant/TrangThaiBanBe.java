package com.example.numberfindinggame.constant;

public enum TrangThaiBanBe {

    TU_CHOI("TuChoi"),
    DONG_Y("DongY"),
    LOI_MOI_KET_BAN("LoiMoiKetBan");

    private final String giaTri;

    TrangThaiBanBe(String giaTri) {
        this.giaTri = giaTri;
    }

    public String getGiaTri() {
        return giaTri;
    }
}