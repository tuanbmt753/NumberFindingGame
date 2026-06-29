package com.example.numberfindinggame.model;

public class ManChoi {
    private String maNguoiDung;
    private int manChoi;
    private String ngayCapNhat;
    private String ngayTao;

    @Override
    public String toString() {
        return "ManChoi{" +
                "maNguoiDung='" + maNguoiDung + '\'' +
                ", manChoi=" + manChoi +
                ", ngayCapNhat='" + ngayCapNhat + '\'' +
                ", ngayTao='" + ngayTao + '\'' +
                '}';
    }

    public ManChoi() {
    }

    public ManChoi(String maNguoiDung, int manChoi, String ngayCapNhat, String ngayTao) {
        this.maNguoiDung = maNguoiDung;
        this.manChoi = manChoi;
        this.ngayCapNhat = ngayCapNhat;
        this.ngayTao = ngayTao;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public int getManChoi() {
        return manChoi;
    }

    public String getNgayCapNhat() {
        return ngayCapNhat;
    }

    public String getNgayTao() {
        return ngayTao;
    }
}
