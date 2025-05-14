package com.nhom5.shelhive.api;

public class CreateDayTroRequest {
    private String email_admin;
    private String ten_tro;
    private String dia_chi;
    private int so_phong;
    private double gia_dien;
    private double gia_nuoc;

    public CreateDayTroRequest(String email_admin, String ten_tro, String dia_chi, int so_phong, double gia_dien, double gia_nuoc) {
        this.email_admin = email_admin;
        this.ten_tro = ten_tro;
        this.dia_chi = dia_chi;
        this.so_phong = so_phong;
        this.gia_dien = gia_dien;
        this.gia_nuoc = gia_nuoc;
    }

    public String getEmail_admin() {
        return email_admin;
    }

    public void setEmail_admin(String email_admin) {
        this.email_admin = email_admin;
    }

    public String getTen_tro() {
        return ten_tro;
    }

    public void setTen_tro(String ten_tro) {
        this.ten_tro = ten_tro;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
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
