package com.example.ad_spotifyclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String taskNumber = intent.getStringExtra("taskNumber");
        // Create a Bundle and put the userId and taskNumber values
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("taskNumber", taskNumber);

        // Load the PlayListFragment
        if (savedInstanceState == null) {
            PlaylistFragment playlistFragment = new PlaylistFragment();
            playlistFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.playlistContainer, playlistFragment)
                    .commit();
        }
    }
}