package com.example.numberfindinggame.model;

public class NhacNen {
    private int maNhacNen;
    private String txtTenNhacNen, txtGhiChu;

    @Override
    public String toString() {
        return "NhacNen{" +
                "maNhacNen=" + maNhacNen +
                ", txtTenNhacNen='" + txtTenNhacNen + '\'' +
                ", txtGhiChu='" + txtGhiChu + '\'' +
                '}';
    }

    public int getMaNhacNen() {
        return maNhacNen;
    }

    public void setMaNhacNen(int maNhacNen) {
        this.maNhacNen = maNhacNen;
    }

    public String getTxtTenNhacNen() {
        return txtTenNhacNen;
    }

    public void setTxtTenNhacNen(String txtTenNhacNen) {
        this.txtTenNhacNen = txtTenNhacNen;
    }

    public String getTxtGhiChu() {
        return txtGhiChu;
    }

    public void setTxtGhiChu(String txtGhiChu) {
        this.txtGhiChu = txtGhiChu;
    }

    public NhacNen(int maNhacNen, String txtTenNhacNen, String txtGhiChu) {
        this.maNhacNen = maNhacNen;
        this.txtTenNhacNen = txtTenNhacNen;
        this.txtGhiChu = txtGhiChu;
    }

    public NhacNen() {
    }
}
