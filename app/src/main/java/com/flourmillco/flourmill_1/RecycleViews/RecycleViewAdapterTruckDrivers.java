package com.flourmillco.flourmill_1.RecycleViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.Location.UserOrder;
import com.flourmillco.flourmill_1.R;

import java.util.List;

public class RecycleViewAdapterTruckDrivers extends RecyclerView.Adapter<RecycleViewAdapterTruckDrivers.MyViewHolder> {

    private Context myContext;
    private List<UserOrder> userOrders;

    public RecycleViewAdapterTruckDrivers(Context myContext, List<UserOrder> userLocations) {
        this.myContext = myContext;
        this.userOrders = userLocations;
    }


    @NonNull
    @Override
    public RecycleViewAdapterTruckDrivers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater MyInflater = LayoutInflater.from(myContext);
        view = MyInflater.inflate(R.layout.cart_item_layout_orderhistory, parent, false);
        return new RecycleViewAdapterTruckDrivers.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterTruckDrivers.MyViewHolder holder, int position) {
        holder.BillAmount.setText("Bill Amount: " + userOrders.get(position).getOrder().getTotalPayment() + "$");
        holder.ItemCount.setText("Total Tons: " + (int) userOrders.get(position).getOrder().getTotalTons());
        holder.OrderDate.setText("Order Date: " + userOrders.get(position).getOrder().getOrder_Date());

        holder.OrderStatues.setText("Order statues: " + OrderStatues(userOrders.get(position).getOrder().getOrderStatues()));

        holder.Destination.setText("Order Destination: " + userOrders.get(position).getOrder().getDestination());

    }

    private String OrderStatues(int statues) {
        String orderStatues = "";
        if (statues == 1) {
            orderStatues = "Order Waiting For Delivery";
        } else if (statues == 2) {
            orderStatues = "Order Currently Being Delivering ";
        }


        return orderStatues;

    }

    @Override
    public int getItemCount() {
        return userOrders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView BillAmount;
        TextView ItemCount;
        TextView OrderDate;
        TextView OrderStatues;
        TextView Destination;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            BillAmount = itemView.findViewById(R.id.billamount);
            ItemCount = itemView.findViewById(R.id.itemcount);
            OrderDate = itemView.findViewById(R.id.orderdate);
            OrderStatues = itemView.findViewById(R.id.orderstatues);
            Destination = itemView.findViewById(R.id.deliveryaddress);
        }
    }
}
