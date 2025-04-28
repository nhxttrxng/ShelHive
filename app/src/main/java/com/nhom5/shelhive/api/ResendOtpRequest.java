package com.nhom5.shelhive.api;

public class ResendOtpRequest {
    private String email;

    public ResendOtpRequest(String email) {
        this.email = email;
    }

    // Getter và Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
