package com.example.numberfindinggame.model;

import java.io.Serializable;

public class NguoiDung implements Serializable {
    private String maNguoiDung;
    private String tenNguoiDung;
    private String email;
    private String phone;
    private String hinhDaiDien;
    private String hinhNen;
    private String matKhau;
    private String loaiDangNhap;
    private long dangNhapCuoi;
    private long ngayCapNhat;
    private long ngayTao;

    public NguoiDung() {
    }

    public NguoiDung(String maNguoiDung,
                     String tenNguoiDung,
                     String email,
                     String phone,
                     String hinhDaiDien,
                     String hinhNen,
                     String matKhau,
                     String loaiDangNhap,
                     long dangNhapCuoi,
                     long ngayCapNhat,
                     long ngayTao) {

        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.email = email;
        this.phone = phone;
        this.hinhDaiDien = hinhDaiDien;
        this.hinhNen = hinhNen;
        this.matKhau = matKhau;
        this.loaiDangNhap = loaiDangNhap;
        this.dangNhapCuoi = dangNhapCuoi;
        this.ngayCapNhat = ngayCapNhat;
        this.ngayTao = ngayTao;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHinhDaiDien() {
        return hinhDaiDien;
    }

    public void setHinhDaiDien(String hinhDaiDien) {
        this.hinhDaiDien = hinhDaiDien;
    }

    public String getHinhNen() {
        return hinhNen;
    }

    public void setHinhNen(String hinhNen) {
        this.hinhNen = hinhNen;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getLoaiDangNhap() {
        return loaiDangNhap;
    }

    public void setLoaiDangNhap(String loaiDangNhap) {
        this.loaiDangNhap = loaiDangNhap;
    }

    public long getDangNhapCuoi() {
        return dangNhapCuoi;
    }

    public void setDangNhapCuoi(long dangNhapCuoi) {
        this.dangNhapCuoi = dangNhapCuoi;
    }

    public long getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(long ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "NguoiDung{" +
                "maNguoiDung='" + maNguoiDung + '\'' +
                ", tenNguoiDung='" + tenNguoiDung + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", hinhDaiDien='" + hinhDaiDien + '\'' +
                ", hinhNen='" + hinhNen + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", loaiDangNhap='" + loaiDangNhap + '\'' +
                ", dangNhapCuoi=" + dangNhapCuoi +
                ", ngayCapNhat=" + ngayCapNhat +
                ", ngayTao=" + ngayTao +
                '}';
    }
}
