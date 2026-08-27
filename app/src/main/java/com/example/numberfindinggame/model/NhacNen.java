package com.example.numberfindinggame.model;

public class NhacNen {
    private int maNhacNen;
    private String txtTenNhacNen, txtGhiChu;
    private String linhDrive, linhYoutube;

    @Override
    public String toString() {
        return "NhacNen{" +
                "maNhacNen=" + maNhacNen +
                ", txtTenNhacNen='" + txtTenNhacNen + '\'' +
                ", txtGhiChu='" + txtGhiChu + '\'' +
                ", linhDrive='" + linhDrive + '\'' +
                ", linhYoutube='" + linhYoutube + '\'' +
                '}';
    }

    public void setMaNhacNen(int maNhacNen) {
        this.maNhacNen = maNhacNen;
    }

    public void setTxtTenNhacNen(String txtTenNhacNen) {
        this.txtTenNhacNen = txtTenNhacNen;
    }

    public void setTxtGhiChu(String txtGhiChu) {
        this.txtGhiChu = txtGhiChu;
    }

    public void setLinhDrive(String linhDrive) {
        this.linhDrive = linhDrive;
    }

    public void setLinhYoutube(String linhYoutube) {
        this.linhYoutube = linhYoutube;
    }

    public int getMaNhacNen() {
        return maNhacNen;
    }

    public String getTxtTenNhacNen() {
        return txtTenNhacNen;
    }

    public String getTxtGhiChu() {
        return txtGhiChu;
    }

    public String getLinhDrive() {
        return linhDrive;
    }

    public String getLinhYoutube() {
        return linhYoutube;
    }

    public NhacNen() {
    }

    public NhacNen(int maNhacNen, String txtTenNhacNen, String txtGhiChu, String linhDrive, String linhYoutube) {
        this.maNhacNen = maNhacNen;
        this.txtTenNhacNen = txtTenNhacNen;
        this.txtGhiChu = txtGhiChu;
        this.linhDrive = linhDrive;
        this.linhYoutube = linhYoutube;
    }
}
