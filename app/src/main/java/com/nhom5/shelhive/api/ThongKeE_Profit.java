package com.nhom5.shelhive.api;

public class ThongKeE_Profit {
    private int ma_day;
    private String month;
    private String year;
    private String electric_profit;

    public ThongKeE_Profit(int ma_day, String month, String year, String electric_profit) {
        this.ma_day = ma_day;
        this.month = month;
        this.year = year;
        this.electric_profit= electric_profit;
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

    public String getElectric_profit() {
        return electric_profit;
    }

    public void setElectric_profit(String electric_profit){
        this.electric_profit = electric_profit;
    }
}
