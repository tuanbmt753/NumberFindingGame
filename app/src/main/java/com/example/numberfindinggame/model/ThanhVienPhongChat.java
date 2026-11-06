package com.example.numberfindinggame.model;

public class ThanhVienPhongChat {
    private String maThanhVienPhongChat;
    private String maPhongChat;
    private String maNguoiDung;
    private Long ngayTao;

    @Override
    public String toString() {
        return "ThanhVienPhongChat{" +
                "maThanhVienPhongChat='" + maThanhVienPhongChat + '\'' +
                ", maPhongChat='" + maPhongChat + '\'' +
                ", maNguoiDung='" + maNguoiDung + '\'' +
                ", ngayTao=" + ngayTao +
                '}';
    }

    public String getMaThanhVienPhongChat() {
        return maThanhVienPhongChat;
    }

    public void setMaThanhVienPhongChat(String maThanhVienPhongChat) {
        this.maThanhVienPhongChat = maThanhVienPhongChat;
    }

    public String getMaPhongChat() {
        return maPhongChat;
    }

    public void setMaPhongChat(String maPhongChat) {
        this.maPhongChat = maPhongChat;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public Long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public ThanhVienPhongChat(String maThanhVienPhongChat, String maPhongChat, String maNguoiDung, Long ngayTao) {
        this.maThanhVienPhongChat = maThanhVienPhongChat;
        this.maPhongChat = maPhongChat;
        this.maNguoiDung = maNguoiDung;
        this.ngayTao = ngayTao;
    }

    public ThanhVienPhongChat() {
    }
}
