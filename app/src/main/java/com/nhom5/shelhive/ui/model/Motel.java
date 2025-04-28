package com.nhom5.shelhive.ui.model;

public class Motel {
    private String name;
    private String address;

    public Motel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
