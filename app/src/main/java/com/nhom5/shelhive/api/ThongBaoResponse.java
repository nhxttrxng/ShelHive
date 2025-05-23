package com.nhom5.shelhive.api;

import com.google.gson.annotations.SerializedName;
import com.nhom5.shelhive.ui.model.ThongBao;
import java.util.List;

public class ThongBaoResponse {
    @SerializedName("notifications")
    private List<ThongBao> notifications;

    public List<ThongBao> getNotifications() {
        return notifications;
    }
}
