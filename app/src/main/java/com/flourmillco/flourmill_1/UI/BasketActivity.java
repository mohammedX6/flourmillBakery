package com.flourmillco.flourmill_1.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.chootdev.recycleclick.RecycleClick;
import com.flourmillco.flourmill_1.API_Calls.OrderClient;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterBasket;
import com.flourmillco.flourmill_1.SQLiteDatabase.BasketDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketActivity extends AppCompatActivity {

    Button b1;
    private RecycleViewAdapterBasket recycleViewAdapterBasket;
    private ArrayList<HashMap<String, String>> BasketList;
    private ArrayList<HashMap<String, String>> OrderProductsList;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private TextView b11;
    private BasketDB basketDB;

    private SharedPreferences pref3;
    private Retrofit retrofit;
    private OrderClient orderClient;
    private SharedPreferences pref4;
    //private LottieAnimationView tv_no_item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView = findViewById(R.id.recyclerview96);
        LottieAnimationView emptycart;
        b11 = findViewById(R.id.text_action_bottom2);
        emptycart = findViewById(R.id.empty_cart);
        pref4 = getApplicationContext().getSharedPreferences("productdata", MODE_PRIVATE);

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

        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        basketDB = new BasketDB(getApplicationContext());

        BasketList = basketDB.getBaskets();

        OrderProductsList = basketDB.getOrderProducts();

        if (OrderProductsList.isEmpty()) {
            emptycart.setVisibility(View.VISIBLE);

        } else {

            populuteCards();

        }

        b11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(OrderProductsList.isEmpty())) {

                    Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toasty.error(BasketActivity.this, "Sorry the Basket is empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        RecycleClick.addTo(recyclerView).setOnItemLongClickListener(new RecycleClick.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, final int position, View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
                builder.setTitle("Remove Product From Basket");
                builder.setMessage("Are you sure you want to delete the product ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        basketDB.deleteItem2(OrderProductsList.get(position).get("id"));
                        Intent intent = new Intent(BasketActivity.this, BasketActivity.class);
                        startActivity(intent);
                        //   Toast.makeText(BasketActivity.this, "Removed" + listIterator, Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
                return true;
            }
        });
    }


    private void populuteCards() {

        recycleViewAdapterBasket = new RecycleViewAdapterBasket(getApplicationContext(), OrderProductsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleViewAdapterBasket);

    }
}