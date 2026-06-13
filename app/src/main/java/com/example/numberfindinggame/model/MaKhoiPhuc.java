package com.example.numberfindinggame.model;

public class MaKhoiPhuc {
    private String maNguoiDung;
    private Integer maKhoiPhuc;
    private Long ngayTao;

    public MaKhoiPhuc() {
    }

    public MaKhoiPhuc(String maNguoiDung, Integer maKhoiPhuc, Long ngayTao) {
        this.maNguoiDung = maNguoiDung;
        this.maKhoiPhuc = maKhoiPhuc;
        this.ngayTao = ngayTao;
    }

    @Override
    public String toString() {
        return "MaKhoiPhuc{" +
                "maNguoiDung='" + maNguoiDung + '\'' +
                ", maKhoiPhuc=" + maKhoiPhuc +
                ", ngayTao=" + ngayTao +
                '}';
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public Integer getMaKhoiPhuc() {
        return maKhoiPhuc;
    }

    public void setMaKhoiPhuc(Integer maKhoiPhuc) {
        this.maKhoiPhuc = maKhoiPhuc;
    }

    public Long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Long ngayTao) {
        this.ngayTao = ngayTao;
    }
}
