package com.nhom5.shelhive.api;

import java.sql.Timestamp;

public class ThongBaoHDDonGian {
    private String noi_dung;
    private Timestamp ngay_tao;
    private int ma_phong;

    public String getNoi_dung() {
        return noi_dung;
    }

    public Timestamp getNgay_tao() {
        return ngay_tao;
    }

    public int getMa_phong() {
        return ma_phong;
    }
}
