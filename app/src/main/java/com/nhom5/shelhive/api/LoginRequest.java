package com.nhom5.shelhive.api;

public class LoginRequest {
    private String email;
    private String mat_khau; // ĐỔI thành mat_khau cho đúng với API

    public LoginRequest(String email, String mat_khau) {
        this.email = email;
        this.mat_khau = mat_khau;
    }

    public String getEmail() {
        return email;
    }

    public String getMat_khau() {
        return mat_khau;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMat_khau(String mat_khau) {
        this.mat_khau = mat_khau;
    }
}
