package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class GetMotelByIdResponse {

    @SerializedName("ma_day")
    private int maDay;

    @SerializedName("email_admin")
    private String emailAdmin;

    @SerializedName("ten_tro")
    private String tenTro;

    @SerializedName("dia_chi")
    private String diaChi;

    @SerializedName("so_phong")
    private int soPhong;

    @SerializedName("gia_dien")
    private double giaDien;

    @SerializedName("gia_nuoc")
    private double giaNuoc;

    // === Getter & Setter ===
    public int getMaDay() {
        return maDay;
    }

    public void setMaDay(int maDay) {
        this.maDay = maDay;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getTenTro() {
        return tenTro;
    }

    public void setTenTro(String tenTro) {
        this.tenTro = tenTro;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public double getGiaDien() {
        return giaDien;
    }

    public void setGiaDien(double giaDien) {
        this.giaDien = giaDien;
    }

    public double getGiaNuoc() {
        return giaNuoc;
    }

    public void setGiaNuoc(double giaNuoc) {
        this.giaNuoc = giaNuoc;
    }
}
