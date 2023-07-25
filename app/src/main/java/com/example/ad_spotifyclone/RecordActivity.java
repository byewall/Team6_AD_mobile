package com.example.ad_spotifyclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {
    private MediaRecorder recorder;
    private Chronometer chronometer;
    private ImageButton stopBtn;
    private boolean isRecording = true;

    private long baseTime;

    private long elapsedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();

        startDuration();
        startRecording();

        stopBtn = findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    stopRecording();
                    chronometer.stop();
                    stopDuration();
                    stopBtn.setImageResource(R.drawable.ic_start);
                    isRecording = false;
                } else {
                    startRecording();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    startDuration();
                    stopBtn.setImageResource(R.drawable.ic_stop);
                    isRecording = true;
                }
            }
        });

    }

    private void startRecording() {
        recorder = new MediaRecorder(getBaseContext());
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp");

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        chronometer.stop();
        stopDuration();
    }

    private void startDuration(){
        chronometer.getBase();
    }

    private void stopDuration(){
        elapsedTime = SystemClock.elapsedRealtime() - baseTime;
        String duration = String.valueOf(elapsedTime);
        File cacheDir = getExternalCacheDir();
        File logFile = new File(cacheDir.getAbsolutePath(), "log.txt");
        try {
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(baseTime + "\n");
            writer.write(elapsedTime + "\n");
            writer.write(duration + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}