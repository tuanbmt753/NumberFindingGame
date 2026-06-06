package com.example.numberfindinggame.model;

public class XacThucEmail {
    private String maXacThuc;
    private String email;
    private long ngayTao;

    private int maOTP;

    public XacThucEmail(String maXacThuc, String email, long ngayTao, int maOTP) {
        this.maXacThuc = maXacThuc;
        this.email = email;
        this.ngayTao = ngayTao;
        this.maOTP = maOTP;
    }

    public int getMaOTP() {
        return maOTP;
    }

    public void setMaOTP(int maOTP) {
        this.maOTP = maOTP;
    }

    public XacThucEmail() {
    }

    @Override
    public String toString() {
        return "XacThucEmail{" +
                "maXacThuc='" + maXacThuc + '\'' +
                ", maNguoiDung='" + email + '\'' +
                ", ngayTao=" + ngayTao +
                ", maOTP=" + maOTP +
                '}';
    }

    public String getMaXacThuc() {
        return maXacThuc;
    }

    public void setMaXacThuc(String maXacThuc) {
        this.maXacThuc = maXacThuc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }
}
