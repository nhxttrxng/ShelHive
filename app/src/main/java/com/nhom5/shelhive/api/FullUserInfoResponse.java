package com.nhom5.shelhive.api;

public class FullUserInfoResponse {
    private String ho_ten;

    private String avt;
    private Integer ma_phong;
    private String ten_tro;
    private String dia_chi;

    // Getter
    public String getHo_ten() {
        return ho_ten;
    }

    public String getAvt() {
        return avt;
    }

    public Integer getMa_phong() {
        return ma_phong;
    }

    public String getTen_tro() {
        return ten_tro;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    // Setter
    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public void setMa_phong(Integer ma_phong) {
        this.ma_phong = ma_phong;
    }

    public void setTen_tro(String ten_tro) {
        this.ten_tro = ten_tro;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }
}

