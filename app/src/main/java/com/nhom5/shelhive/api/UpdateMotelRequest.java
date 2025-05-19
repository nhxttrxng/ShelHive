package com.nhom5.shelhive.api;

public class UpdateMotelRequest {
    private String ten_tro;
    private String dia_chi;
    private double gia_dien;
    private double gia_nuoc;

    public UpdateMotelRequest(String ten_tro, String dia_chi, double gia_dien, double gia_nuoc) {
        this.ten_tro = ten_tro;
        this.dia_chi = dia_chi;
        this.gia_dien = gia_dien;
        this.gia_nuoc = gia_nuoc;
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
