package com.nhom5.shelhive.api;

public class CreateInvoiceNotificationRequest {
    private int ma_hoa_don;
    private String noi_dung;
    private String ngay_tao; // yyyy-MM-dd HH:mm:ss hoặc yyyy-MM-dd tuỳ BE

    public CreateInvoiceNotificationRequest(int ma_hoa_don, String noi_dung, String ngay_tao) {
        this.ma_hoa_don = ma_hoa_don;
        this.noi_dung = noi_dung;
        this.ngay_tao = ngay_tao;
    }

    // Getter & Setter
    public int getMa_hoa_don() { return ma_hoa_don; }
    public void setMa_hoa_don(int ma_hoa_don) { this.ma_hoa_don = ma_hoa_don; }

    public String getNoi_dung() { return noi_dung; }
    public void setNoi_dung(String noi_dung) { this.noi_dung = noi_dung; }

    public String getNgay_tao() { return ngay_tao; }
    public void setNgay_tao(String ngay_tao) { this.ngay_tao = ngay_tao; }
}
