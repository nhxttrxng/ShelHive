package com.nhom5.shelhive.api;

public class ThongBaoRequest {
    private int ma_day;
    private String noi_dung;

    public ThongBaoRequest(int ma_day, String noi_dung) {
        this.ma_day = ma_day;
        this.noi_dung = noi_dung;
    }

    // Getter và Setter
    public int getMa_day() { return ma_day; }
    public String getNoi_dung() { return noi_dung; }
    public void setMa_day(int ma_day) { this.ma_day = ma_day; }
    public void setNoi_dung(String noi_dung) { this.noi_dung = noi_dung; }
}

