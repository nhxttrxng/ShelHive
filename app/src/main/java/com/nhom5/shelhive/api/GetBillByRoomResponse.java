package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class GetBillByRoomResponse {
    @SerializedName("ma_hoa_don")
    private int maHoaDon;

    @SerializedName("ma_phong")
    private int maPhong;

    @SerializedName("tong_tien")
    private String tongTien;

    @SerializedName("so_dien")
    private int soDien;

    @SerializedName("so_nuoc")
    private int soNuoc;

    @SerializedName("han_dong_tien")
    private String hanDongTien;

    @SerializedName("trang_thai")
    private String trangThai;

    @SerializedName("da_duyet_gia_han")
    private boolean daDuyetGiaHan;

    @SerializedName("ngay_gia_han")
    private String ngayGiaHan;

    @SerializedName("chi_so_dien_cu")
    private int chiSoDienCu;

    @SerializedName("chi_so_dien_moi")
    private int chiSoDienMoi;

    @SerializedName("chi_so_nuoc_cu")
    private int chiSoNuocCu;

    @SerializedName("chi_so_nuoc_moi")
    private int chiSoNuocMoi;

    @SerializedName("tien_dien")
    private String tienDien;

    @SerializedName("tien_nuoc")
    private String tienNuoc;

    @SerializedName("tien_phong")
    private String tienPhong;

    @SerializedName("ngay_tao")
    private String ngayTao;

    @SerializedName("tien_lai_gia_han")
    private String tienLaiGiaHan;

    @SerializedName("so_ngay_gia_han")
    private int soNgayGiaHan;

    @SerializedName("ngay_cap_nhat_gia_han")
    private String ngayCapNhatGiaHan;

    @SerializedName("ngay_thanh_toan")
    private String ngayThanhToan;

    @SerializedName("thang_nam")
    private String thangNam;

    // --- Getter & Setter ---

    public int getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(int maHoaDon) { this.maHoaDon = maHoaDon; }

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }

    public String getTongTien() { return tongTien; }
    public void setTongTien(String tongTien) { this.tongTien = tongTien; }

    public int getSoDien() { return soDien; }
    public void setSoDien(int soDien) { this.soDien = soDien; }

    public int getSoNuoc() { return soNuoc; }
    public void setSoNuoc(int soNuoc) { this.soNuoc = soNuoc; }

    public String getHanDongTien() { return hanDongTien; }
    public void setHanDongTien(String hanDongTien) { this.hanDongTien = hanDongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public boolean isDaDuyetGiaHan() { return daDuyetGiaHan; }
    public void setDaDuyetGiaHan(boolean daDuyetGiaHan) { this.daDuyetGiaHan = daDuyetGiaHan; }

    public String getNgayGiaHan() { return ngayGiaHan; }
    public void setNgayGiaHan(String ngayGiaHan) { this.ngayGiaHan = ngayGiaHan; }

    public int getChiSoDienCu() { return chiSoDienCu; }
    public void setChiSoDienCu(int chiSoDienCu) { this.chiSoDienCu = chiSoDienCu; }

    public int getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(int chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }

    public int getChiSoNuocCu() { return chiSoNuocCu; }
    public void setChiSoNuocCu(int chiSoNuocCu) { this.chiSoNuocCu = chiSoNuocCu; }

    public int getChiSoNuocMoi() { return chiSoNuocMoi; }
    public void setChiSoNuocMoi(int chiSoNuocMoi) { this.chiSoNuocMoi = chiSoNuocMoi; }

    public String getTienDien() { return tienDien; }
    public void setTienDien(String tienDien) { this.tienDien = tienDien; }

    public String getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(String tienNuoc) { this.tienNuoc = tienNuoc; }

    public String getTienPhong() { return tienPhong; }
    public void setTienPhong(String tienPhong) { this.tienPhong = tienPhong; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public String getTienLaiGiaHan() { return tienLaiGiaHan; }
    public void setTienLaiGiaHan(String tienLaiGiaHan) { this.tienLaiGiaHan = tienLaiGiaHan; }

    public int getSoNgayGiaHan() { return soNgayGiaHan; }
    public void setSoNgayGiaHan(int soNgayGiaHan) { this.soNgayGiaHan = soNgayGiaHan; }

    public String getNgayCapNhatGiaHan() { return ngayCapNhatGiaHan; }
    public void setNgayCapNhatGiaHan(String ngayCapNhatGiaHan) { this.ngayCapNhatGiaHan = ngayCapNhatGiaHan; }

    public String getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(String ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public String getThangNam() { return thangNam; }
    public void setThangNam(String thangNam) { this.thangNam = thangNam; }
}
