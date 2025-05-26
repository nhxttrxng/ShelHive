package com.nhom5.shelhive.ui.model;

import com.google.gson.annotations.SerializedName;
import com.nhom5.shelhive.api.GetBillByRoomResponse;
import java.util.ArrayList;
import java.util.List;

public class Bill {

    @SerializedName("ma_hoa_don")
    private int id;

    @SerializedName("ma_phong")
    private int roomId;

    @SerializedName("tong_tien")
    private double amount;

    @SerializedName("so_dien")
    private int electricityUsed;

    @SerializedName("so_nuoc")
    private int waterUsed;

    @SerializedName("han_dong_tien")
    private String dueDate;

    @SerializedName("trang_thai")
    private String status;

    @SerializedName("da_duyet_gia_han")
    private boolean extensionApproved;

    @SerializedName("ngay_gia_han")
    private String extendedDueDate;

    @SerializedName("chi_so_dien_cu")
    private int electricityOldIndex;

    @SerializedName("chi_so_dien_moi")
    private int electricityNewIndex;

    @SerializedName("chi_so_nuoc_cu")
    private int waterOldIndex;

    @SerializedName("chi_so_nuoc_moi")
    private int waterNewIndex;

    @SerializedName("tien_dien")
    private double electricityAmount;

    @SerializedName("tien_nuoc")
    private double waterAmount;

    @SerializedName("tien_phong")
    private double roomAmount;

    @SerializedName("ngay_tao")
    private String createdAt;

    @SerializedName("tien_lai_gia_han")
    private double extensionFee;

    @SerializedName("so_ngay_gia_han")
    private int extensionDays;

    @SerializedName("ngay_cap_nhat_gia_han")
    private String extensionUpdateDate;

    @SerializedName("ngay_thanh_toan")
    private String paymentDate;

    @SerializedName("thang_nam")
    private String billMonthYear;

    // ==== GETTER & SETTER ====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getElectricityUsed() { return electricityUsed; }
    public void setElectricityUsed(int electricityUsed) { this.electricityUsed = electricityUsed; }

    public int getWaterUsed() { return waterUsed; }
    public void setWaterUsed(int waterUsed) { this.waterUsed = waterUsed; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isExtensionApproved() { return extensionApproved; }
    public void setExtensionApproved(boolean extensionApproved) { this.extensionApproved = extensionApproved; }

    public String getExtendedDueDate() { return extendedDueDate; }
    public void setExtendedDueDate(String extendedDueDate) { this.extendedDueDate = extendedDueDate; }

    public int getElectricityOldIndex() { return electricityOldIndex; }
    public void setElectricityOldIndex(int electricityOldIndex) { this.electricityOldIndex = electricityOldIndex; }

    public int getElectricityNewIndex() { return electricityNewIndex; }
    public void setElectricityNewIndex(int electricityNewIndex) { this.electricityNewIndex = electricityNewIndex; }

    public int getWaterOldIndex() { return waterOldIndex; }
    public void setWaterOldIndex(int waterOldIndex) { this.waterOldIndex = waterOldIndex; }

    public int getWaterNewIndex() { return waterNewIndex; }
    public void setWaterNewIndex(int waterNewIndex) { this.waterNewIndex = waterNewIndex; }

    public double getElectricityAmount() { return electricityAmount; }
    public void setElectricityAmount(double electricityAmount) { this.electricityAmount = electricityAmount; }

    public double getWaterAmount() { return waterAmount; }
    public void setWaterAmount(double waterAmount) { this.waterAmount = waterAmount; }

    public double getRoomAmount() { return roomAmount; }
    public void setRoomAmount(double roomAmount) { this.roomAmount = roomAmount; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public double getExtensionFee() { return extensionFee; }
    public void setExtensionFee(double extensionFee) { this.extensionFee = extensionFee; }

    public int getExtensionDays() { return extensionDays; }
    public void setExtensionDays(int extensionDays) { this.extensionDays = extensionDays; }

    public String getExtensionUpdateDate() { return extensionUpdateDate; }
    public void setExtensionUpdateDate(String extensionUpdateDate) { this.extensionUpdateDate = extensionUpdateDate; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getBillMonthYear() { return billMonthYear; }
    public void setBillMonthYear(String billMonthYear) { this.billMonthYear = billMonthYear; }

    // ==== CONVERTER FROM GetBillByRoomResponse ====
    public static Bill fromGetBillByRoomResponse(GetBillByRoomResponse res) {
        Bill bill = new Bill();
        bill.setId(res.getMaHoaDon());
        bill.setRoomId(res.getMaPhong());
        bill.setAmount(parseDouble(res.getTongTien()));
        bill.setElectricityUsed(res.getSoDien());
        bill.setWaterUsed(res.getSoNuoc());
        bill.setDueDate(res.getHanDongTien());
        bill.setStatus(res.getTrangThai());
        bill.setExtensionApproved(res.isDaDuyetGiaHan());
        bill.setExtendedDueDate(res.getNgayGiaHan());
        bill.setElectricityOldIndex(res.getChiSoDienCu());
        bill.setElectricityNewIndex(res.getChiSoDienMoi());
        bill.setWaterOldIndex(res.getChiSoNuocCu());
        bill.setWaterNewIndex(res.getChiSoNuocMoi());
        bill.setElectricityAmount(parseDouble(res.getTienDien()));
        bill.setWaterAmount(parseDouble(res.getTienNuoc()));
        bill.setRoomAmount(parseDouble(res.getTienPhong()));
        bill.setCreatedAt(res.getNgayTao());
        bill.setExtensionFee(parseDouble(res.getTienLaiGiaHan()));
        bill.setExtensionDays(res.getSoNgayGiaHan());
        bill.setExtensionUpdateDate(res.getNgayCapNhatGiaHan());
        bill.setPaymentDate(res.getNgayThanhToan());
        bill.setBillMonthYear(res.getThangNam());
        return bill;
    }

    public static List<Bill> fromGetBillByRoomResponseList(List<GetBillByRoomResponse> src) {
        List<Bill> result = new ArrayList<>();
        if (src != null) {
            for (GetBillByRoomResponse item : src) {
                result.add(fromGetBillByRoomResponse(item));
            }
        }
        return result;
    }

    private static double parseDouble(String s) {
        try {
            return s == null ? 0 : Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
