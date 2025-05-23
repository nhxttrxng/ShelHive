package com.nhom5.shelhive.ui.model;

import android.graphics.Color;
import com.google.gson.annotations.SerializedName;

public class Room {
    private String roomNumber;
    private String tenantName;
    private int unpaidBills;
    private RoomStatus status;
    private int overdueCount;
    @SerializedName("trang_thai")
    private String trangThai;

    public enum RoomStatus {
        PAID("Đã đóng", "#2E8B57", "ic_room_green"),
        OVERDUE("Trễ hạn", "#D70000", "ic_room_red"),
        UNPAID("Chưa đóng", "#755200", "ic_room_brown");

        private final String label;
        private final String color;
        private final String iconName;

        RoomStatus(String label, String color, String iconName) {
            this.label = label;
            this.color = color;
            this.iconName = iconName;
        }

        public String getLabel() {
            return label;
        }

        public String getColor() {
            return color;
        }

        public String getIconName() {
            return iconName;
        }
    }

    public Room(String roomNumber, String tenantName, int unpaidBills, RoomStatus status, int overdueCount) {
        this.roomNumber = roomNumber;
        this.tenantName = tenantName;
        this.unpaidBills = unpaidBills;
        this.status = status;
        this.overdueCount = overdueCount;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public int getUnpaidBills() {
        return unpaidBills;
    }

    public void setUnpaidBills(int unpaidBills) {
        this.unpaidBills = unpaidBills;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public int getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(int overdueCount) {
        this.overdueCount = overdueCount;
    }
}