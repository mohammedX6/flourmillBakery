package com.flourmillco.flourmill_1.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.API_Calls.OrderClient;
import com.flourmillco.flourmill_1.Model.OrderProducts;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterOrderHistoryDetails;

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

public class OrderHistoryDetailsActivity extends AppCompatActivity {


    LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private RecycleViewAdapterOrderHistoryDetails historyDetailsAdapter;
    private Retrofit retrofit;
    private OrderClient orderClient;
    private SharedPreferences pref3;
    private LinearLayoutManager layoutManager;
    private List<OrderProducts> orderList;
    private int OrderId;
    private TextView payment;
    private TextView tons;
    private TextView dateT;
    private TextView statuesT;
    private TextView destinationT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        OrderId = bundle.getInt("orderId", 0);
        String totalPayment = bundle.getString("payment");
        String totaltons = bundle.getString("tons");
        String date = bundle.getString("date");
        String statues = bundle.getString("statues");
        String destination = bundle.getString("destination");
        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerview91);
        payment = findViewById(R.id.billamount2);
        tons = findViewById(R.id.itemcount2);
        dateT = findViewById(R.id.orderdate2);
        statuesT = findViewById(R.id.orderstatues2);
        destinationT = findViewById(R.id.deliveryaddress2);
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


        Call<List<OrderProducts>> call = orderClient.getAllOrdersDetail(OrderId, "Bearer " + token);

        call.enqueue(new Callback<List<OrderProducts>>() {
            @Override
            public void onResponse(Call<List<OrderProducts>> call, Response<List<OrderProducts>> response) {

                if (response.isSuccessful()) {
                    orderList = response.body();
                    historyDetailsAdapter = new RecycleViewAdapterOrderHistoryDetails(getApplicationContext(), orderList);
                    layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(historyDetailsAdapter);
                    //  Toasty.success(getApplicationContext(), "order detail  loaded", Toast.LENGTH_LONG).show();

                } else {
                    Log.d("TAG", response.code() + "");
                    //    Toasty.error(getApplicationContext(), "order detail not  loaded", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<List<OrderProducts>> call, Throwable t) {

                Toasty.error(getApplicationContext(), "Error in Connection", Toast.LENGTH_LONG).show();

            }
        });

        payment.setText("Bill Amount: " + totalPayment + "$");
        tons.setText("Total Tons: " + totaltons);
        dateT.setText("Order Date: " + date);
        statuesT.setText("Order statues: " + OrderStatues(Integer.parseInt(statues)));
        destinationT.setText("Order Destination: " + destination);

    }

    private String OrderStatues(int statues) {
        String orderStatues;
        if (statues == 0) {
            orderStatues = "In Progress";
        } else {
            orderStatues = "Delivered";
        }
        return orderStatues;

    }
}
