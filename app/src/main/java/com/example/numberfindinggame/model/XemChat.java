package com.example.numberfindinggame.model;

public class XemChat {
    private String maXemChat;
    private boolean daXem;
    private String maTinNhan;

    private String maNguoiDung;

    private long ngayTao;

    @Override
    public String toString() {
        return "XemChat{" +
                "maXemChat='" + maXemChat + '\'' +
                ", daXem=" + daXem +
                ", maTinNhan='" + maTinNhan + '\'' +
                ", maNguoiDung='" + maNguoiDung + '\'' +
                ", ngayTao=" + ngayTao +
                '}';
    }

    public String getMaXemChat() {
        return maXemChat;
    }

    public void setMaXemChat(String maXemChat) {
        this.maXemChat = maXemChat;
    }

    public boolean isDaXem() {
        return daXem;
    }

    public void setDaXem(boolean daXem) {
        this.daXem = daXem;
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

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public XemChat(String maXemChat, boolean daXem, String maTinNhan, String maNguoiDung, long ngayTao) {
        this.maXemChat = maXemChat;
        this.daXem = daXem;
        this.maTinNhan = maTinNhan;
        this.maNguoiDung = maNguoiDung;
        this.ngayTao = ngayTao;
    }

    public XemChat() {
    }
}
