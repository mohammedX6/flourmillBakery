package com.flourmillco.flourmill_1.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.UI.Fragments.FlourMillFragment;
import com.flourmillco.flourmill_1.UI.Fragments.HomeFragment;
import com.flourmillco.flourmill_1.UI.Fragments.UserFragment;
import com.flourmillco.flourmill_1.UI.Fragments.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView v1;
    private Fragment selected;
    private FrameLayout framelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);




        Objects.requireNonNull(getSupportActionBar()).hide();
        v1 = findViewById(R.id.navigationView);
        framelay = findViewById(R.id.frame1);
        selected = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1, selected).commit();
        v1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.track: {
                        selected = new HomeFragment();
                        break;
                    }
                    case R.id.Order: {
                        selected = new FlourMillFragment();
                        break;
                    }
                    case R.id.wishlist: {
                        selected = new WishlistFragment();
                        break;
                    }
                    case R.id.account: {
                        selected = new UserFragment();
                        break;
                    }
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame1, selected).commit();

                return true;
            }
        });
    }

}

