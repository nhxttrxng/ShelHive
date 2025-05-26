package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class GetBillRequest {
    @SerializedName("ma_hoa_don")
    private int maHoaDon;

    @SerializedName("ma_phong")
    private String maPhong;

    @SerializedName("tong_tien")
    private double tongTien;

    @SerializedName("so_dien")
    private int soDien;

    @SerializedName("so_nuoc")
    private int soNuoc;

    @SerializedName("han_dong_tien")
    private String hanDongTien;

    @SerializedName("trang_thai")
    private String trangThai;

    @SerializedName("chi_so_dien_cu")
    private int chiSoDienCu;

    @SerializedName("chi_so_dien_moi")
    private int chiSoDienMoi;

    @SerializedName("chi_so_nuoc_cu")
    private int chiSoNuocCu;

    @SerializedName("chi_so_nuoc_moi")
    private int chiSoNuocMoi;

    @SerializedName("tien_dien")
    private double tienDien;

    @SerializedName("tien_nuoc")
    private double tienNuoc;

    @SerializedName("tien_phong")
    private double tienPhong;

    @SerializedName("ngay_tao")
    private String ngayTao;

    @SerializedName("ngay_gia_han")
    private String ngayGiaHan;

    @SerializedName("da_duyet_gia_han")
    private Boolean daDuyetGiaHan;

    @SerializedName("ngay_cap_nhat_gia_han")
    private String ngayCapNhatGiaHan;

    @SerializedName("tien_lai_gia_han")
    private Double tienLaiGiaHan;

    @SerializedName("so_ngay_gia_han")
    private Integer soNgayGiaHan;

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public String getMaPhong() {
        return maPhong;
    }

}
