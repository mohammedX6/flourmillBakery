package com.flourmillco.flourmill_1.RecycleViews;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.Model.Wishlist;
import com.flourmillco.flourmill_1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewAdapterWishLists extends RecyclerView.Adapter<RecycleViewAdapterWishLists.MyViewHolder> {

    private Context myContext;
    private List<Wishlist> wishlist;


    public RecycleViewAdapterWishLists(Context myContext, List<Wishlist> wishlist) {
        this.myContext = myContext;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater MyInflater = LayoutInflater.from(myContext);
        view = MyInflater.inflate(R.layout.cart_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.BadgeName.setText("Badge Name: " + wishlist.get(position).getBadgename() + " ");
        holder.price.setText("Price: " + wishlist.get(position).getPrice() + "$");
        holder.usage.setText("Usage: " + "MultiPurpose");
        String imageUrl = wishlist.get(position).getUrl();
        Picasso.get().load(imageUrl).into(holder.Image);
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView BadgeName;
        TextView price;
        ImageView Image;
        TextView usage;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            BadgeName = itemView.findViewById(R.id.cart_prtitle);
            usage = itemView.findViewById(R.id.cart_usage);
            price = itemView.findViewById(R.id.cart_prprice);
            Image = itemView.findViewById(R.id.image_cartlist);


        }
    }
}
