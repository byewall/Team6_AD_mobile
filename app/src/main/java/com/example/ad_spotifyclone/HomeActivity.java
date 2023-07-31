package com.example.ad_spotifyclone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

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

    String filename;
    private void submitLogFile() {
        // Code to submit the log file goes here.
        // This function will be called when the "Submit Log File" button is clicked.
        // Implement the logic to send the log file to your desired server or save it for later analysis.
        createDummyLogFile();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
                    String LINE_FEED = "\r\n";

                    URL url = new URL("http://172.188.27.126:8080/logs/upload");
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setUseCaches(false);
                    httpConn.setDoOutput(true); // indicates POST method
                    httpConn.setDoInput(true);
                    httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    OutputStream outputStream = httpConn.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, Charset.defaultCharset()), true);

                    File uploadFile = new File(getFilesDir(), filename);
                    String fileName = uploadFile.getName();
                    writer.append("--" + boundary).append(LINE_FEED);
                    writer.append("Content-Disposition: form-data; name=\"logFile\"; filename=\"" + fileName + "\"").append(LINE_FEED);
                    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
                    writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
                    writer.append(LINE_FEED);
                    writer.flush();

                    FileInputStream inputStream = new FileInputStream(uploadFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    inputStream.close();
                    writer.append(LINE_FEED).flush();
                    writer.append("--" + boundary + "--").append(LINE_FEED);
                    writer.close();

                    // checks server's status code first
                    int status = httpConn.getResponseCode();
                    if (status == HttpURLConnection.HTTP_OK) {
                        // server response is OK
                        InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());
                        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = responseStreamReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        responseStreamReader.close();

                        String response = stringBuilder.toString();
                        // use response for further processing
                    } else {
                        throw new IOException("Server returned non-OK status: " + status);
                    }
                    httpConn.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();


    }
    // Only for testing sending
    protected void createDummyLogFile() {
        filename = "logfile.log";
        String fileContents = "Test log data...\nMore log data...";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
