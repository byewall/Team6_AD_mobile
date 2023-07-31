package com.example.ad_spotifyclone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private ImageButton startBtn, backBtn;
    private boolean isChronometerRunning = false;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Load the SearchFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
        startBtn = findViewById(R.id.startBtn);
        backBtn = findViewById(R.id.backBtn);
        chronometer = findViewById(R.id.chronometer);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                startBtn.setImageResource(R.drawable.ic_start);
                isChronometerRunning = false;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homeBtn){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            .commit();
                    return true;
                }
                if (item.getItemId() == R.id.searchBtn){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SearchFragment())
                            .commit();
                    return true;
                }
                if (item.getItemId() == R.id.historyBtn) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HistoryFragment())
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
    public void startChronometer() {
        if (!isChronometerRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            startBtn.setImageResource(R.drawable.ic_stop);
            isChronometerRunning = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chronometer.stop();
    }

}
