package com.flourmillco.flourmill_1.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.flourmillco.flourmill_1.R;


public class OrderPlacedActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        textView = findViewById(R.id.orderid);
        String myid = getIntent().getStringExtra("myid");
        myid = myid.replaceAll("[^\\d.]", "");



        textView.setText(myid + "");


    }
}
