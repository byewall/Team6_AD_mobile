package com.example.ad_spotifyclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class HomeActivity extends AppCompatActivity {

    private EditText editTextUserId;
    private EditText editTextTaskNumber;
    private Button buttonModel1;
    private Button buttonModel2;
    private Button buttonSubmitLogFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        editTextUserId = findViewById(R.id.editTextUserId);
        editTextTaskNumber = findViewById(R.id.editTextTaskNumber);
        buttonModel1 = findViewById(R.id.btnModel1);
        buttonModel2 = findViewById(R.id.btnModel2);
        buttonSubmitLogFile = findViewById(R.id.btnSubmit);

        buttonModel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    logInteraction("Model 1");
                    Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            }
        });

        buttonModel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    logInteraction("Model 2");
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
            Toast.makeText(this, "User ID and Task ID cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void submitLogFile() {
        // Code to submit the log file goes here.
        // This function will be called when the "Submit Log File" button is clicked.
        // Implement the logic to send the log file to your desired server or save it for later analysis.
        saveLogFile();
    }

    private void saveLogFile(){
        // save the log file to the app storage as a txt file first
        // then later submit the log file to the server
        String userId = editTextUserId.getText().toString();
        String taskNumber = editTextTaskNumber.getText().toString();
        String logFileName = "logUser" + userId + "Task" + taskNumber + ".txt";

        File cacheDir = getExternalCacheDir();
        File logFile = new File(cacheDir.getAbsolutePath(), logFileName);
        try {
            // Runtime.getRuntime().exec("logcat -f " + logFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("AppLog") && line.contains(userId) && line.contains(taskNumber)) {
                    log.append(line);
                    log.append("\n");
                }
            }
            FileWriter writer = new FileWriter(logFile);
            writer.write(log.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
