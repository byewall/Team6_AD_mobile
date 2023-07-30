package com.example.ad_spotifyclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    View view;

    String userId;

    String taskNumber;

    private Button popHitsButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_result,container,false);

        // Get the arguments from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
            taskNumber = bundle.getString("taskNumber");
        }

        popHitsButton = view.findViewById(R.id.popHitsBtn);
        popHitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInteraction("Pop Hits");
                Intent intent = new Intent(getActivity(),PlaylistActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("taskNumber", taskNumber);
                startActivity(intent);
            }
        });

        return view;

    }

    private void logInteraction(String action) {

        //edit later with start and stop time also
        String logMessage = "User ID: " + userId + ", Task Number: " + taskNumber + ", Action: " + action;

        //Using Logcat for logging.
        Log.d("AppLog", logMessage);
    }
}
