package com.nhom5.shelhive.ui.model;

import com.google.gson.annotations.SerializedName;

public class Room {
    @SerializedName("ma_phong")
    private String ma_phong;

    @SerializedName("ma_day")
    private int ma_day;

    @SerializedName("email_user")
    private String email_user;

    @SerializedName("da_thue")
    private Boolean da_thue;  // Field boolean

    @SerializedName("ngay_bat_dau")
    private String ngay_bat_dau;

    @SerializedName("ngay_ket_thuc")
    private String ngay_ket_thuc;

    @SerializedName("gia_thue")
    private Double gia_thue;

    // Số hóa đơn chưa thanh toán
    @SerializedName("unpaid_bills")
    private int unpaidBills;

    // Trạng thái thanh toán hóa đơn
    @SerializedName("pay_status")
    private String payStatus;

    // ======= THÊM MỚI: Tên người thuê =======
    // Không map @SerializedName vì thường tự gán từ user.getHoTen()
    private String tenNguoiThue;

    // Constructor đầy đủ
    public Room(String ma_phong, int ma_day, String email_user, Boolean da_thue,
                String ngay_bat_dau, String ngay_ket_thuc, Double gia_thue,
                int unpaidBills, String payStatus, String tenNguoiThue) {
        this.ma_phong = ma_phong;
        this.ma_day = ma_day;
        this.email_user = email_user;
        this.da_thue = da_thue;
        this.ngay_bat_dau = ngay_bat_dau;
        this.ngay_ket_thuc = ngay_ket_thuc;
        this.gia_thue = gia_thue;
        this.unpaidBills = unpaidBills;
        this.payStatus = payStatus;
        this.tenNguoiThue = tenNguoiThue;
    }

    // Constructor không có unpaidBills, payStatus, tenNguoiThue (cho trường hợp cũ)
    public Room(String ma_phong, int ma_day, String email_user, Boolean da_thue,
                String ngay_bat_dau, String ngay_ket_thuc, Double gia_thue) {
        this(ma_phong, ma_day, email_user, da_thue, ngay_bat_dau, ngay_ket_thuc, gia_thue, 0, null, null);
    }

    // Getters
    public String getMa_phong() { return ma_phong; }
    public int getMa_day() { return ma_day; }
    public String getEmail_user() { return email_user; }
    public Boolean getDa_thue() { return da_thue; }
    public String getNgay_bat_dau() { return ngay_bat_dau; }
    public String getNgay_ket_thuc() { return ngay_ket_thuc; }
    public Double getGia_thue() { return gia_thue; }
    public int getUnpaidBills() { return unpaidBills; }
    public String getPayStatus() { return payStatus; }
    public String getTenNguoiThue() { return tenNguoiThue; } // <-- Getter tên người thuê

    // Setters
    public void setMa_phong(String ma_phong) { this.ma_phong = ma_phong; }
    public void setMa_day(int ma_day) { this.ma_day = ma_day; }
    public void setEmail_user(String email_user) { this.email_user = email_user; }
    public void setDa_thue(Boolean da_thue) { this.da_thue = da_thue; }
    public void setNgay_bat_dau(String ngay_bat_dau) { this.ngay_bat_dau = ngay_bat_dau; }
    public void setNgay_ket_thuc(String ngay_ket_thuc) { this.ngay_ket_thuc = ngay_ket_thuc; }
    public void setGia_thue(Double gia_thue) { this.gia_thue = gia_thue; }
    public void setUnpaidBills(int unpaidBills) { this.unpaidBills = unpaidBills; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }
    public void setTenNguoiThue(String tenNguoiThue) { this.tenNguoiThue = tenNguoiThue; } // <-- Setter tên người thuê
}
