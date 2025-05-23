package com.nhom5.shelhive.model;

import java.sql.Timestamp; // hoặc java.util.Date nếu dùng kiểu này

public class ThongBao {
    private int ma_thong_bao;
    private int ma_day;
    private String noi_dung;
    private Timestamp ngay_tao;

    public ThongBao(int ma_thong_bao, int ma_day, String noi_dung, Timestamp ngay_tao) {
        this.ma_thong_bao = ma_thong_bao;
        this.ma_day = ma_day;
        this.noi_dung = noi_dung;
        this.ngay_tao = ngay_tao;
    }

    public int getMaThongBao() {
        return ma_thong_bao;
    }

    public void setMaThongBao(int ma_thong_bao) {
        this.ma_thong_bao = ma_thong_bao;
    }

    public int getMaDay() {
        return ma_day;
    }

    public void setMaDay(int ma_day) {
        this.ma_day = ma_day;
    }

    public String getNoiDung() {
        return noi_dung;
    }

    public void setNoiDung(String noi_dung) {
        this.noi_dung = noi_dung;
    }

    public Timestamp getNgayTao() {
        return ngay_tao;
    }

    public void setNgayTao(Timestamp ngay_tao) {
        this.ngay_tao = ngay_tao;
    }
}
