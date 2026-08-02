package com.example.numberfindinggame.model;

public class TimSo {
    private Integer so;

    public TimSo(Integer so) {
        this.so = so;
    }

    public TimSo() {
    }

    @Override
    public String toString() {
        return "TimSo{" +
                "so=" + so +
                '}';
    }

    public Integer getSo() {
        return so;
    }

    public void setSo(Integer so) {
        this.so = so;
    }
}
