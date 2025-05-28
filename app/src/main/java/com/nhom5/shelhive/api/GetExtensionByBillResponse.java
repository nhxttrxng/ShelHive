// GetExtensionByBillResponse.java
package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class GetExtensionByBillResponse {
    @SerializedName("ma_gia_han")
    private int maGiaHan;

    @SerializedName("ma_hoa_don")
    private int maHoaDon;

    @SerializedName("han_dong_tien_goc")
    private String hanDongTienGoc; // yyyy-MM-dd

    @SerializedName("han_thanh_toan_moi")
    private String hanThanhToanMoi; // yyyy-MM-dd

    @SerializedName("trang_thai")
    private String trangThai;

    @SerializedName("lai_suat")
    private double laiSuat;

    @SerializedName("tien_lai_tinh_du_kien")
    private double tienLaiTinhDuKien;

    // Getter & Setter
    public int getMaGiaHan() { return maGiaHan; }
    public void setMaGiaHan(int maGiaHan) { this.maGiaHan = maGiaHan; }

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getHanDongTienGoc() { return hanDongTienGoc; }
    public void setHanDongTienGoc(String hanDongTienGoc) { this.hanDongTienGoc = hanDongTienGoc; }

    public String getHanThanhToanMoi() { return hanThanhToanMoi; }
    public void setHanThanhToanMoi(String hanThanhToanMoi) { this.hanThanhToanMoi = hanThanhToanMoi; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public double getLaiSuat() { return laiSuat; }
    public void setLaiSuat(double laiSuat) { this.laiSuat = laiSuat; }

    public double getTienLaiTinhDuKien() { return tienLaiTinhDuKien; }
    public void setTienLaiTinhDuKien(double tienLaiTinhDuKien) { this.tienLaiTinhDuKien = tienLaiTinhDuKien; }
}
