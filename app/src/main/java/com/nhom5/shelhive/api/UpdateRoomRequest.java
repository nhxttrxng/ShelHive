package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class UpdateRoomRequest {
    @SerializedName("email_user")
    private String emailUser;

    // Nếu BE vẫn yêu cầu trường này thì giữ lại, nếu không thì xóa đi cho sạch code
    //@SerializedName("trang_thai_phong")
    //private String trangThaiPhong;

    @SerializedName("ngay_bat_dau")
    private String ngayBatDau;   // Đảm bảo truyền "yyyy-MM-dd"

    @SerializedName("ngay_ket_thuc")
    private String ngayKetThuc;  // Đảm bảo truyền "yyyy-MM-dd"

    @SerializedName("gia_thue")
    private Double giaThue;

    public UpdateRoomRequest() {}

    public UpdateRoomRequest(String emailUser, String ngayBatDau, String ngayKetThuc, Double giaThue) {
        this.emailUser = emailUser;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.giaThue = giaThue;
    }

    // Getters & Setters
    public String getEmailUser() { return emailUser; }
    public void setEmailUser(String emailUser) { this.emailUser = emailUser; }

    public String getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public String getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(String ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public Double getGiaThue() { return giaThue; }
    public void setGiaThue(Double giaThue) { this.giaThue = giaThue; }
}
