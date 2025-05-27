package com.nhom5.shelhive.api;

public class CreateBillRequest {
    private int ma_phong;
    private int chi_so_dien_cu;
    private int chi_so_dien_moi;
    private int chi_so_nuoc_cu;
    private int chi_so_nuoc_moi;
    private double tien_phong;
    private double tien_dien;  // Thêm mới
    private double tien_nuoc;  // Thêm mới
    private String han_dong_tien;
    private String thang_nam;

    public CreateBillRequest(int ma_phong, int chi_so_dien_cu, int chi_so_dien_moi,
                             int chi_so_nuoc_cu, int chi_so_nuoc_moi,
                             double tien_phong, double tien_dien, double tien_nuoc,
                             String han_dong_tien, String thang_nam) {
        this.ma_phong = ma_phong;
        this.chi_so_dien_cu = chi_so_dien_cu;
        this.chi_so_dien_moi = chi_so_dien_moi;
        this.chi_so_nuoc_cu = chi_so_nuoc_cu;
        this.chi_so_nuoc_moi = chi_so_nuoc_moi;
        this.tien_phong = tien_phong;
        this.tien_dien = tien_dien;
        this.tien_nuoc = tien_nuoc;
        this.han_dong_tien = han_dong_tien;
        this.thang_nam = thang_nam;
    }

    // Getter & Setter đầy đủ
    public int getMa_phong() { return ma_phong; }
    public void setMa_phong(int ma_phong) { this.ma_phong = ma_phong; }

    public int getChi_so_dien_cu() { return chi_so_dien_cu; }
    public void setChi_so_dien_cu(int chi_so_dien_cu) { this.chi_so_dien_cu = chi_so_dien_cu; }

    public int getChi_so_dien_moi() { return chi_so_dien_moi; }
    public void setChi_so_dien_moi(int chi_so_dien_moi) { this.chi_so_dien_moi = chi_so_dien_moi; }

    public int getChi_so_nuoc_cu() { return chi_so_nuoc_cu; }
    public void setChi_so_nuoc_cu(int chi_so_nuoc_cu) { this.chi_so_nuoc_cu = chi_so_nuoc_cu; }

    public int getChi_so_nuoc_moi() { return chi_so_nuoc_moi; }
    public void setChi_so_nuoc_moi(int chi_so_nuoc_moi) { this.chi_so_nuoc_moi = chi_so_nuoc_moi; }

    public double getTien_phong() { return tien_phong; }
    public void setTien_phong(double tien_phong) { this.tien_phong = tien_phong; }

    public double getTien_dien() { return tien_dien; }
    public void setTien_dien(double tien_dien) { this.tien_dien = tien_dien; }

    public double getTien_nuoc() { return tien_nuoc; }
    public void setTien_nuoc(double tien_nuoc) { this.tien_nuoc = tien_nuoc; }

    public String getHan_dong_tien() { return han_dong_tien; }
    public void setHan_dong_tien(String han_dong_tien) { this.han_dong_tien = han_dong_tien; }

    public String getThang_nam() { return thang_nam; }
    public void setThang_nam(String thang_nam) { this.thang_nam = thang_nam; }
}
