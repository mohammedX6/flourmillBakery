package com.flourmillco.flourmill_1.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flourmillco.flourmill_1.API_Calls.GeoLocationClient;
import com.flourmillco.flourmill_1.API_Calls.OrderClient;
import com.flourmillco.flourmill_1.CallBacks.FirestoreCallBackFlourMill;
import com.flourmillco.flourmill_1.Location.FlourMillsLocation;
import com.flourmillco.flourmill_1.Model.FullOrder;
import com.flourmillco.flourmill_1.Model.Order;
import com.flourmillco.flourmill_1.Model.OrderProducts;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.SQLiteDatabase.BasketDB;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonElement;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailsActivity extends AppCompatActivity {
    public static final double TAX = 16;
    public static final double TAX_PERCENTAGE = 100;
    public static final double DIESEL_PRICE = 0.615;
    private static final String TAG = "OrderDetailsSActivity";
    double TotalShipment = 0;
    GoogleMap googleMap;
    int myid;
    LatLngBounds Boundery;
    GeoLocationClient geoLocationClient;
    String latitude;
    String longtude;
    double DistanceBFlourMillABakery = 0;
    double TimeRequired = 0;
    double totalorder = 0;
    private ImageView imageView;
    private ArrayList<HashMap<String, String>> BasketList;
    private ArrayList<HashMap<String, String>> OrderProductsList;
    private LinearLayoutManager layoutManager;
    private TextView T1;
    private TextView T2;
    private TextView T3;
    private BasketDB basketDB;
    private SharedPreferences pref3;
    private SharedPreferences pref5;
    private Retrofit retrofit;
    private OrderClient orderClient;
    private SharedPreferences pref4;
    private SharedPreferences pref6;
    private LinearLayout linearLayout;
    private OrderProducts orderProducts;
    private List<OrderProducts> orderList;
    private Order order;
    private double FullOrder = 0;
    private int totaltons = 0;
    private TextView nameca;
    private TextView mobile;
    private TextView locationc;
    private EditText comment;
    private GeoApiContext geoApiContext = null;
    private FirebaseFirestore mDB;
    private ArrayList<FlourMillsLocation> FlourMills = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_details);
        imageView = findViewById(R.id.imageclcik);
        nameca = findViewById(R.id.namecard);
        mobile = findViewById(R.id.mobile);
        locationc = findViewById(R.id.location);
        comment = findViewById(R.id.comment);
        linearLayout = findViewById(R.id.linerclick);
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
        geoLocationClient = retrofit.create(GeoLocationClient.class);

        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_map_api_key)).build();

        }
        pref4 = getApplicationContext().getSharedPreferences("productdata", MODE_PRIVATE);
        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        pref5 = getApplicationContext().getSharedPreferences("rating", MODE_PRIVATE);
        pref6 = getApplicationContext().getSharedPreferences("userinfo", MODE_PRIVATE);

        nameca.setText(pref3.getString("name", ""));
        mobile.setText(pref6.getString("phone", "0"));
        locationc.setText(pref6.getString("destination", ""));
        mDB = FirebaseFirestore.getInstance();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        basketDB = new BasketDB(getApplicationContext());
        BasketList = basketDB.getBaskets();
        OrderProductsList = basketDB.getOrderProducts();
        T1 = findViewById(R.id.total_amountU);
        T2 = findViewById(R.id.chargestotal);
        T3 = findViewById(R.id.totalwithcharges);


        double[] arr = new double[OrderProductsList.size()];

        for (int i = 0; i < OrderProductsList.size(); i++) {

            totalorder += Double.parseDouble(OrderProductsList.get(i).get("price")) * Double.parseDouble(OrderProductsList.get(i).get("tons"));
            totaltons += Double.parseDouble(OrderProductsList.get(i).get("tons"));
            arr[i] = totalorder;

        }

        for (int i = 0; i < arr.length; i++) {
            FullOrder += arr[i];

        }


        String token = pref3.getString("token", " ");
        Call<JsonElement> jsonElementCall = geoLocationClient.getGeoLocation(Integer.parseInt(pref3.getString("nameid", "0")), "Bearer " + token);
        jsonElementCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {

                    latitude = response.body().getAsJsonObject().get("lati").getAsString();
                    longtude = response.body().getAsJsonObject().get("longi").getAsString();
                    // Toasty.success(getApplicationContext(), latitude + " " + longtude, Toasty.LENGTH_LONG).show();
                    Log.d("suci", latitude + " " + longtude);

                } else {
                    //     Toasty.error(getApplicationContext(), latitude + " " + longtude, Toasty.LENGTH_LONG).show();


                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Error in conenction", Toasty.LENGTH_LONG).show();


            }
        });

        CalculateDirections();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                T1.setText(totalorder + "$");


                Double Precision = BigDecimal.valueOf(CalculateTotalPayment())
                        .setScale(3, RoundingMode.HALF_UP)
                        .doubleValue();


                T2.setText("Included");

                double total =totalorder;

                T3.setText(total + "$");


            }
        }, 2000);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!BasketList.isEmpty()) {

                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    orderList = new ArrayList<>();

                    order = new Order();

                    order.setCustomerName(pref3.getString("name", "defualt"));

                    order.setTotalPayment(totalorder);
                    order.setDestination(pref6.getString("destination", ""));
                    order.setTotalTons(totaltons);
                    String comm;
                    if (comment.getText() != null) {
                        comm = comment.getText().toString();
                    } else {
                        comm = "no comment";
                    }
                    order.setOrderComment(comm);
                    order.setAdministratorID(pref5.getInt("adminid", 0));
                    order.setBakeryID(Integer.parseInt(pref3.getString("nameid", "0")));
                    order.setTruckDriverID(1);
                    order.setOrderStatues(0);
                    order.setOrder_Date(date);
                    order.setShipmentPrice((int) Math.round((CalculateTotalPayment())));

                }

                if (!(OrderProductsList.isEmpty())) {


                    FullOrder fullOrder = new FullOrder(order, orderList);

                    Call<JsonElement> call = orderClient.putOrderOnly(order);
                    call.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                            String temp = response.body().getAsJsonObject().get("id").getAsString();
                            temp = temp.replaceAll("[^\\d.]", "");
                            myid = Integer.parseInt(temp);

                            if (!(OrderProductsList.isEmpty())) {
                                for (int j = 0; j < OrderProductsList.size(); j++) {
                                    orderProducts = new OrderProducts();
                                    orderProducts.setBadgeName(OrderProductsList.get(j).get("Badge"));
                                    orderProducts.setUrl(OrderProductsList.get(j).get("pic"));
                                    orderProducts.setPrice(Integer.parseInt(OrderProductsList.get(j).get("price")));
                                    orderProducts.setTons(Integer.parseInt(OrderProductsList.get(j).get("tons")));
                                    orderProducts.setOrderId(myid);
                                    orderList.add(orderProducts);
                                }
                            }

                            Call<JsonElement> call1 = orderClient.putOrdersProducts(orderList);
                            call1.enqueue(new Callback<JsonElement>() {
                                @Override
                                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                                }

                                @Override
                                public void onFailure(Call<JsonElement> call, Throwable t) {

                                }
                            });


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                public void run() {
                                }
                            }, 2000);
                            Intent intent = new Intent(getApplicationContext(), OrderPlacedActivity.class);
                            intent.putExtra("myid", temp);
                            startActivity(intent);
                            finish();


                        }


                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {

                        }


                    });


                } else {
                    Toasty.error(getApplicationContext(), "Sorry The Basket Is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivity(myIntent);
                return true;
            }
        });


    }

    void ReadDataFlourMills(FirestoreCallBackFlourMill firestoreCallBackFlourMill) {

        CollectionReference collectionReference = mDB.collection("flourmill_location");
        collectionReference.whereEqualTo(FieldPath.of("user", "id"), 15)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            FlourMillsLocation flourMillsLocation = queryDocumentSnapshot.toObject(FlourMillsLocation.class);
                            FlourMills.add(flourMillsLocation);
                            Log.d("mnb", flourMillsLocation.getLocation().getLatitude() + " ");
                            Log.d("mnb", flourMillsLocation.getUser().getId() + " ");

                        }
                        firestoreCallBackFlourMill.onCallBack2(FlourMills);

                    }
                });
    }


    private double CalculateTotalPayment() {

        double Load_Unload = totaltons * 5;
        double Cargo = FullOrder;
        double FuelRequired = DIESEL_PRICE * DistanceBFlourMillABakery;
        double CaclualteTime = TimeRequired * 0.0006;


        TotalShipment = Load_Unload + Cargo + FuelRequired + CaclualteTime;
        double TotalWithTax = TAX / TotalShipment;
        double temp = TAX / TotalShipment;
        TotalWithTax *= 100;


        Log.d("totalds", "Load_Unload " + Load_Unload);
        Log.d("totalds", "Cargo " + Cargo);
        Log.d("totalds", "FuelRequired " + FuelRequired);
        Log.d("totalds", "CaclualteTime " + CaclualteTime);
        Log.d("totalds", "TotalShipment " + TotalShipment);
        Log.d("totalds", "DistanceBFlourMillABakery " + DistanceBFlourMillABakery);
        Log.d("totalds", "TimeRequired " + TimeRequired);
        Log.d("totalds", "((TAX * 100) / 100) " + ((TAX * 100) / 100));
        Log.d("totalds", "temp " + temp);



        return TotalWithTax;
    }


    private void CalculateDirections() {
        Log.d("calds", "calleddirections");

        ReadDataFlourMills(new FirestoreCallBackFlourMill() {
            @Override
            public void onCallBack2(List<FlourMillsLocation> k) {
                for (FlourMillsLocation flourMillsLocation : k) {
                    Log.d("csss", k.size() + " ");
                    Log.d("calds", "Calculatedireaction");

                    Log.d("csss", flourMillsLocation.getLocation().getLatitude() + "");
                    Log.d("csss", Double.parseDouble(latitude) + " ");
                    Log.d("totalloc", "Origin" + " " + Double.parseDouble(latitude) + " " + Double.parseDouble(longtude) + " Destination " + flourMillsLocation.getLocation().getLatitude() + " " + flourMillsLocation.getLocation().getLongitude());

                    Log.d("csss", Double.parseDouble(longtude) + " ");
                    com.google.maps.model.LatLng mydestination = new com.google.maps.model.LatLng(flourMillsLocation.getLocation().getLatitude(), flourMillsLocation.getLocation().getLongitude());


                    DirectionsApiRequest directionsApiRequest = new DirectionsApiRequest(geoApiContext);
                    directionsApiRequest.alternatives(true);

                    directionsApiRequest.origin(
                            new com.google.maps.model.LatLng(
                                    Double.parseDouble(latitude),
                                    Double.parseDouble(longtude)
                            )
                    );
                    //  directionsApiRequest.w;
                    Log.d("pol", "Calcualtedirection destination" + mydestination.toString());
                    directionsApiRequest.destination(mydestination).setCallback(new PendingResult.Callback<DirectionsResult>() {
                        @Override
                        public void onResult(DirectionsResult result) {
                            String distance = result.routes[0].legs[0].distance.toString();
                            String time = String.valueOf(result.routes[0].legs[0].duration.inSeconds);
                            distance = distance.replaceAll("[^\\d.]", "");
                            time = time.replaceAll("[^\\d.]", "");

                            DistanceBFlourMillABakery = Double.parseDouble(distance);
                            TimeRequired = Double.parseDouble(time);
                            Log.d("sw", "DistanceBFlourMillABakery " + DistanceBFlourMillABakery);
                            Log.d("sw", "TimeRequired " + TimeRequired);
                            Log.d("vb", "calculateDirections: route " + result.routes[0].toString());
                            Log.d("vb", "calculateDirections: duration " + result.routes[0].legs[0].duration);
                            Log.d("vb", "calculateDirections: distance " + result.routes[0].legs[0].distance);
                            Log.d("vb", "calculateDirections: geocodedwaypoints " + result.geocodedWaypoints.toString());

                        }

                        @Override
                        public void onFailure(Throwable e) {
                            Log.d("ersds", "calculateDirections: failyer " + e.getMessage());


                        }
                    });
                }
            }
        });

    }
}

