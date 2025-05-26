package com.nhom5.shelhive.api;

import java.util.List;

public class ThongBaoResponse1 {
    private boolean success;
    private List<ThongBaoDonGian> data;

    public boolean isSuccess() {
        return success;
    }

    public List<ThongBaoDonGian> getData() {
        return data;
    }
}
