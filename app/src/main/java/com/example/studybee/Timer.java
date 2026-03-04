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

public class Timer extends AppCompatActivity{

    TextView t;
    Button s;

    boolean startstop = false;

    int seconds = 0;

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.timer);

        t = findViewById(R.id.timer);
        s = findViewById(R.id.startbutton);
    }

    private String formatTime(int totalSeconds){

        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public void startstop(View v){

        if(!startstop){

            startstop = true;
            s.setText("Stop");

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

        seconds = 0;

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