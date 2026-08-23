package com.example.numberfindinggame.model;

public class HieuUng {
    private int maHieuUng;
    private String tenHieuUng, ghiChu;

    @Override
    public String toString() {
        return "HieuUng{" +
                "maHieuUng=" + maHieuUng +
                ", tenHieuUng='" + tenHieuUng + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }

    public int getMaHieuUng() {
        return maHieuUng;
    }

    public void setMaHieuUng(int maHieuUng) {
        this.maHieuUng = maHieuUng;
    }

    public String getTenHieuUng() {
        return tenHieuUng;
    }

    public void setTenHieuUng(String tenHieuUng) {
        this.tenHieuUng = tenHieuUng;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public HieuUng(int maHieuUng, String tenHieuUng, String ghiChu) {
        this.maHieuUng = maHieuUng;
        this.tenHieuUng = tenHieuUng;
        this.ghiChu = ghiChu;
    }

    public HieuUng() {
    }
}
