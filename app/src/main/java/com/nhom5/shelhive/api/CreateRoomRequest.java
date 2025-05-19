package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class CreateRoomRequest {
    @SerializedName("ma_phong")
    private String maPhong;

    @SerializedName("ma_day")
    private int maDay;

    @SerializedName("email_user")
    private String emailUser; // Nullable

    @SerializedName("ngay_bat_dau")
    private String ngayBatDau; // Nullable

    @SerializedName("ngay_ket_thuc")
    private String ngayKetThuc; // Nullable

    @SerializedName("gia_thue")
    private Double giaThue; // Nullable

    // Constructors
    public CreateRoomRequest(String maPhong, int maDay, String emailUser, String ngayBatDau, String ngayKetThuc, Double giaThue) {
        this.maPhong = maPhong;
        this.maDay = maDay;
        this.emailUser = emailUser;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giaThue = giaThue;
    }

    public CreateRoomRequest(int maDay, String emailUser, String ngayBatDau, String ngayKetThuc, Double giaThue) {
        this.maDay = maDay;
        this.emailUser = emailUser;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giaThue = giaThue;
    }

    // Getters & Setters
    public int getMaDay() { return maDay; }
    public void setMaDay(int maDay) { this.maDay = maDay; }

    public String getEmailUser() { return emailUser; }
    public void setEmailUser(String emailUser) { this.emailUser = emailUser; }

    public String getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public String getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(String ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public Double getGiaThue() { return giaThue; }
    public void setGiaThue(Double giaThue) { this.giaThue = giaThue; }
}
