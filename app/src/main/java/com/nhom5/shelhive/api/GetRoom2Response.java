package com.nhom5.shelhive.api;

public class GetRoom2Response {
    private String ma_phong;
    private int ma_day;
    private String email_user;
    private String trang_thai_phong;
    private String ngay_bat_dau;
    private String ngay_ket_thuc;
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

    public String getTrang_thai_phong() {
        return trang_thai_phong;
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

    public void setTrang_thai_phong(String trang_thai_phong) {
        this.trang_thai_phong = trang_thai_phong;
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
