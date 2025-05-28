package com.nhom5.shelhive.api;


public class ThongKeDienResponse {
    private int ma_phong;
    private String month;
    private String year;
    private String electric_money;

    public ThongKeDienResponse(int ma_phong, String month, String year, String electric_money) {
        this.ma_phong = ma_phong;
        this.month = month;
        this.year = year;
        this.electric_money = electric_money;
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

    public String getElectric_money() {
        return electric_money;
    }

    public void setElectric_money(String electric_money) {
        this.electric_money = electric_money;
    }
}