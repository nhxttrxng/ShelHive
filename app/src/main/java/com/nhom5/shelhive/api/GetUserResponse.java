package com.nhom5.shelhive.api;

public class GetUserResponse {
    private String email;
    private String ho_ten;
    private String dia_chi;
    private String gioi_tinh;
    private String que_quan;
    private String mat_khau;
    private String sdt;
    private String cccd;
    private String ngay_sinh;
    private String avt;
    private Boolean is_verified;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public String getGioi_tinh() {
        return gioi_tinh;
    }

    public String getQue_quan() {
        return que_quan;
    }

    public String getMat_khau() {
        return mat_khau;
    }

    public String getSdt() {
        return sdt;
    }

    public String getCccd() {
        return cccd;
    }

    public String getNgay_sinh() {
        return ngay_sinh;
    }

    public String getAvt() {
        return avt;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    // Setters (nếu cần)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public void setGioi_tinh(String gioi_tinh) {
        this.gioi_tinh = gioi_tinh;
    }

    public void setQue_quan(String que_quan) {
        this.que_quan = que_quan;
    }

    public void setMat_khau(String mat_khau) {
        this.mat_khau = mat_khau;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setNgay_sinh(String ngay_sinh) {
        this.ngay_sinh = ngay_sinh;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }
}
