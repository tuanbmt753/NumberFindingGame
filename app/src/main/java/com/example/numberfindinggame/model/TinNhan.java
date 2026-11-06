package com.example.numberfindinggame.model;

public class TinNhan {
    private String maTinNhan;
    private String maNguoiDung; //Người gửi tin nhắn
    private String maPhongChat;
    private String noiDung;
    private String loaiTinNhan;
    private long ngayTao;

    @Override
    public String toString() {
        return "TinNhan{" +
                "maTinNhan='" + maTinNhan + '\'' +
                ", maNguoiDung='" + maNguoiDung + '\'' +
                ", maPhongChat='" + maPhongChat + '\'' +
                ", noiDung='" + noiDung + '\'' +
                ", loaiTinNhan='" + loaiTinNhan + '\'' +
                ", ngayTao=" + ngayTao +
                '}';
    }

    public String getMaTinNhan() {
        return maTinNhan;
    }

    public void setMaTinNhan(String maTinNhan) {
        this.maTinNhan = maTinNhan;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaPhongChat() {
        return maPhongChat;
    }

    public void setMaPhongChat(String maPhongChat) {
        this.maPhongChat = maPhongChat;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getLoaiTinNhan() {
        return loaiTinNhan;
    }

    public void setLoaiTinNhan(String loaiTinNhan) {
        this.loaiTinNhan = loaiTinNhan;
    }

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public TinNhan(String maTinNhan, String maNguoiDung, String maPhongChat, String noiDung, String loaiTinNhan, long ngayTao) {
        this.maTinNhan = maTinNhan;
        this.maNguoiDung = maNguoiDung;
        this.maPhongChat = maPhongChat;
        this.noiDung = noiDung;
        this.loaiTinNhan = loaiTinNhan;
        this.ngayTao = ngayTao;
    }

    public TinNhan() {
    }
}
