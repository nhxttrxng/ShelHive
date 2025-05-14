package com.nhom5.shelhive.model;

public class PhanAnh {
    private String tieuDe;
    private String loaiVanDe;
    private String moTa;
    private boolean daXuLy;
    private String phong;
    private String noiDung;

    public PhanAnh(String phong, String noiDung, boolean daXuLy) {
        this.phong = phong;
        this.noiDung = noiDung;
        this.daXuLy = daXuLy;
        this.tieuDe = tieuDe;
        this.loaiVanDe = loaiVanDe;
        this.moTa = "";
    }

    public String getPhong() {
        return phong;
    }

    public String getNoiDung() {
        return noiDung;
    }


    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getLoaiVanDe() {
        return loaiVanDe;
    }

    public void setLoaiVanDe(String loaiVanDe) {
        this.loaiVanDe = loaiVanDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isDaXuLy() {
        return daXuLy;
    }

    public void setDaXuLy(boolean daXuLy) {
        this.daXuLy = daXuLy;
    }
}
