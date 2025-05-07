package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("ho_ten")
    private String ho_ten;

    @SerializedName("sdt")
    private String sdt;

    @SerializedName("mat_khau")
    private String mat_khau;

    // Constructor
    public RegisterRequest(String email, String ho_ten, String sdt, String mat_khau) {
        this.email = email;
        this.ho_ten = ho_ten;
        this.sdt = sdt;
        this.mat_khau = mat_khau;
    }

    // Getter và Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMat_khau() {
        return mat_khau;
    }

    public void setMat_khau(String mat_khau) {
        this.mat_khau = mat_khau;
    }
}
