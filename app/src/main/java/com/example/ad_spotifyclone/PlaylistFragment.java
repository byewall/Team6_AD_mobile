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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlaylistFragment extends Fragment {

    View view;

    private String userId;

    private String taskNumber;

    private int modelNumber;

    private long startTime;

    private long duration;

    private int success;

    Button foundButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_playlist,container,false);

        // Get the arguments from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
            taskNumber = bundle.getString("taskNumber");
            modelNumber = bundle.getInt("modelNumber");
            startTime = bundle.getLong("startTime");
        }

        foundButton = view.findViewById(R.id.foundBtn);
        foundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                success = 1;
                logInteraction("Song Found");
                long endTime = System.currentTimeMillis();
                duration = endTime - startTime;
                // logInteraction("Time Stopped. Duration: " + endTime);
                saveLogFile();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }
    private void saveLogFile(){
        // save the log file to the app storage as a txt file first
        // then later submit the log file to the server
        String logFileName = "user" + userId + ".txt";
        File cacheDir = getContext().getExternalCacheDir();
        File logFile = new File(cacheDir.getAbsolutePath(), logFileName);
        try {
            // Retrieve new log messages
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("AppLog") && line.contains(userId) && line.contains(taskNumber)) {
                    String[] parts = line.split("Action: ");
                    log.append(parts[1]);
                    log.append("|");
                }
            }
            FileWriter writer;
            if(logFile.exists()){
                writer = new FileWriter(logFile, true);
                writer.append("\n");
            } else {
                writer = new FileWriter(logFile);
                // writer.write("taskid, model no, duration (milliseconds), success, history");
                writer.append("\n");
            }
            writer.write(taskNumber + "," + modelNumber + "," + duration + "," + success + ",");
            writer.append(log.toString().substring(0, log.length() - 1));
            writer.close();
            // Clear the log buffer
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void logInteraction(String action) {

        //edit later with start and stop time also
        String logMessage = "User ID: " + userId + ", Task Number: " + taskNumber + ", Action: " + action;

        //Using Logcat for logging.
        Log.d("AppLog", logMessage);
    }
}
