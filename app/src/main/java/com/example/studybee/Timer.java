package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Timer extends AppCompatActivity {

    TextView t;
    Button s;

    boolean startstop = false;

    int seconds = 0;

    DatabaseHelper dbHelper;

    long startTimeMillis = 0;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.timer);

        dbHelper = new DatabaseHelper(this);

        t = findViewById(R.id.timer);
        s = findViewById(R.id.startbutton);
    }

    private String formatTime(int totalSeconds){

        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
    }

    public void startstop(View v){

        if(!startstop){

            startstop = true;
            s.setText("Stop");

            // shrani start timestamp samo prvič
            if(startTimeMillis == 0){
                startTimeMillis = System.currentTimeMillis();
            }

            runnable = new Runnable() {
                @Override
                public void run() {

                    seconds++;

                    t.setText(formatTime(seconds));

                    handler.postDelayed(this, 1000);
                }
            };

            handler.post(runnable);

        }else{

            startstop = false;
            s.setText("Start");

            handler.removeCallbacks(runnable);
        }
    }

    public void koncajtimer(View v){

        startstop = false;

        handler.removeCallbacks(runnable);

        if(startTimeMillis != 0){

            long endTimeMillis = System.currentTimeMillis();

            // format start time
            String startTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(startTimeMillis));

            // format end time
            String endTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(endTimeMillis));

            long duration = seconds;

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());

            dbHelper.insertSession(startTime, endTime, duration, date);
        }

        seconds = 0;
        startTimeMillis = 0;

        t.setText("00:00:00");
        s.setText("Start");
    }

    public void nastavitve(View v){
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

    public void kuci(View v){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    public void cas(View v){
        Intent i = new Intent(this, Timer.class);
        startActivity(i);
    }

    public void infotocka(View v){
        Intent i = new Intent(this, Info.class);
        startActivity(i);
    }
}