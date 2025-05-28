package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class UpdateBillRequest {
    @SerializedName("chi_so_dien_moi")
    private Integer chiSoDienMoi;

    @SerializedName("chi_so_nuoc_moi")
    private Integer chiSoNuocMoi;

    @SerializedName("tien_dien")
    private Double tienDien;

    @SerializedName("tien_nuoc")
    private Double tienNuoc;

    @SerializedName("so_dien")
    private Integer soDien;

    @SerializedName("so_nuoc")
    private Integer soNuoc;

    @SerializedName("tien_phong")
    private Double tienPhong;

    @SerializedName("tong_tien")
    private Double tongTien;

    @SerializedName("tien_lai_gia_han")
    private Double tienLaiGiaHan;

    // Constructors
    public UpdateBillRequest() {}

    public UpdateBillRequest(Integer chiSoDienMoi, Integer chiSoNuocMoi, Double tienDien, Double tienNuoc,
                             Integer soDien, Integer soNuoc, Double tienPhong, Double tongTien, Double tienLaiGiaHan) {
        this.chiSoDienMoi = chiSoDienMoi;
        this.chiSoNuocMoi = chiSoNuocMoi;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.soDien = soDien;
        this.soNuoc = soNuoc;
        this.tienPhong = tienPhong;
        this.tongTien = tongTien;
        this.tienLaiGiaHan = tienLaiGiaHan;
    }

    // Getters & Setters
    public Integer getChiSoDienMoi() { return chiSoDienMoi; }
    public void setChiSoDienMoi(Integer chiSoDienMoi) { this.chiSoDienMoi = chiSoDienMoi; }

    public Integer getChiSoNuocMoi() { return chiSoNuocMoi; }
    public void setChiSoNuocMoi(Integer chiSoNuocMoi) { this.chiSoNuocMoi = chiSoNuocMoi; }

    public Double getTienDien() { return tienDien; }
    public void setTienDien(Double tienDien) { this.tienDien = tienDien; }

    public Double getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(Double tienNuoc) { this.tienNuoc = tienNuoc; }

    public Integer getSoDien() { return soDien; }
    public void setSoDien(Integer soDien) { this.soDien = soDien; }

    public Integer getSoNuoc() { return soNuoc; }
    public void setSoNuoc(Integer soNuoc) { this.soNuoc = soNuoc; }

    public Double getTienPhong() { return tienPhong; }
    public void setTienPhong(Double tienPhong) { this.tienPhong = tienPhong; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public Double getTienLaiGiaHan() { return tienLaiGiaHan; }
    public void setTienLaiGiaHan(Double tienLaiGiaHan) { this.tienLaiGiaHan = tienLaiGiaHan; }
}
