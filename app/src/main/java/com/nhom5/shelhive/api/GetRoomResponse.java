package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class GetRoomResponse {
    @SerializedName("ma_phong")
    private String ma_phong;

    @SerializedName("ma_day")
    private int ma_day;

    @SerializedName("email_user")
    private String email_user;

    // Đã chuyển sang cột boolean da_thue, không còn dùng trang_thai_phong
    @SerializedName("da_thue")
    private Boolean da_thue;

    @SerializedName("ngay_bat_dau")
    private String ngay_bat_dau;

    @SerializedName("ngay_ket_thuc")
    private String ngay_ket_thuc;

    @SerializedName("gia_thue")
    private Double gia_thue;

    // Getters
    public String getMa_phong() {
        return ma_phong;
    }

    public int getMa_day() {
        return ma_day;
    }

    public String getEmail_user() {
        return email_user;
    }

    public Boolean getDa_thue() {
        return da_thue;
    }

    public String getNgay_bat_dau() {
        return ngay_bat_dau;
    }

    public String getNgay_ket_thuc() {
        return ngay_ket_thuc;
    }

    public Double getGia_thue() {
        return gia_thue;
    }

    // Setters (nếu cần)
    public void setMa_phong(String ma_phong) {
        this.ma_phong = ma_phong;
    }

    public void setMa_day(int ma_day) {
        this.ma_day = ma_day;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public void setDa_thue(Boolean da_thue) {
        this.da_thue = da_thue;
    }

    public void setNgay_bat_dau(String ngay_bat_dau) {
        this.ngay_bat_dau = ngay_bat_dau;
    }

    public void setNgay_ket_thuc(String ngay_ket_thuc) {
        this.ngay_ket_thuc = ngay_ket_thuc;
    }

    public void setGia_thue(Double gia_thue) {
        this.gia_thue = gia_thue;
    }
}
