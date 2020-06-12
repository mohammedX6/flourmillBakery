package com.flourmillco.flourmill_1.UI.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.flourmillco.flourmill_1.API_Calls.ProductClient;
import com.flourmillco.flourmill_1.Model.Product;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterProducts;
import com.flourmillco.flourmill_1.UI.BasketActivity;
import com.flourmillco.flourmill_1.UI.SingleProductActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecycleViewAdapterProducts myFoodsAdapter;
    SharedPreferences pref;
    SharedPreferences pref2;
    SharedPreferences.Editor editor2;
    List<Product> Produc;
    RecyclerView r1;
    LinearLayoutManager layoutManager;
    Retrofit retrofit;
    ProductClient productClient;
    int AdminID;
    Bundle bundle2;
    ImageView imageView;


    String mParam1;
    String mParam2;


    public ProductFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getActivity().getSharedPreferences("secretcode", MODE_PRIVATE);
        pref2 = getActivity().getSharedPreferences("productid", MODE_PRIVATE);
        editor2 = pref2.edit();
        r1 = getActivity().findViewById(R.id.recyclerview_id4);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            AdminID = bundle.getInt("fmID", 0);
        }
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
        productClient = retrofit.create(ProductClient.class);

        Call<List<Product>> call = productClient.getProducts(AdminID, "Bearer " + pref.getString("token", "0"));

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    Produc = response.body();
                    myFoodsAdapter = new RecycleViewAdapterProducts(getContext(), Produc);
                    r1.setAdapter(myFoodsAdapter);
                    Log.d("TAG", response.code() + "");

                } else {
                    Log.d("TAG", response.code() + "");
                    Toast.makeText(getContext(), "Product Not Loaded", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("TAG", t.getMessage() + "");

                Toast.makeText(getContext(), "Error In Request", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        View v1 = inflater.inflate(R.layout.fragment_product, container, false);
        r1 = v1.findViewById(R.id.recyclerview_id4);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        imageView = v1.findViewById(R.id.hiffs);

        r1.setLayoutManager(layoutManager);


        r1.setAdapter(myFoodsAdapter);

        RecycleClick.addTo(r1).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                bundle2 = new Bundle();
                editor2.putInt("myid", Produc.get(position).getId());
                editor2.apply();
                bundle2.putInt("fmID2", AdminID);
                Intent myIntent = new Intent(getContext(), SingleProductActivity.class);
                myIntent.putExtras(bundle2);
                startActivity(myIntent);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BasketActivity.class);
                startActivity(intent);
            }
        });


        return v1;
    }


}
