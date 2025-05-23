package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class PhanAnhRequest {

    @SerializedName("ma_phong")
    private int maPhong;

    @SerializedName("tieu_de")
    private String tieuDe;

    @SerializedName("loai_su_co")
    private String loaiSuCo;

    @SerializedName("noi_dung")
    private String noiDung;

    public PhanAnhRequest(int maPhong, String tieuDe, String loaiSuCo, String noiDung) {
        this.maPhong = maPhong;
        this.tieuDe = tieuDe;
        this.loaiSuCo = loaiSuCo;
        this.noiDung = noiDung;
    }

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getLoaiSuCo() { return loaiSuCo; }
    public void setLoaiSuCo(String loaiSuCo) { this.loaiSuCo = loaiSuCo; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
}
