package com.flourmillco.flourmill_1.API_Calls;

import com.flourmillco.flourmill_1.Model.FullOrder;
import com.flourmillco.flourmill_1.Model.Order;
import com.flourmillco.flourmill_1.Model.OrderProducts;
import com.google.gson.JsonElement;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderClient {


    @POST("/api/Orders/addOrder_full")
    Call<JsonElement> putOrders(@Body FullOrder full);

    @POST("/api/Orders/addOrderProducts_only")
    Call<JsonElement> putOrdersProducts(@Body List<OrderProducts> full);

    @POST("/api/Orders/addOrder_only")
    Call<JsonElement> putOrderOnly(@Body Order full);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/Orders/getall/{id}")
    Call<List<Order>> getAllReports(@Path("id") int id, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/Orders/GetOrderDetail/{id}")
    Call<List<OrderProducts>> getAllOrdersDetail(@Path("id") int id, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/Orders/GetOrderId/{id}")
    Call<ResponseBody> getOrderId(@Path("id") int id, @Header("Authorization") String auth);

}
