package com.flourmillco.flourmill_1.UI.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.flourmillco.flourmill_1.API_Calls.WishlistClient;
import com.flourmillco.flourmill_1.Model.Wishlist;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.RecycleViews.RecycleViewAdapterWishLists;
import com.flourmillco.flourmill_1.UI.SingleProductActivity;

import java.util.List;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment {

    private List<Wishlist> wishlistList;
    private RecycleViewAdapterWishLists recycleViewAdapterWishLists;
    private Retrofit retrofit;
    private RecyclerView r1;
    private WishlistClient wishlistClient;
    private LinearLayoutManager layoutManager;
    private SharedPreferences pref;
    private Bundle bundle2;
    private SharedPreferences pref2;
    private SharedPreferences.Editor editor2;

    public WishlistFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getActivity().getSharedPreferences("secretcode", MODE_PRIVATE);
        pref2 = getActivity().getSharedPreferences("productid", MODE_PRIVATE);
        editor2 = pref2.edit();
        r1 = getActivity().findViewById(R.id.recyclerview_id17);
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
        wishlistClient = retrofit.create(WishlistClient.class);

        Call<List<Wishlist>> call = wishlistClient.getWishLists("Bearer " + pref.getString("token", "0"));

        call.enqueue(new Callback<List<Wishlist>>() {
            @Override
            public void onResponse(Call<List<Wishlist>> call, Response<List<Wishlist>> response) {
                if (response.isSuccessful()) {
                    wishlistList = response.body();
                    recycleViewAdapterWishLists = new RecycleViewAdapterWishLists(getContext(), wishlistList);
                    r1.setAdapter(recycleViewAdapterWishLists);
                    //  Toasty.success(getContext(), "Wishlist added", Toasty.LENGTH_LONG).show();
                } else {
                    Toasty.error(getContext(), "Wishlist not added", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Wishlist>> call, Throwable t) {
                Toasty.error(getContext(), "Error In Connection", Toasty.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        r1 = view.findViewById(R.id.recyclerview_id17);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        r1.setLayoutManager(layoutManager);


        r1.setAdapter(recycleViewAdapterWishLists);
        RecycleClick.addTo(r1).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {


                bundle2 = new Bundle();

                editor2.putInt("myid", wishlistList.get(position).getProductid());
                editor2.apply();

                bundle2.putInt("fmID2", wishlistList.get(position).getAdminID());
                //     Toasty.success(getContext(), "Test ", Toasty.LENGTH_LONG).show();

                Intent myIntent = new Intent(getContext(), SingleProductActivity.class);
                myIntent.putExtras(bundle2);
                startActivity(myIntent);


            }
        });
        RecycleClick.addTo(r1).setOnItemLongClickListener(new RecycleClick.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Remove Product From WishList");
                builder.setMessage("Are you sure you want to delete the WishList ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.level(HttpLoggingInterceptor.Level.BODY);

                        OkHttpClient client = new OkHttpClient.Builder()
                                .addInterceptor(logging)
                                .build();

                        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://floutmill-jo.azurewebsites.net/").
                                addConverterFactory(GsonConverterFactory.create()).client(client);
                        retrofit = builder.build();
                        wishlistClient = retrofit.create(WishlistClient.class);

                        Call<ResponseBody> call = wishlistClient.deleteWishList(wishlistList.get(position).getId());

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {

                                    Toasty.success(getContext(), "Item removed", Toasty.LENGTH_LONG).show();


                                } else {
                                    Toasty.error(getContext(), "Item not removed", Toasty.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toasty.error(getContext(), "Error in connection", Toasty.LENGTH_LONG).show();
                            }
                        });


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
        return view;
    }

}
