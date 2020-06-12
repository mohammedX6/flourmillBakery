package com.flourmillco.flourmill_1.API_Calls;

import com.flourmillco.flourmill_1.Model.Bakery;
import com.flourmillco.flourmill_1.Model.Login;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserClient {

    @POST("/api/BakeryAuth/bakery_login")
    Call<JsonElement> login(@Body Login login);

    @POST("/api/BakeryAuth/bakery_register")
    Call<JsonElement> register(@Body Bakery bakery);


}
