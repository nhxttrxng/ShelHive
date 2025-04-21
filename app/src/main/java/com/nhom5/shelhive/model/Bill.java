package com.nhom5.shelhive.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Bill {
    private String id;
    private String month;
    private String dueDate;
    private double amount;
    private boolean isPaid;
    private String roomId;
    private String roomName;
    private boolean isExtension;
    private String name;

    public Bill() {
    }

    public Bill(String id, String month, String dueDate, double amount, boolean isPaid, String roomId, String roomName, boolean isExtension) {
        this.id = id;
        this.month = month;
        this.dueDate = dueDate;
        this.amount = amount;
        this.isPaid = isPaid;
        this.roomId = roomId;
        this.roomName = roomName;
        this.isExtension = isExtension;
        this.name = "Tiền phòng " + month;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
        // Cập nhật tên hóa đơn khi thay đổi tháng
        this.name = "Tiền phòng " + month;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isExtension() {
        return isExtension;
    }

    public void setExtension(boolean extension) {
        isExtension = extension;
    }
    
    public String getName() {
        if (name == null || name.isEmpty()) {
            return "Tiền phòng " + (month != null ? month : "");
        }
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isOverdue() {
        if (isPaid) {
            return false; // Hóa đơn đã thanh toán không quá hạn
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dueDateTime = dateFormat.parse(dueDate);
            Date currentDate = new Date();
            return dueDateTime != null && currentDate.after(dueDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Date getDueDateAsDate() {
        if (dueDate == null || dueDate.isEmpty()) {
            return null;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
} 