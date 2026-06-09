package com.example.numberfindinggame.model;

public class ThietBiDangNhap {
    private String maThietBi, maNguoiDung, tenThietBi;
    private Boolean dangHoatDong;

    private Long ngayTao, ngayCapNhatCuoi;

    @Override
    public String toString() {
        return "ThietBiDangNhap{" +
                "maThietBi='" + maThietBi + '\'' +
                ", maNguoiDung='" + maNguoiDung + '\'' +
                ", tenThietBi='" + tenThietBi + '\'' +
                ", dangHoatDong=" + dangHoatDong +
                ", ngayTao=" + ngayTao +
                ", ngayCapNhatCuoi=" + ngayCapNhatCuoi +
                '}';
    }

    public String getMaThietBi() {
        return maThietBi;
    }

    public void setMaThietBi(String maThietBi) {
        this.maThietBi = maThietBi;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenThietBi() {
        return tenThietBi;
    }

    public void setTenThietBi(String tenThietBi) {
        this.tenThietBi = tenThietBi;
    }

    public Boolean getDangHoatDong() {
        return dangHoatDong;
    }

    public void setDangHoatDong(Boolean dangHoatDong) {
        this.dangHoatDong = dangHoatDong;
    }

    public Long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Long getNgayCapNhatCuoi() {
        return ngayCapNhatCuoi;
    }

    public void setNgayCapNhatCuoi(Long ngayCapNhatCuoi) {
        this.ngayCapNhatCuoi = ngayCapNhatCuoi;
    }

    public ThietBiDangNhap() {
    }

    public ThietBiDangNhap(String maThietBi, String maNguoiDung, String tenThietBi, Boolean dangHoatDong, Long ngayTao, Long ngayCapNhatCuoi) {
        this.maThietBi = maThietBi;
        this.maNguoiDung = maNguoiDung;
        this.tenThietBi = tenThietBi;
        this.dangHoatDong = dangHoatDong;
        this.ngayTao = ngayTao;
        this.ngayCapNhatCuoi = ngayCapNhatCuoi;
    }
}
