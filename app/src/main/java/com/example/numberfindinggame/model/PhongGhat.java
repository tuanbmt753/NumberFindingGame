package com.example.numberfindinggame.model;

public class PhongGhat {
    private String maPhongChat;
    private String loaiPhongChat;
    private Long ngayTao;

    @Override
    public String toString() {
        return "PhongGhat{" +
                "maPhongChat='" + maPhongChat + '\'' +
                ", loaiPhongChat='" + loaiPhongChat + '\'' +
                ", ngayTao=" + ngayTao +
                '}';
    }

    public String getMaPhongChat() {
        return maPhongChat;
    }

    public void setMaPhongChat(String maPhongChat) {
        this.maPhongChat = maPhongChat;
    }

    public String getLoaiPhongChat() {
        return loaiPhongChat;
    }

    public void setLoaiPhongChat(String loaiPhongChat) {
        this.loaiPhongChat = loaiPhongChat;
    }

    public Long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public PhongGhat(String maPhongChat, String loaiPhongChat, Long ngayTao) {
        this.maPhongChat = maPhongChat;
        this.loaiPhongChat = loaiPhongChat;
        this.ngayTao = ngayTao;
    }

    public PhongGhat() {
    }
}
