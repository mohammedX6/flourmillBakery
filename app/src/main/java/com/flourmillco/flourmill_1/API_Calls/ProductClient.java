package com.flourmillco.flourmill_1.API_Calls;

import com.flourmillco.flourmill_1.Model.Product;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ProductClient {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/BakeryProducts/{id}")
    Call<List<Product>> getProducts(@Path("id") int nameid, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/BakeryProducts/{id}/{pid}")
    Call<JsonElement> getSingle(@Path("id") int id, @Path("pid") int produid, @Header("Authorization") String auth);


}
