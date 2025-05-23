package com.nhom5.shelhive.api;

public class PhanAnhResponse {
    private int maPhanAnh;
    private String tieuDe;
    private String noiDung;
    private String emailNguoiGui;
    private String trangThai;
    private String ngayTao;

    // Getter, Setter...

    public int getMaPhanAnh() { return maPhanAnh; }
    public void setMaPhanAnh(int maPhanAnh) { this.maPhanAnh = maPhanAnh; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getEmailNguoiGui() { return emailNguoiGui; }
    public void setEmailNguoiGui(String emailNguoiGui) { this.emailNguoiGui = emailNguoiGui; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }
}
