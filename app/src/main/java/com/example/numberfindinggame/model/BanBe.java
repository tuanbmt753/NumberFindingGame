package com.example.numberfindinggame.model;

public class BanBe {
    private String maBanBe;
    private String maNguoiDung1;
    private String maNguoiDung2;
    private String trangThai; //TuChoi - DongY - LoiMoiKetBan
    private long ngayTao;

    private NguoiDung nguoiDung1;
    private NguoiDung nguoiDung2;

    public NguoiDung getNguoiDung2() {
        return nguoiDung2;
    }

    public void setNguoiDung2(NguoiDung nguoiDung2) {
        this.nguoiDung2 = nguoiDung2;
    }

    public NguoiDung getNguoiDung1() {
        return nguoiDung1;
    }

    public void setNguoiDung1(NguoiDung nguoiDung1) {
        this.nguoiDung1 = nguoiDung1;
    }

    @Override
    public String toString() {
        return "BanBe{" +
                "maBanBe='" + maBanBe + '\'' +
                ", maNguoiDung1='" + maNguoiDung1 + '\'' +
                ", maNguoiDung2='" + maNguoiDung2 + '\'' +
                ", trangThai='" + trangThai + '\'' +
                ", ngayTao=" + ngayTao +
                '}';
    }

    public String getMaBanBe() {
        return maBanBe;
    }

    public void setMaBanBe(String maBanBe) {
        this.maBanBe = maBanBe;
    }

    public String getMaNguoiDung1() {
        return maNguoiDung1;
    }

    public void setMaNguoiDung1(String maNguoiDung1) {
        this.maNguoiDung1 = maNguoiDung1;
    }

    public String getMaNguoiDung2() {
        return maNguoiDung2;
    }

    public void setMaNguoiDung2(String maNguoiDung2) {
        this.maNguoiDung2 = maNguoiDung2;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public BanBe() {
    }

    public BanBe(String maBanBe, String maNguoiDung1, String maNguoiDung2, String trangThai, long ngayTao) {
        this.maBanBe = maBanBe;
        this.maNguoiDung1 = maNguoiDung1;
        this.maNguoiDung2 = maNguoiDung2;
        this.trangThai = trangThai;
        this.ngayTao = ngayTao;
    }
}
