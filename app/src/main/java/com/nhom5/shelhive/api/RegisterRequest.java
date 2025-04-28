package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("ho_ten")
    private String hoTen;

    @SerializedName("sdt")
    private String sdt;

    @SerializedName("mat_khau")
    private String matKhau;

    public RegisterRequest(String email, String hoTen, String sdt, String matKhau) {
        this.email = email;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return email;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public String getMatKhau() {
        return matKhau;
    }
}
