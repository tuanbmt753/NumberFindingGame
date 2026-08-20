package com.example.numberfindinggame.model;

public class ManChoi {
    private String maNguoiDung;
    private int manChoi;
    private Long ngayCapNhat;
    private Long ngayTao;

    public ManChoi() {
    }

    @Override
    public String toString() {
        return "ManChoi{" +
                "maNguoiDung='" + maNguoiDung + '\'' +
                ", manChoi=" + manChoi +
                ", ngayCapNhat=" + ngayCapNhat +
                ", ngayTao=" + ngayTao +
                '}';
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public int getManChoi() {
        return manChoi;
    }

    public void setManChoi(int manChoi) {
        this.manChoi = manChoi;
    }

    public Long getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(Long ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public ManChoi(String maNguoiDung, int manChoi, Long ngayCapNhat, Long ngayTao) {
        this.maNguoiDung = maNguoiDung;
        this.manChoi = manChoi;
        this.ngayCapNhat = ngayCapNhat;
        this.ngayTao = ngayTao;
    }

    public Long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Long ngayTao) {
        this.ngayTao = ngayTao;
    }
}
