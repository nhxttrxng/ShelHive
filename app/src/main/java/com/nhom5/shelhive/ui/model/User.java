package com.nhom5.shelhive.ui.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("email")
    private String email;

    @SerializedName("ho_ten")
    private String hoTen;

    @SerializedName("dia_chi")
    private String diaChi;

    @SerializedName("gioi_tinh")
    private String gioiTinh;

    @SerializedName("que_quan")
    private String queQuan;

    @SerializedName("ngay_sinh")
    private String ngaySinh;

    @SerializedName("sdt")
    private String sdt;

    @SerializedName("cccd")
    private String cccd;

    // Constructor mặc định
    public User() {}

    // Constructor đầy đủ
    public User(String email, String hoTen, String diaChi, String gioiTinh, String queQuan,
                String ngaySinh, String sdt, String cccd) {
        this.email = email;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.queQuan = queQuan;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.cccd = cccd;
    }

    // Getter & Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
}
