package com.nhom5.shelhive.model;

import com.google.gson.annotations.SerializedName;

public class PhanAnh {

    @SerializedName("ma_phan_anh")
    private int ma_phan_anh;

    @SerializedName("ma_phong")
    private int ma_phong;

    @SerializedName("tieu_de")
    private String tieu_de;

    @SerializedName("loai_su_co")
    private String loai_su_co;

    @SerializedName("noi_dung")
    private String noi_dung;

    @SerializedName("tinh_trang")
    private String tinh_trang;

    // Getters and Setters
    public int getMaPhanAnh() {
        return ma_phan_anh;
    }

    public void setMaPhanAnh(int maPhanAnh) {
        this.ma_phan_anh = maPhanAnh;
    }

    public int getMaPhong() {
        return ma_phong;
    }

    public void setMaPhong(int maPhong) {
        this.ma_phong = maPhong;
    }

    public String getTieuDe() {
        return tieu_de;
    }

    public void setTieuDe(String tieuDe) {
        this.tieu_de = tieuDe;
    }

    public String getLoaiSuCo() {
        return loai_su_co;
    }

    public void setLoaiSuCo(String loaiSuCo) {
        this.loai_su_co = loaiSuCo;
    }

    public String getNoiDung() {
        return noi_dung;
    }

    public void setNoiDung(String noiDung) {
        this.noi_dung = noiDung;
    }

    public String getTinhTrang() {
        return tinh_trang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinh_trang = tinhTrang;
    }
}
