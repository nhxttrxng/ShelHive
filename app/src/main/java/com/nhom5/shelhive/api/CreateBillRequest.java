package com.nhom5.shelhive.api;

public class CreateBillRequest {
    private String ma_phong;
    private int chi_so_dien_cu;
    private int chi_so_dien_moi;
    private int chi_so_nuoc_cu;
    private int chi_so_nuoc_moi;
    private double tien_phong;
    private String han_dong_tien;

    public CreateBillRequest(String ma_phong, int chi_so_dien_cu, int chi_so_dien_moi,
                             int chi_so_nuoc_cu, int chi_so_nuoc_moi, double tien_phong,
                             String han_dong_tien) {
        this.ma_phong = ma_phong;
        this.chi_so_dien_cu = chi_so_dien_cu;
        this.chi_so_dien_moi = chi_so_dien_moi;
        this.chi_so_nuoc_cu = chi_so_nuoc_cu;
        this.chi_so_nuoc_moi = chi_so_nuoc_moi;
        this.tien_phong = tien_phong;
        this.han_dong_tien = han_dong_tien;
    }

    // Getter và Setter nếu cần
}
