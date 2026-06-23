package com.example.numberfindinggame.model;

public class BackgroundItem {

    private final int resId;

    private final String name;

    public BackgroundItem(int resId, String name) {

        this.resId = resId;

        this.name = name;
    }

    public int getResId() {

        return resId;
    }

    public String getName() {

        return name;
    }

}