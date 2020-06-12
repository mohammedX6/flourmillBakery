package com.flourmillco.flourmill_1.API_Calls;

import com.flourmillco.flourmill_1.Model.AllRates;
import com.flourmillco.flourmill_1.Model.ProductRate;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RatingClient {
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/ProductRates/add_rate")
    Call<ResponseBody> putRate(@Body ProductRate rate, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/ProductRates/getBakeryRate/{pid}")
    Call<List<ProductRate>> getrate(@Path("pid") int productid, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/ProductRates/getusersrates/{pid}")
    Call<List<AllRates>> getbakeryrates(@Path("pid") int productid, @Header("Authorization") String auth);
}
