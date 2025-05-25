package com.nhom5.shelhive.api;

public class UpdateAdminRequest {
    private String ho_ten;
    private String sdt;

    public UpdateAdminRequest(String ho_ten, String sdt) {
        this.ho_ten = ho_ten;
        this.sdt = sdt;
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
}
