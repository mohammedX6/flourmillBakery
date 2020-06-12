package com.flourmillco.flourmill_1.RecycleViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flourmillco.flourmill_1.Model.FlourMills;
import com.flourmillco.flourmill_1.R;

import java.util.List;

public class RecycleViewAdapterFlourMills extends RecyclerView.Adapter<RecycleViewAdapterFlourMills.MyViewHolder1> {

    private Context context;
    private List<FlourMills> FlourMillList;

    public RecycleViewAdapterFlourMills(Context context, List<FlourMills> flourMills) {
        this.context = context;
        FlourMillList = flourMills;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view;
        LayoutInflater MyInflater2 = LayoutInflater.from(context);
        view = MyInflater2.inflate(R.layout.cart_item_layout, viewGroup, false);

        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 myViewHolder1, int i) {

        myViewHolder1.t1.setText("Flour Mill: " + FlourMillList.get(i).getUsername());
        myViewHolder1.t2.setText("Flour Mill ID: " + FlourMillList.get(i).getId());
        myViewHolder1.t3.setText("Phone Number: " + FlourMillList.get(i).getPhoneNumber());
        myViewHolder1.t4.setText("Email: " + FlourMillList.get(i).getEmail());
    }

    @Override
    public int getItemCount() {
        return FlourMillList.size();
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {

        TextView t1, t2, t3, t4;

        ImageView i1;

        MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.cart_prtitle);
            t2 = itemView.findViewById(R.id.cart_usage);
            t3 = itemView.findViewById(R.id.cart_prprice);
            t4 = itemView.findViewById(R.id.tonsusage);
            i1 = itemView.findViewById(R.id.image_cartlist);

        }
    }
}
