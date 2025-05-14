package com.nhom5.shelhive.ui.model;

public class Motel {
    private int ma_day;
    private String name;
    private String address;
    private int so_phong;
    private double gia_dien;
    private double gia_nuoc;

    // Constructor đầy đủ
    public Motel(int ma_day, String name, String address, int so_phong, double gia_dien, double gia_nuoc) {
        this.ma_day = ma_day;
        this.name = name;
        this.address = address;
        this.so_phong = so_phong;
        this.gia_dien = gia_dien;
        this.gia_nuoc = gia_nuoc;
    }

    // Constructor cũ (vẫn giữ nếu một số chỗ chưa cần 3 trường mới)
    public Motel(int ma_day, String name, String address) {
        this(ma_day, name, address, 0, 0.0, 0.0);
    }

    public int getMaday() {
        return ma_day;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getSo_phong() {
        return so_phong;
    }

    public void setSo_phong(int so_phong) {
        this.so_phong = so_phong;
    }

    public double getGia_dien() {
        return gia_dien;
    }

    public void setGia_dien(double gia_dien) {
        this.gia_dien = gia_dien;
    }

    public double getGia_nuoc() {
        return gia_nuoc;
    }

    public void setGia_nuoc(double gia_nuoc) {
        this.gia_nuoc = gia_nuoc;
    }
}
