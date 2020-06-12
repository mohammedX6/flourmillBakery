package com.flourmillco.flourmill_1.RecycleViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class RecycleViewAdapterBasket extends RecyclerView.Adapter<RecycleViewAdapterBasket.MyViewHolder> {
    private ArrayList<HashMap<String, String>> baksetlist;
    private Context myContext;

    public RecycleViewAdapterBasket(Context mContext, ArrayList<HashMap<String, String>> baksetlist) {
        this.myContext = mContext;
        this.baksetlist = baksetlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater MyInflater = LayoutInflater.from(myContext);
        view = MyInflater.inflate(R.layout.cart_item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.TextName.setText("Badge Name: " + baksetlist.get(i).get("Badge"));
        myViewHolder.price.setText("Price: " + baksetlist.get(i).get("price") + "$");
        myViewHolder.usage.setText("Usage: " + "MultiPurpose");
        String imageUrl = baksetlist.get(i).get("pic");
        Picasso.get().load(imageUrl).into(myViewHolder.Image);


    }

    @Override
    public int getItemCount() {
        return baksetlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TextName;
        TextView usage;
        TextView price;
        ImageView Image;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TextName = itemView.findViewById(R.id.cart_prtitle);
            usage = itemView.findViewById(R.id.cart_usage);
            price = itemView.findViewById(R.id.cart_prprice);
            Image = itemView.findViewById(R.id.image_cartlist);
        }
    }
}
