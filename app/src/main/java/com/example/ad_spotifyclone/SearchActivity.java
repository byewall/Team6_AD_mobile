package com.example.ad_spotifyclone;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String taskNumber = intent.getStringExtra("taskNumber");
        int modelNumber = intent.getIntExtra("modelNumber", 0);
        long startTime = intent.getLongExtra("startTime",0);
        // Create a Bundle and put the userId and taskNumber values
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("taskNumber", taskNumber);
        bundle.putInt("modelNumber",modelNumber);
        bundle.putLong("startTime",startTime);

        // Load the SearchFragment
        if (savedInstanceState == null) {
            // Load the SearchFragment and set the arguments
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, searchFragment)
                    .commit();
        }
    }
}
