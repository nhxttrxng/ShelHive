package com.nhom5.shelhive.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiServiceWithNull {
    @PUT("/api/rooms/{ma_phong}")
    Call<ResponseBody> updateRoom(
            @Path("ma_phong") int maPhong,
            @Body Map<String, Object> request
    );
    public static ApiServiceWithNull apiServiceWithNull = RetrofitClientForNull.getRetrofitInstance().create(ApiServiceWithNull.class);
}

