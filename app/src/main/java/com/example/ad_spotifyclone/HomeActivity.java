package com.example.ad_spotifyclone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
        String API_URL = "https://example.com/api/upload";
        String filePath = "/path/to/your/file.txt"; // 请替换为你要上传的txt文件的实际路径

        File file = new File(filePath);
        OkHttpClient client = new OkHttpClient();

        // 构建文件请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_TEXT, file))
                .build();

        // 构建HTTP请求
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build();

        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this, "File upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 处理请求成功的响应
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HomeActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
