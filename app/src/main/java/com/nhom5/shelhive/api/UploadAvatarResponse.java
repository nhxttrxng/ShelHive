package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;

public class UploadAvatarResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("path")
    private String path;

    public String getMessage() {
        return message;
    }
    public String getPath() {
        return path;
    }
}
