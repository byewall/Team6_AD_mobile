package com.example.ad_spotifyclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String taskNumber = intent.getStringExtra("taskNumber");
        // Create a Bundle and put the userId and taskNumber values
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("taskNumber", taskNumber);

        // Load the ResultFragment
        if (savedInstanceState == null) {
            ResultFragment resultFragment = new ResultFragment();
            resultFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.resultContainer, resultFragment)
                    .commit();
        }
    }
}