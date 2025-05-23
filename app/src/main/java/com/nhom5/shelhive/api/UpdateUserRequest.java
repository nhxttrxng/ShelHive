package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class UpdateUserRequest {
    @SerializedName("ho_ten")
    @Expose
    private String ho_ten;

    @SerializedName("ngay_sinh")
    @Expose
    private String ngay_sinh;

    @SerializedName("gioi_tinh")
    @Expose
    private String gioi_tinh;

    @SerializedName("que_quan")
    @Expose
    private String que_quan;

    @SerializedName("dia_chi")
    @Expose
    private String dia_chi;

    @SerializedName("cccd")
    @Expose
    private String cccd;

    @SerializedName("avt")
    @Expose(serialize = false) // <--- Thêm vào để KHÔNG gửi lên server nếu là null
    private String avt;

    @SerializedName("sdt")
    @Expose(serialize = false) // <--- Thêm vào để KHÔNG gửi lên server nếu là null
    private String sdt;

    public UpdateUserRequest() {}

    public UpdateUserRequest(String ho_ten, String ngay_sinh, String gioi_tinh, String que_quan,
                             String dia_chi, String cccd, String avt, String sdt) {
        this.ho_ten = ho_ten;
        this.ngay_sinh = ngay_sinh;
        this.gioi_tinh = gioi_tinh;
        this.que_quan = que_quan;
        this.dia_chi = dia_chi;
        this.cccd = cccd;
        this.avt = avt;
        this.sdt = sdt;
    }

    // Getter & Setter
    public String getHo_ten() {
        return ho_ten;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public String getNgay_sinh() {
        return ngay_sinh;
    }

    public void setNgay_sinh(String ngay_sinh) {
        this.ngay_sinh = ngay_sinh;
    }

    public String getGioi_tinh() {
        return gioi_tinh;
    }

    public void setGioi_tinh(String gioi_tinh) {
        this.gioi_tinh = gioi_tinh;
    }

    public String getQue_quan() {
        return que_quan;
    }

    public void setQue_quan(String que_quan) {
        this.que_quan = que_quan;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
