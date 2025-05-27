package com.nhom5.shelhive.api;

public class CreateExtensionRequest {
    private int ma_hoa_don;
    private String han_thanh_toan_moi;
    private double lai_suat;

    public CreateExtensionRequest(int ma_hoa_don, String han_thanh_toan_moi, double lai_suat) {
        this.ma_hoa_don = ma_hoa_don;
        this.han_thanh_toan_moi = han_thanh_toan_moi;
        this.lai_suat = lai_suat;
    }

    public int getMa_hoa_don() {
        return ma_hoa_don;
    }

    public void setMa_hoa_don(int ma_hoa_don) {
        this.ma_hoa_don = ma_hoa_don;
    }

    public String getHan_thanh_toan_moi() {
        return han_thanh_toan_moi;
    }

    public void setHan_thanh_toan_moi(String han_thanh_toan_moi) {
        this.han_thanh_toan_moi = han_thanh_toan_moi;
    }

    public double getLai_suat() {
        return lai_suat;
    }

    public void setLai_suat(double lai_suat) {
        this.lai_suat = lai_suat;
    }
}
