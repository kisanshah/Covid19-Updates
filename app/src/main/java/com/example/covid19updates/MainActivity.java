package com.example.covid19updates;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    RelativeLayout mainContainer;
    BottomNavigationView bottomNavigationView;
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        mainContainer = findViewById(R.id.mainContainer);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.countryData);

    }


    AllCountry allCountry = new AllCountry();
    Country country = new Country();
    Global global = new Global();
    State state = new State();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.allData:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, allCountry).commit();
                return true;
            case R.id.countryData:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, country).commit();
                return true;
            case R.id.globalData:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, global).commit();
                return true;
            case R.id.stateData:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, state).commit();
                return true;

        }
        return true;
    }

}
