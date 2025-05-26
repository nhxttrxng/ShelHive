package com.nhom5.shelhive.ui.model;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class ThongBaoHoaDon {
    private int ma_thong_bao_hoa_don;
    private int ma_hoa_don;
    private String noi_dung;
    private Timestamp ngay_tao;
    private int ma_day;

    // Constructor rỗng (bắt buộc nếu dùng Retrofit hoặc Firebase)
    public ThongBaoHoaDon() {
    }

    // Constructor đầy đủ
    public ThongBaoHoaDon(int ma_thong_bao_hoa_don, int ma_hoa_don,int ma_day, String noi_dung, Timestamp ngay_tao) {
        this.ma_thong_bao_hoa_don = ma_thong_bao_hoa_don;
        this.ma_hoa_don = ma_hoa_don;
        this.ma_day=ma_day;
        this.noi_dung = noi_dung;
        this.ngay_tao = ngay_tao;
    }

    // Getters
    public int getMa_thong_bao_hoa_don() {
        return ma_thong_bao_hoa_don;
    }

    public int getMa_hoa_don() {
        return ma_hoa_don;
    }

    public String getNoi_dung() {
        return noi_dung;
    }

    public Date getNgay_tao() {
        return ngay_tao;
    }
public int getMa_day(){
        return ma_day;
}
public void setMa_day(int ma_day)
{
    this.ma_day=ma_day;
}
    // Setters
    public void setMa_thong_bao_hoa_don(int ma_thong_bao_hoa_don) {
        this.ma_thong_bao_hoa_don = ma_thong_bao_hoa_don;
    }

    public void setMa_hoa_don(int ma_hoa_don) {
        this.ma_hoa_don = ma_hoa_don;
    }

    public void setNoi_dung(String noi_dung) {
        this.noi_dung = noi_dung;
    }

    public void setNgay_tao(Timestamp ngay_tao) {
        this.ngay_tao = ngay_tao;
    }

    @Override
    public String toString() {
        return "ThongBaoHoaDon{" +
                "ma_thong_bao_hoa_don=" + ma_thong_bao_hoa_don +
                ", ma_hoa_don=" + ma_hoa_don +
                ", noi_dung='" + noi_dung + '\'' +
                ", ngay_tao=" + ngay_tao +
                '}';
    }
}
