package com.nhom5.shelhive.model;

public class Motel {
    private String name;
    private String address;
    private int badgeCount;

    public Motel(String name, String address, int badgeCount) {
        this.name = name;
        this.address = address;
        this.badgeCount = badgeCount;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getBadgeCount() {
        return badgeCount;
    }
}
