package com.nhom5.shelhive.api;

public class ThongKeRentMoney {
    private int ma_day;
    private int month;
    private int year;
    private int total_unpaid_rent;
    private int total_paid_rent;

    public int getMa_day() {
        return ma_day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getTotal_unpaid_rent() {
        return total_unpaid_rent;
    }

    public int getTotal_paid_rent() {
        return total_paid_rent;
    }
}
