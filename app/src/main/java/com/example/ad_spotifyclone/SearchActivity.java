package com.example.ad_spotifyclone;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SearchActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private ImageButton startBtn, backBtn;
    private boolean isFinding = false;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Load the SearchFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SearchFragment())
                    .commit();
        }
        startBtn = findViewById(R.id.startBtn);
        backBtn = findViewById(R.id.backBtn);
        chronometer = findViewById(R.id.chronometer);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFinding){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    startBtn.setImageResource(R.drawable.ic_stop);
                    isFinding = true;
                } else {
                    chronometer.stop();
                    startBtn.setImageResource(R.drawable.ic_start);
                    isFinding = false;
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        chronometer.stop();
    }

}
