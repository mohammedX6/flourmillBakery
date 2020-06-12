package com.flourmillco.flourmill_1.API_Calls;

import com.flourmillco.flourmill_1.Model.FlourMills;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface BakeryClient {

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/BakeryProducts/get_all")
    Call<List<FlourMills>> getAllFlourMills(@Header("Authorization") String auth);
}
