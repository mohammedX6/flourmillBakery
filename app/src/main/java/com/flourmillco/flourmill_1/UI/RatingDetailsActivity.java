package com.flourmillco.flourmill_1.UI;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.API_Calls.RatingClient;
import com.flourmillco.flourmill_1.Model.AllRates;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterRatings;

import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatingDetailsActivity extends AppCompatActivity {
    private List<AllRates> allrates;
    private RecyclerView r1;
    private LinearLayoutManager layoutManager;
    private Retrofit retrofit;
    private SharedPreferences pref3;
    private RatingClient ratingClient;
    private int productid2;
    private RecycleViewAdapterRatings recycleViewAdapterRatings;
    private int productid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_details);
        r1 = findViewById(R.id.recyclerview_idrating);
        productid = 1;
        productid2 = getIntent().getIntExtra("productidnew", 0);
        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging).
                        connectTimeout(5, TimeUnit.MINUTES).
                        writeTimeout(5, TimeUnit.MINUTES).
                        readTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://floutmill-jo.azurewebsites.net/").
                addConverterFactory(GsonConverterFactory.create()).client(client);
        retrofit = builder.build();
        ratingClient = retrofit.create(RatingClient.class);
        Call<List<AllRates>> call = ratingClient.getbakeryrates(productid2, "Bearer " + pref3.getString("token", "0"));
        call.enqueue(new Callback<List<AllRates>>() {
            @Override
            public void onResponse(Call<List<AllRates>> call, Response<List<AllRates>> response) {
                if (response.isSuccessful()) {

                    allrates = response.body();
                    recycleViewAdapterRatings = new RecycleViewAdapterRatings(getApplicationContext(), allrates);
                    r1.setLayoutManager(layoutManager);
                    r1.setAdapter(recycleViewAdapterRatings);
                    Toasty.success(getApplicationContext(), "Rating added", Toasty.LENGTH_LONG).show();


                } else {

                    Toasty.error(getApplicationContext(), "Rating not added", Toasty.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<List<AllRates>> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Error in connection", Toasty.LENGTH_LONG).show();
            }
        });


    }
}
