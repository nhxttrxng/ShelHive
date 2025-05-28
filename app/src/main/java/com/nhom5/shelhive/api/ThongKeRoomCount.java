package com.nhom5.shelhive.api;

public class ThongKeRoomCount {
    private int ma_day;
    private int month;
    private int year;
    private int paid_room_count;
    private int overdue_room_count;
    private int unpaid_room_count;

    public int getMa_day() {
        return ma_day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getPaid_room_count() {
        return paid_room_count;
    }

    public int getOverdue_room_count() {
        return overdue_room_count;
    }

    public int getUnpaid_room_count() {
        return unpaid_room_count;
    }
}

