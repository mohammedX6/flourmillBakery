package com.flourmillco.flourmill_1.API_Calls;

import com.flourmillco.flourmill_1.Model.Wishlist;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WishlistClient {


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/api/Wishlist/getAll")
    Call<List<Wishlist>> getWishLists(@Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("/api/Wishlist/add")
    Call<ResponseBody> AddWishList(@Body Wishlist wishlist, @Header("Authorization") String auth);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("/api/Wishlist/{id}")
    Call<ResponseBody> deleteWishList(@Path("id") int id);

}
