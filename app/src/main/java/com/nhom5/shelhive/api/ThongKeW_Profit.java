package com.nhom5.shelhive.api;

public class ThongKeW_Profit {
    private int ma_day;
    private String month;
    private String year;
    private String water_profit;

    public ThongKeW_Profit(int ma_day, String month, String year, String water_profit) {
        this.ma_day = ma_day;
        this.month = month;
        this.year = year;
        this.water_profit= water_profit;
    }

    public int getMa_day() {
        return ma_day;
    }

    public void setMa_day(int ma_day) {
        this.ma_day = ma_day;
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

    public String getWater_profit() {
        return water_profit;
    }

    public void setWater_profit(String water_profit){
        this.water_profit = water_profit;
    }
}
