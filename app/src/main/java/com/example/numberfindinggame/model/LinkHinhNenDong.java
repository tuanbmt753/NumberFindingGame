package com.example.numberfindinggame.model;

public class LinkHinhNenDong {
    private String tenLink;
    private String duongDanLink;

    @Override
    public String toString() {
        return "LinkHinhNenDong{" +
                "tenLink='" + tenLink + '\'' +
                ", duongDanLink='" + duongDanLink + '\'' +
                '}';
    }

    public String getTenLink() {
        return tenLink;
    }

    public void setTenLink(String tenLink) {
        this.tenLink = tenLink;
    }

    public String getDuongDanLink() {
        return duongDanLink;
    }

    public void setDuongDanLink(String duongDanLink) {
        this.duongDanLink = duongDanLink;
    }

    public LinkHinhNenDong(String tenLink, String duongDanLink) {
        this.tenLink = tenLink;
        this.duongDanLink = duongDanLink;
    }

    public LinkHinhNenDong() {
    }
}
