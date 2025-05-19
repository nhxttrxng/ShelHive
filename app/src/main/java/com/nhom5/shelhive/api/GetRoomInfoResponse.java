package com.nhom5.shelhive.api;

import com.nhom5.shelhive.ui.model.Room2;
import com.nhom5.shelhive.ui.model.User;
import com.google.gson.annotations.SerializedName;

public class GetRoomInfoResponse {
    @SerializedName("room")
    private Room2 room;

    @SerializedName("user")
    private User user;  // Có thể là null nếu phòng chưa có người thuê

    public Room2 getRoom() {
        return room;
    }

    public void setRoom(Room2 room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
