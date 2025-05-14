package com.nhom5.shelhive.api;

public class CheckVerifyRequest {
    private String email;

    public CheckVerifyRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
