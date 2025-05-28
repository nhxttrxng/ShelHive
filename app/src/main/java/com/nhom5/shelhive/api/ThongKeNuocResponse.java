package com.nhom5.shelhive.api;

public class ThongKeNuocResponse {
    private int ma_phong;
    private String month;
    private String year;
    private String water_money;

    public ThongKeNuocResponse(int ma_phong, String month, String year, String water_money) {
        this.ma_phong = ma_phong;
        this.month = month;
        this.year = year;
        this.water_money = water_money;
    }

    public int getMa_phong() {
        return ma_phong;
    }

    public void setMa_phong(int ma_phong) {
        this.ma_phong = ma_phong;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getWater_money() {
        return water_money;
    }

    public void setWater_money(String water_money) {
        this.water_money = water_money;
    }
}