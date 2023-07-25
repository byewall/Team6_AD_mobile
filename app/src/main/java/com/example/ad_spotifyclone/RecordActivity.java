package com.example.ad_spotifyclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.io.IOException;

public class RecordActivity extends AppCompatActivity {
    private MediaRecorder recorder;
    private Chronometer chronometer;
    private ImageButton stopBtn;
    private boolean isRecording = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();

        startRecording();

        stopBtn = findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    stopRecording();
                    chronometer.stop();
                    stopBtn.setImageResource(R.drawable.ic_start);
                    isRecording = false;
                } else {
                    startRecording();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
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
    }

}