package com.flourmillco.flourmill_1.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flourmillco.flourmill_1.API_Calls.RatingClient;
import com.flourmillco.flourmill_1.Model.ProductRate;
import com.flourmillco.flourmill_1.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatingActivity extends AppCompatActivity {

    double averagelist;
    double total;
    List<ProductRate> productRates;
    private RatingBar ratingBar;
    private Button rateb;
    private EditText editText;
    private TextView average;
    private String date;
    private float ratingvalue2;
    private String ratingtext;
    private SharedPreferences pref3;
    private SharedPreferences adminidForRating;
    private Retrofit retrofit;
    private ProgressBar pr1;
    private ProgressBar pr2;
    private ProgressBar pr3;
    private ProgressBar pr4;
    private ProgressBar pr5;
    private RatingClient ratingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ratingBar = findViewById(R.id.ratingbar);
        average = findViewById(R.id.ratingaverage);
        rateb = findViewById(R.id.ratebutton);


        pr1 = findViewById(R.id.progress_1);
        pr2 = findViewById(R.id.progress_2);
        pr3 = findViewById(R.id.progress_3);
        pr4 = findViewById(R.id.progress_4);
        pr5 = findViewById(R.id.progress_5);
        float ratingvalue = getIntent().getFloatExtra("ratingvalue", 0);
        int productid = getIntent().getIntExtra("productid", 0);
        ratingBar.setRating(ratingvalue);
        editText = findViewById(R.id.reviewtext);
        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        adminidForRating = getApplicationContext().getSharedPreferences("rating", MODE_PRIVATE);
        ratingvalue2 = ratingBar.getRating();

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
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
        //Toasty.success(getApplicationContext(), "value " + adminidForRating.getInt("adminid", 0), Toasty.LENGTH_LONG).show();
        rateb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ratingtext = editText.getText().toString();
                ProductRate productRate = new ProductRate();
                productRate.setValue(Math.round(ratingvalue));
                productRate.setRateDate(date);
                productRate.setRateText(ratingtext);
                productRate.setAdministratorID(adminidForRating.getInt("adminid", 0));
                productRate.setBakeryID(Integer.parseInt(pref3.getString("nameid", "0")));
                productRate.setProductID(productid);
                Call<ResponseBody> call = ratingClient.putRate(productRate, "Bearer " + pref3.getString("token", "0"));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {


                            Toasty.success(getApplicationContext(), "Rating added", Toasty.LENGTH_LONG).show();


                        } else {

                            Toasty.error(getApplicationContext(), "Rating not added", Toasty.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "Error in connection", Toasty.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}
