package com.flourmillco.flourmill_1.UI.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.flourmillco.flourmill_1.API_Calls.BakeryClient;
import com.flourmillco.flourmill_1.Model.FlourMills;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterFlourMills;
import com.flourmillco.flourmill_1.SQLiteDatabase.BasketDB;
import com.flourmillco.flourmill_1.UI.BasketActivity;

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

public class FlourMillFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Retrofit retrofit;
    private ImageView imageView4;
    private RecyclerView r2;
    private LinearLayoutManager layoutManager;
    private BakeryClient bakeryClient;
    private List<FlourMills> flourMills;
    private RecycleViewAdapterFlourMills recycleViewAdapterFlourMills;
    private SharedPreferences pref3;
    private SharedPreferences adminidForRating;
    private SharedPreferences.Editor adminidForRatingediter;
    private Fragment selected;
    private Bundle bundle;
    private SharedPreferences pref2;
    private SharedPreferences.Editor editor2;


    public FlourMillFragment() {

    }

    public static FlourMillFragment newInstance(String param1, String param2) {
        FlourMillFragment fragment = new FlourMillFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BasketDB basketDB = new BasketDB(getContext());
        basketDB.deleteall();


        r2 = getActivity().findViewById(R.id.recyclerview_id4);
        imageView4 = getActivity().findViewById(R.id.hiImage);
        pref3 = getContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        pref2 = getActivity().getSharedPreferences("productwishlist", MODE_PRIVATE);

        adminidForRating = getActivity().getSharedPreferences("rating", MODE_PRIVATE);
        adminidForRatingediter = adminidForRating.edit();

        editor2 = pref2.edit();

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
        bakeryClient = retrofit.create(BakeryClient.class);
        Call<List<FlourMills>> call2 = bakeryClient.getAllFlourMills("Bearer " + pref3.getString("token", ""));

        call2.enqueue(new Callback<List<FlourMills>>() {
            @Override
            public void onResponse(Call<List<FlourMills>> call, Response<List<FlourMills>> response) {
                if (response.isSuccessful()) {
                    flourMills = response.body();
                    recycleViewAdapterFlourMills = new RecycleViewAdapterFlourMills((getContext()), flourMills);
                    r2.setAdapter(recycleViewAdapterFlourMills);

                }

            }

            @Override
            public void onFailure(Call<List<FlourMills>> call, Throwable t) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v1 = inflater.inflate(R.layout.fragment_flour_mill, container, false);
        r2 = v1.findViewById(R.id.recyclerview_id4);
        imageView4 = v1.findViewById(R.id.hiImage);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        r2.setLayoutManager(layoutManager);
        r2.setAdapter(recycleViewAdapterFlourMills);
        bundle = new Bundle();

        RecycleClick.addTo(r2).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                selected = new ProductFragment();
                bundle.putInt("fmID", flourMills.get(position).getId());
                adminidForRatingediter.putInt("adminid", flourMills.get(position).getId());
                adminidForRatingediter.commit();
                selected.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame1, selected).commit();

            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BasketActivity.class);
                startActivity(intent);
            }
        });


        return v1;
    }

}
