package com.flourmillco.flourmill_1.RecycleViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.Model.Product;
import com.flourmillco.flourmill_1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewAdapterProducts extends RecyclerView.Adapter<RecycleViewAdapterProducts.MyViewHolder> {
    private Context myContext;
    private List<Product> ProductList;

    public RecycleViewAdapterProducts(Context mContext, List<Product> productList) {
        this.myContext = mContext;
        this.ProductList = productList;
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

        myViewHolder.TextName.setText("Badge Name: " + ProductList.get(i).getBadgeName() + " ");
        myViewHolder.price.setText("Price: " + ProductList.get(i).getPrice() + "$");
        myViewHolder.usage.setText("Usage: " + ProductList.get(i).getUsage() + " ");
        String imageUrl = ProductList.get(i).getUrl();
        Picasso.get().load(imageUrl).into(myViewHolder.Image);

    }

    @Override
    public int getItemCount() {
        return ProductList.size();
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
