package com.example.numberfindinggame.model;

public class CaiDat {
    private String maNguoiDung;
    private Integer amThanhNen, amThanhHieuUng;
    private Boolean xacThucEmail, maKhoiPhuc;
    private long ngayTao, ngayCapNhap;

    public CaiDat() {
    }

    @Override
    public String toString() {
        return "CaiDat{" +
                "maNguoiDung='" + maNguoiDung + '\'' +
                ", amThanhNen=" + amThanhNen +
                ", amThanhHieuUng=" + amThanhHieuUng +
                ", xacThucEmail=" + xacThucEmail +
                ", maKhoiPhuc=" + maKhoiPhuc +
                ", ngayTao=" + ngayTao +
                ", ngayCapNhap=" + ngayCapNhap +
                '}';
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public Integer getAmThanhNen() {
        return amThanhNen;
    }

    public void setAmThanhNen(Integer amThanhNen) {
        this.amThanhNen = amThanhNen;
    }

    public Integer getAmThanhHieuUng() {
        return amThanhHieuUng;
    }

    public void setAmThanhHieuUng(Integer amThanhHieuUng) {
        this.amThanhHieuUng = amThanhHieuUng;
    }

    public Boolean getXacThucEmail() {
        return xacThucEmail;
    }

    public void setXacThucEmail(Boolean xacThucEmail) {
        this.xacThucEmail = xacThucEmail;
    }

    public Boolean getMaKhoiPhuc() {
        return maKhoiPhuc;
    }

    public void setMaKhoiPhuc(Boolean maKhoiPhuc) {
        this.maKhoiPhuc = maKhoiPhuc;
    }

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public long getNgayCapNhap() {
        return ngayCapNhap;
    }

    public void setNgayCapNhap(long ngayCapNhap) {
        this.ngayCapNhap = ngayCapNhap;
    }

    public CaiDat(String maNguoiDung, Integer amThanhNen, Integer amThanhHieuUng, Boolean xacThucEmail, Boolean maKhoiPhuc, long ngayTao, long ngayCapNhap) {
        this.maNguoiDung = maNguoiDung;
        this.amThanhNen = amThanhNen;
        this.amThanhHieuUng = amThanhHieuUng;
        this.xacThucEmail = xacThucEmail;
        this.maKhoiPhuc = maKhoiPhuc;
        this.ngayTao = ngayTao;
        this.ngayCapNhap = ngayCapNhap;
    }

    public CaiDat(CaiDat caiDat) {
        this.maNguoiDung = caiDat.getMaNguoiDung();
        this.amThanhNen = caiDat.getAmThanhNen();
        this.amThanhHieuUng = caiDat.getAmThanhHieuUng();
        this.xacThucEmail = caiDat.getXacThucEmail();
        this.maKhoiPhuc = caiDat.getMaKhoiPhuc();
        this.ngayTao = caiDat.getNgayTao();
        this.ngayCapNhap = caiDat.getNgayCapNhap();
    }
}
