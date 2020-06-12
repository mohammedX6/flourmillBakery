package com.flourmillco.flourmill_1.RecycleViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.Model.AllRates;
import com.flourmillco.flourmill_1.R;

import java.util.List;

public class RecycleViewAdapterRatings extends RecyclerView.Adapter<RecycleViewAdapterRatings.MyViewHolder> {

    private Context myContext;
    private List<AllRates> orders;


    public RecycleViewAdapterRatings(Context myContext, List<AllRates> orders) {
        this.myContext = myContext;
        this.orders = orders;
    }

    @NonNull
    @Override
    public RecycleViewAdapterRatings.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater MyInflater = LayoutInflater.from(myContext);
        view = MyInflater.inflate(R.layout.card_stars_layout, parent, false);
        return new RecycleViewAdapterRatings.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterRatings.MyViewHolder holder, int position) {
        holder.Username.setText(orders.get(position).getUsername());
        holder.ratingBar.setRating(orders.get(position).getValue());
        holder.ratedate.setText(orders.get(position).getRateDate());
        holder.description.setText(orders.get(position).getRateText());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Username;
        RatingBar ratingBar;
        TextView ratedate;
        TextView description;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Username = itemView.findViewById(R.id.usernamerating);
            ratingBar = itemView.findViewById(R.id.ratingbarnew);
            ratedate = itemView.findViewById(R.id.ratedate);
            description = itemView.findViewById(R.id.descriptionrating);
        }
    }
}
