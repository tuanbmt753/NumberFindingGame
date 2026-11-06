package com.example.numberfindinggame.model;

import java.util.ArrayList;

public class BackgroundItem {

    private int resId;

    private String name;

    private ArrayList<LinkHinhNenDong> dsLinkHinhNenDong = new ArrayList<>();

    @Override
    public String toString() {
        return "BackgroundItem{" +
                "resId=" + resId +
                ", name='" + name + '\'' +
                ", dsLinkHinhNenDong=" + dsLinkHinhNenDong +
                '}';
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<LinkHinhNenDong> getDsLinkHinhNenDong() {
        return dsLinkHinhNenDong;
    }

    public void setDsLinkHinhNenDong(ArrayList<LinkHinhNenDong> dsLinkHinhNenDong) {
        this.dsLinkHinhNenDong = dsLinkHinhNenDong;
    }

    public BackgroundItem(int resId, String name, ArrayList<LinkHinhNenDong> dsLinkHinhNenDong) {
        this.resId = resId;
        this.name = name;
        this.dsLinkHinhNenDong = dsLinkHinhNenDong;
    }

    public BackgroundItem() {
    }
}