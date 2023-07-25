package com.example.ad_spotifyclone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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
                logInteraction("Model 1");
            }
        });

        buttonModel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInteraction("Model 2");
            }
        });

        buttonSubmitLogFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitLogFile();
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

    private void submitLogFile() {
        // Code to submit the log file goes here.
        // This function will be called when the "Submit Log File" button is clicked.
        // Implement the logic to send the log file to your desired server or save it for later analysis.
    }
}
