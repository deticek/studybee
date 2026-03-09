package com.example.studybee;

import android.content.Intent;
import android.content.SharedPreferences;
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
    long startTimeMillis = 0;

    DatabaseHelper dbHelper;
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.timer);

        dbHelper = new DatabaseHelper(this);

        // 1️⃣ Inicializiraj UI
        t = findViewById(R.id.timer);
        s = findViewById(R.id.startbutton);

        // 2️⃣ Preberi stanje iz SharedPreferences
        SharedPreferences prefs = getSharedPreferences("timerPrefs", MODE_PRIVATE);
        seconds = prefs.getInt("seconds", 0);
        startstop = prefs.getBoolean("running", false);
        startTimeMillis = prefs.getLong("startTimeMillis", 0);

        t.setText(formatTime(seconds));
        s.setText(startstop ? "Stop" : "Start");

        // 3️⃣ Definiraj runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                if(startstop){
                    seconds++;
                    t.setText(formatTime(seconds));
                    saveTimerState();  // shrani vsak second
                    handler.postDelayed(this, 1000);
                }
            }
        };

        // 4️⃣ Če je timer tekel, nadaljuj
        if(startstop){
            handler.post(runnable);
        }
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

            // skrij gumbove
            findViewById(R.id.homeb).setVisibility(View.GONE);
            findViewById(R.id.kolendarb).setVisibility(View.GONE);
            findViewById(R.id.textView3).setVisibility(View.GONE);
            findViewById(R.id.settingsb).setVisibility(View.GONE);
            findViewById(R.id.urab).setVisibility(View.GONE);

            // shrani start timestamp samo prvič
            if(startTimeMillis == 0){
                startTimeMillis = System.currentTimeMillis();
            }

            handler.post(runnable);

        }else{
            startstop = false;
            s.setText("Start");

            // pokaži gumbove
            findViewById(R.id.homeb).setVisibility(View.VISIBLE);
            findViewById(R.id.kolendarb).setVisibility(View.VISIBLE);
            findViewById(R.id.textView3).setVisibility(View.VISIBLE);
            findViewById(R.id.settingsb).setVisibility(View.VISIBLE);
            findViewById(R.id.urab).setVisibility(View.VISIBLE);

            handler.removeCallbacks(runnable);
        }

        saveTimerState(); // shrani stanje ob start/pause
    }

    public void koncajtimer(View v){
        startstop = false;
        handler.removeCallbacks(runnable);

        if(startTimeMillis != 0){
            long endTimeMillis = System.currentTimeMillis();
            String startTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(startTimeMillis));
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
        saveTimerState();
    }

    private void saveTimerState() {
        getSharedPreferences("timerPrefs", MODE_PRIVATE)
                .edit()
                .putInt("seconds", seconds)
                .putBoolean("running", startstop)
                .putLong("startTimeMillis", startTimeMillis)
                .apply();
    }

    public void nastavitve(View v){
        startActivity(new Intent(this, Nastavitve.class));
    }

    public void kuci(View v){
        startActivity(new Intent(this, Home.class));
    }

    public void cas(View v){
        startActivity(new Intent(this, Timer.class));
    }

    public void infotocka(View v){
        startActivity(new Intent(this, Info.class));
    }
}