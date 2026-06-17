package com.example.numberfindinggame.model;

public class NhacHieuUng {
    private Integer maHieuUng;
    private String tenHieuUng, ghiChu;

    public NhacHieuUng() {
    }

    @Override
    public String toString() {
        return "NhacHieuUng{" +
                "maHieuUng=" + maHieuUng +
                ", tenHieuUng='" + tenHieuUng + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }

    public Integer getMaHieuUng() {
        return maHieuUng;
    }

    public void setMaHieuUng(Integer maHieuUng) {
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

    public NhacHieuUng(Integer maHieuUng, String tenHieuUng, String ghiChu) {
        this.maHieuUng = maHieuUng;
        this.tenHieuUng = tenHieuUng;
        this.ghiChu = ghiChu;
    }
}
