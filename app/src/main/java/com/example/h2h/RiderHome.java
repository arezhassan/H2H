package com.example.h2h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RiderHome extends AppCompatActivity {
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);
        init();
        getSupportFragmentManager().beginTransaction().replace(R.id.rider_fragment_container, new RiderConsignments()).commit();
        navigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.consignments:
                    getSupportFragmentManager().beginTransaction().replace(R.id.rider_fragment_container, new RiderConsignments()).commit();
                    return true;
                case R.id.account:
                    getSupportFragmentManager().beginTransaction().replace(R.id.rider_fragment_container, new RiderProfile()).commit();
                    return true;
            }
            return false;
        });
    }

    private void init() {
        navigation = findViewById(R.id.ridernavigation);
    }
}
