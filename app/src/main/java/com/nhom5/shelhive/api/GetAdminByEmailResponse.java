package com.nhom5.shelhive.api;

public class GetAdminByEmailResponse {
    private String email;
    private String ho_ten;
    private String sdt;
    private String avt;

    // Getter methods
    public String getEmail() {
        return email;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public String getSdt() {
        return sdt;
    }

    public String getAvt() {
        return avt;
    }

    // Setter methods
    public void setEmail(String email) {
        this.email = email;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}

