package com.example.ad_spotifyclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {
    private EditText editTextUserId, editTextTaskNumber;
    private Button buttonModel1, buttonModel2, buttonSubmitLogFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        editTextUserId = v.findViewById(R.id.editTextUserId );
        editTextTaskNumber = v.findViewById(R.id.editTextTaskNumber);
        buttonModel1 = v.findViewById(R.id.btnModel1);
        buttonModel2 = v.findViewById(R.id.btnModel2);
        buttonSubmitLogFile = v.findViewById(R.id.btnSubmit);

        buttonModel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    logInteraction("Model 1");
                    startSearch();
                }
            }
        });

        buttonModel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    logInteraction("Model 2");
                    startSearch();
                }
            }
        });

        buttonSubmitLogFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    submitLogFile();
                }
            }
        });
        return v;
    }

    private void startSearch(){
        // Replace HomeFragment with SearchFragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new SearchFragment())
                .commit();
        ((HomeActivity)getActivity()).startChronometer();
        // Switch to 'Search' tab in BottomNavigationView
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.searchBtn);
    }

    private void logInteraction(String action) {
        String userId = editTextUserId.getText().toString();
        String taskNumber = editTextTaskNumber.getText().toString();

        //edit later with start and stop time also
        String logMessage = "User ID: " + userId + ", Task Number: " + taskNumber + ", Action: " + action;

        //Using Logcat for logging.
        Log.d("AppLog", logMessage);
    }

    private boolean validateInputs() {
        String userId = editTextUserId.getText().toString();
        String taskNumber = editTextTaskNumber.getText().toString();

        if (userId.isEmpty() || taskNumber.isEmpty()) {
            Toast.makeText(getActivity(), "User ID and Task ID cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void submitLogFile() {
        // Code to submit the log file goes here.
        // This function will be called when the "Submit Log File" button is clicked.
        // Implement the logic to send the log file to your desired server or save it for later analysis.
    }
}