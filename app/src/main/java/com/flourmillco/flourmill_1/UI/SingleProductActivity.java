package com.flourmillco.flourmill_1.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flourmillco.flourmill_1.API_Calls.ProductClient;
import com.flourmillco.flourmill_1.API_Calls.RatingClient;
import com.flourmillco.flourmill_1.API_Calls.WishlistClient;
import com.flourmillco.flourmill_1.Model.Basket;
import com.flourmillco.flourmill_1.Model.OrderProducts;
import com.flourmillco.flourmill_1.Model.Product;
import com.flourmillco.flourmill_1.Model.ProductRate;
import com.flourmillco.flourmill_1.Model.Wishlist;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.SQLiteDatabase.BasketDB;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleProductActivity extends AppCompatActivity {
    private static int totaltons;
    @BindView(R.id.tons)
    EditText MyTons;
    private int tons_a = 1;
    private SharedPreferences pref;
    private SharedPreferences bascket;
    private SharedPreferences.Editor bascketEditor;
    private ImageView imageView;
    private ImageView imageView2;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private ProgressBar pr1;
    private ProgressBar pr2;
    private ProgressBar pr3;
    private ProgressBar pr4;
    private ProgressBar pr5;
    private RatingClient ratingClient;
    private TextView average;
    private double averagelist;
    private double total;
    private List<ProductRate> productRates;
    private TextView add;
    private TextView checkout;
    private SharedPreferences pref2;
    private SharedPreferences pref4;
    private RatingBar ratingBar;
    private SharedPreferences.Editor pref4editor;
    private BasketDB basketDB;
    private Basket basket;
    private int savedid;
    private int AdminID;
    private Product p;
    private Retrofit retrofit;
    private ProductClient productClient;
    private WishlistClient wishlistClient;
    private TextWatcher productcount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (MyTons.getText().toString().equals("")) {
                MyTons.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //none
            if (Integer.parseInt(MyTons.getText().toString()) >= 20) {
                Toasty.error(SingleProductActivity.this, "Total Tons Must Be Less Than 20", Toast.LENGTH_LONG).show();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);
        ButterKnife.bind(this);
        pref2 = getApplicationContext().getSharedPreferences("productid", MODE_PRIVATE);
        pref = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        pref4 = getApplicationContext().getSharedPreferences("productdata", MODE_PRIVATE);
        bascket = getApplicationContext().getSharedPreferences("bascket", MODE_PRIVATE);
        View test1View = findViewById(R.id.reviewids);
        bascketEditor = bascket.edit();
        pref4editor = pref4.edit();
        imageView2 = findViewById(R.id.imageclciks2);
        pr1 = test1View.findViewById(R.id.progress_1);
        pr2 = test1View.findViewById(R.id.progress_2);
        average = test1View.findViewById(R.id.ratingaverage);
        pr3 = test1View.findViewById(R.id.progress_3);
        pr4 = test1View.findViewById(R.id.progress_4);
        pr5 = test1View.findViewById(R.id.progress_5);
        ratingBar = findViewById(R.id.bar2);
        t1 = findViewById(R.id.badgename);
        t2 = findViewById(R.id.badgeprice);
        t3 = findViewById(R.id.productdate);
        t4 = findViewById(R.id.productdesc);
        t5 = findViewById(R.id.desct);
        add = findViewById(R.id.add_to_cart);
        checkout = findViewById(R.id.checkout);
        MyTons.setText("1");
        MyTons.addTextChangedListener(productcount);
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        // Toast.makeText(getApplicationContext(),"value of id single"+ bundle.getString("fmID2",""),Toast.LENGTH_LONG).show();
        imageView = findViewById(R.id.productimage);
        AdminID = bundle.getInt("fmID2", 0);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Intent intent = new Intent(getApplicationContext(), RatingActivity.class);
                intent.putExtra("ratingvalue", v);
                intent.putExtra("productid", p.getId());
                startActivity(intent);


            }
        });
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        basketDB = new BasketDB(getApplicationContext());

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging).
                        connectTimeout(5, TimeUnit.MINUTES).
                        writeTimeout(5, TimeUnit.MINUTES).
                        readTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://floutmill-jo.azurewebsites.net/").
                addConverterFactory(GsonConverterFactory.create()).client(client);
        retrofit = builder.build();
        productClient = retrofit.create(ProductClient.class);

        Call<JsonElement> call = productClient.getSingle(AdminID, pref2.getInt("myid", 1), "Bearer " + pref.getString("token", "0"));
//        Toast.makeText(getApplicationContext(),pref2.getInt("myid",1),Toast.LENGTH_LONG);


        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {


                    p = new Product(response.body().getAsJsonObject().get("url").getAsString(), response.body().getAsJsonObject().get("badgeName").getAsString(), response.body().getAsJsonObject().get("badgeType").getAsString(), response.body().getAsJsonObject().get("badgeSize").getAsString(),
                            response.body().getAsJsonObject().get("productionDate").getAsString(), response.body().getAsJsonObject().get("expireDate").getAsString(),
                            response.body().getAsJsonObject().get("usage").getAsString(), response.body().getAsJsonObject().get("productDescription").getAsString(),

                            Integer.parseInt(response.body().getAsJsonObject().get("price").getAsString()), Integer.parseInt(response.body().getAsJsonObject().get("id").getAsString()));


                    ratingClient = retrofit.create(RatingClient.class);
                    Call<List<ProductRate>> call2 = ratingClient.getrate(Integer.parseInt(response.body().getAsJsonObject().get("id").getAsString()), "Bearer " + pref.getString("token", "0"));
                    call2.enqueue(new Callback<List<ProductRate>>() {
                        @Override
                        public void onResponse(Call<List<ProductRate>> call, Response<List<ProductRate>> response) {
                            if (response.isSuccessful()) {

                                productRates = response.body();
                                averagelist = 0;
                                total = 0;
                                int[] star1 = new int[productRates.size()];
                                int counterstar = 0;
                                int[] star2 = new int[productRates.size()];
                                int counterstar2 = 0;
                                int[] star3 = new int[productRates.size()];
                                int counterstar3 = 0;
                                int[] star4 = new int[productRates.size()];
                                int counterstar4 = 0;
                                int[] star5 = new int[productRates.size()];
                                int counterstar5 = 0;

                                for (int i = 0; i < productRates.size(); i++) {
                                    total += productRates.get(i).getValue();

                                    if (productRates.get(i).getValue() == 1) {
                                        star1[i] = productRates.get(i).getValue();
                                        counterstar++;
                                    }
                                    if (productRates.get(i).getValue() == 2) {

                                        star2[i] = productRates.get(i).getValue();
                                        counterstar2++;
                                    }
                                    if (productRates.get(i).getValue() == 3) {

                                        star3[i] = productRates.get(i).getValue();
                                        counterstar3++;
                                    }
                                    if (productRates.get(i).getValue() == 4) {

                                        star4[i] = productRates.get(i).getValue();
                                        counterstar4++;
                                    }
                                    if (productRates.get(i).getValue() == 5) {
                                        star5[i] = productRates.get(i).getValue();
                                        counterstar5++;

                                    }
                                    Log.d("TAG", "...............value " + productRates.get(i).getValue());
                                }
                                Log.d("TAG", "...............star 1 " + star1.length + "counter " + counterstar);
                                Log.d("TAG", "...............star 2 " + star2.length + "counter " + counterstar2);
                                Log.d("TAG", "...............star 3 " + star3.length + "counter " + counterstar3);
                                Log.d("TAG", "...............star 4 " + star4.length + "counter " + counterstar4);
                                Log.d("TAG", "...............star 5 " + star5.length + "counter " + counterstar5);

                                pr1.setProgress(counterstar);
                                pr2.setProgress(counterstar2);
                                pr3.setProgress(counterstar3);
                                pr4.setProgress(counterstar4);
                                pr5.setProgress(counterstar5);

                                averagelist = total / productRates.size();
                                average.setText(averagelist + "");
                                //       Toasty.success(getApplicationContext(), "rates", Toasty.LENGTH_LONG).show();


                            } else {

                                //    Toasty.error(getApplicationContext(), "not rates", Toasty.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ProductRate>> call, Throwable t) {
                            Toasty.error(getApplicationContext(), "Error in connection", Toasty.LENGTH_LONG).show();
                        }
                    });

                    savedid = Integer.parseInt(response.body().getAsJsonObject().get("id").getAsString());

                    Log.d("TAG", "....................! " + savedid + "");
                    t1.setText(p.getBadgeName());
                    t2.setText("$. " + p.getPrice());
                    t3.setText(p.getProductionDate());
                    t4.setText(p.getUsage());
                    t5.setText(p.getProductDescription());
                    if (p.getUrl().isEmpty()) {
                        imageView.setImageResource(R.drawable.windmall);
                    } else {
                        Picasso.get().load(p.getUrl()).into(imageView);
                    }


                } else {
                    Log.d("TAG", response.code() + "");
                    //        Toasty.error(getApplicationContext(), "log in not correct", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }


        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totaltons = Integer.parseInt(MyTons.getText().toString());
                int totalkilo = totaltons * 1000;
                double totalbadges = Math.ceil(totaltons / 50);

                pref4editor.putInt("tons", totaltons);
                double totalprice = 50 * totaltons;


                if (basket == null) {
                    basket = new Basket(p.getId(), p.getBadgeName(), p.getUrl(), String.valueOf(totalbadges), " irbid", String.valueOf(totalprice));
                    basketDB.addBasket(basket);
                }


                HashMap<String, String> lastrecord = basketDB.getLastRecord();
                OrderProducts orderProducts = new OrderProducts(p.getId(), p.getBadgeName(), p.getPrice(), p.getUrl(), Integer.parseInt(lastrecord.get("BasketId")), totaltons);
                basketDB.addOrderProduct(orderProducts);
                Toasty.success(SingleProductActivity.this, "Product added to the basket with total of " + totaltons+ " Tons", Toast.LENGTH_LONG).show();

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivity(intent);
            }
        });


        pr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RatingDetailsActivity.class);
                intent.putExtra("productidnew", savedid);
                startActivity(intent);

            }
        });
        pr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingDetailsActivity.class);
                intent.putExtra("productidnew", savedid);
                startActivity(intent);

            }
        });
        pr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingDetailsActivity.class);
                intent.putExtra("productidnew", savedid);
                startActivity(intent);

            }
        });
        pr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingDetailsActivity.class);
                intent.putExtra("productidnew", savedid);
                startActivity(intent);

            }
        });
        pr5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RatingDetailsActivity.class);
                intent.putExtra("productidnew", savedid);
                startActivity(intent);
            }
        });

    }

    public void decrement(View view) {
        if (tons_a > 1) {
            tons_a--;
            MyTons.setText(String.valueOf(tons_a));
        }
    }

    public void increment(View view) {
        if (tons_a < 20) {
            tons_a++;
            MyTons.setText(String.valueOf(tons_a));
        } else {
            Toasty.error(SingleProductActivity.this, "Total Tons Must Be Less Than 20", Toast.LENGTH_LONG).show();
        }
    }

    public void shareProduct(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String ShareSubject = p.getBadgeName();
        String ShareBody = "من منتجات مطحنةالشمال";
        intent.putExtra(Intent.EXTRA_SUBJECT, ShareBody);
        intent.putExtra(Intent.EXTRA_TEXT, ShareSubject);
        startActivity(Intent.createChooser(intent, "Share using"));

    }

    public void addToWishList(View view) {


        Wishlist wishlist = GenerateWishList();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://floutmill-jo.azurewebsites.net/").
                addConverterFactory(GsonConverterFactory.create()).client(client);
        retrofit = builder.build();
        wishlistClient = retrofit.create(WishlistClient.class);

        Call<ResponseBody> call = wishlistClient.AddWishList(wishlist, pref.getString("token", "0"));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Toasty.success(getApplicationContext(), "Wishlist added", Toasty.LENGTH_LONG).show();
                } else
                    Toasty.error(getApplicationContext(), "Wishlist not added", Toasty.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Error in call", Toasty.LENGTH_LONG).show();
            }
        });


    }

    private Wishlist GenerateWishList() {
        Wishlist wishlist = new Wishlist();
        wishlist.setAdminID(AdminID);
        wishlist.setBadgename(p.getBadgeName());
        wishlist.setBakeryId(Integer.parseInt(pref.getString("nameid", "0")));
        wishlist.setUrl(p.getUrl());
        wishlist.setProductid(p.getId());
        wishlist.setPrice(p.getPrice());
        return wishlist;
    }


}
