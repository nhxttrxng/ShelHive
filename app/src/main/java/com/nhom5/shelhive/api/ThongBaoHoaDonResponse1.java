package com.nhom5.shelhive.api;

import java.util.List;

public class ThongBaoHoaDonResponse1 {
    private boolean success;
    private List<ThongBaoHDDonGian> data;

    public boolean isSuccess() {
        return success;
    }

    public List<ThongBaoHDDonGian> getData() {
        return data;
    }
}
