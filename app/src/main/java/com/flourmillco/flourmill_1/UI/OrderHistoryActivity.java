package com.flourmillco.flourmill_1.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.chootdev.recycleclick.RecycleClick;
import com.flourmillco.flourmill_1.API_Calls.OrderClient;
import com.flourmillco.flourmill_1.Model.Order;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterOrderHistory;

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

public class OrderHistoryActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private RecycleViewAdapterOrderHistory orderHistoryAdapter;
    private Retrofit retrofit;
    private OrderClient orderClient;
    private SharedPreferences pref3;
    private LinearLayoutManager layoutManager;
    private List<Order> orderList;
    private Bundle bundle2;
    private LottieAnimationView emptycart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history);
        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerview90);
        emptycart = findViewById(R.id.empty_cart2);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        int bakeryId = Integer.parseInt(pref3.getString("nameid", " "));
        String token = pref3.getString("token", " ");
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
        orderClient = retrofit.create(OrderClient.class);

        try {
            Call<List<Order>> call = orderClient.getAllReports(bakeryId, "Bearer " + token);
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful()) {
                        orderList = response.body();
                        if (orderList.isEmpty()) {
                            Toasty.success(getApplicationContext(), "Sorry you didn't have any orders yet. ", Toast.LENGTH_SHORT).show();
                            emptycart.setVisibility(View.VISIBLE);

                        } else {

                            //  Toasty.success(getApplicationContext(), "Orders history Loaded ", Toast.LENGTH_SHORT).show();
                            orderHistoryAdapter = new RecycleViewAdapterOrderHistory(getApplicationContext(), orderList);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(orderHistoryAdapter);

                        }

                    }
                }


                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Toasty.error(getApplicationContext(), "Error in connection  ", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {

        }
        RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                bundle2 = new Bundle();

                bundle2.putInt("orderId", orderList.get(position).getId());
                bundle2.putString("payment", String.valueOf(orderList.get(position).getTotalPayment()));
                bundle2.putString("tons", String.valueOf(orderList.get(position).getTotalTons()));
                bundle2.putString("date", String.valueOf(orderList.get(position).getOrder_Date()));
                bundle2.putString("statues", String.valueOf(orderList.get(position).getOrderStatues()));
                bundle2.putString("destination", String.valueOf(orderList.get(position).getDestination()));

                Intent myIntent = new Intent(getApplicationContext(), OrderHistoryDetailsActivity.class);
                myIntent.putExtras(bundle2);
                startActivity(myIntent);
            }
        });

    }
}
