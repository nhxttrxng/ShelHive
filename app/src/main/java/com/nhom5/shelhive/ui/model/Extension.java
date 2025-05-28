package com.nhom5.shelhive.ui.model;

import com.google.gson.annotations.SerializedName;

public class Extension {
    @SerializedName("ma_gia_han")
    private int id;

    @SerializedName("ma_hoa_don")
    private int billId;

    @SerializedName("han_dong_tien_goc")
    private String originalDueDate;   // ISO 8601 string

    @SerializedName("han_thanh_toan_moi")
    private String newDueDate;        // ISO 8601 string

    @SerializedName("lai_suat")
    private double interestRate;      // có thể là String, nhưng nên để double để code dễ dùng

    @SerializedName("tien_lai_tinh_du_kien")
    private double expectedInterest;

    @SerializedName("trang_thai")
    private String status;            // "chờ xác nhận", "đã duyệt", v.v.

    public Extension() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public String getOriginalDueDate() { return originalDueDate; }
    public void setOriginalDueDate(String originalDueDate) { this.originalDueDate = originalDueDate; }

    public String getNewDueDate() { return newDueDate; }
    public void setNewDueDate(String newDueDate) { this.newDueDate = newDueDate; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getExpectedInterest() { return expectedInterest; }
    public void setExpectedInterest(double expectedInterest) { this.expectedInterest = expectedInterest; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
