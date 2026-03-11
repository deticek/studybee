package com.example.studybee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    Handler lockHandler = new Handler(Looper.getMainLooper());
    Runnable lockChecker;

    boolean firstCheck =false;
    int graceCounter = 0; // milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.timer);

        dbHelper = new DatabaseHelper(this);
        t = findViewById(R.id.timer);
        s = findViewById(R.id.startbutton);

        SharedPreferences prefs = getSharedPreferences("timerPrefs", MODE_PRIVATE);
        seconds = prefs.getInt("seconds", 0);
        startstop = prefs.getBoolean("running", false);
        startTimeMillis = prefs.getLong("startTimeMillis", 0);

        t.setText(formatTime(seconds));
        s.setText(startstop ? "Stop" : "Start");

        runnable = new Runnable() {
            @Override
            public void run() {
                if (startstop) {
                    seconds++;
                    t.setText(formatTime(seconds));
                    saveTimerState();
                    handler.postDelayed(this, 1000);
                }
            }
        };

        // Soft lock checker
        lockChecker = new Runnable() {
            @Override
            public void run() {
                if (startstop && FocusManager.focusEnable) {
                    if (!((MyApp) getApplication()).isInForeground) {
                        if(!firstCheck){
                            sendNotification();
                            firstCheck = true;
                        }
                        graceCounter += 500;
                        if (graceCounter >= 10000) {
                            stopTimerDueToExit();
                            return;
                        }
                    } else {
                        firstCheck=false;
                        graceCounter = 0;
                    }
                    lockHandler.postDelayed(this, 500);
                }
            }
        };

        if (startstop) {
            handler.post(runnable);
            if (FocusManager.focusEnable) lockHandler.post(lockChecker);
        }
    }

    private void sendNotification(){
        Notifications.pushNotification(this,"You left the app!","You have 10s to come back to app or the timer will be reset!", Timer.class);
    }

    @Override
    public void onBackPressed() {
        if (FocusManager.focusEnable && startstop) {
            Toast.makeText(this, "Focus mode active - cannot leave", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private String formatTime(int totalSeconds) {
        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s);
    }

    private void resetSavedTimer() {
        getSharedPreferences("timerPrefs", MODE_PRIVATE)
                .edit()
                .putInt("seconds", 0)
                .putBoolean("running", false)
                .putLong("startTimeMillis", 0)
                .apply();
    }

    public void startstop(View v) {
        if (!startstop) {
            startstop = true;
            FocusManager.timerRunning = true;
            s.setText("Stop");

            if(FocusManager.focusEnable) findViewById(R.id.obvestilo).setVisibility(View.VISIBLE);
            hideUIButtons();

            if (startTimeMillis == 0) startTimeMillis = System.currentTimeMillis();

            handler.post(runnable);
            if (FocusManager.focusEnable) lockHandler.post(lockChecker);
        } else {
            pauseTimer();
        }
        saveTimerState();
    }

    private void pauseTimer() {
        startstop = false;
        FocusManager.timerRunning = false;
        s.setText("Start");

        findViewById(R.id.obvestilo).setVisibility(View.GONE);
        showUIButtons();

        handler.removeCallbacks(runnable);
        lockHandler.removeCallbacks(lockChecker);
        graceCounter = 0;
    }

    private void stopTimerDueToExit() {
        Toast.makeText(this, "You left the app! Timer stopped.", Toast.LENGTH_LONG).show();
        Notifications.pushNotification(this, "Timer Stopped", "You left the app!", Timer.class);

        startstop = false;
        FocusManager.timerRunning = false;
        handler.removeCallbacks(runnable);
        lockHandler.removeCallbacks(lockChecker);
        graceCounter = 0;

        findViewById(R.id.obvestilo).setVisibility(View.GONE);
        showUIButtons();

        seconds = 0;
        startTimeMillis = 0;
        t.setText("00:00:00");
        s.setText("Start");

        resetSavedTimer();

    }

    public void koncajtimer(View v) {
        startstop = false;
        FocusManager.timerRunning = false;
        handler.removeCallbacks(runnable);
        lockHandler.removeCallbacks(lockChecker);
        graceCounter = 0;

        findViewById(R.id.obvestilo).setVisibility(View.GONE);
        showUIButtons();

        if (startTimeMillis != 0) {
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
        resetSavedTimer();

    }

    private void hideUIButtons() {
        findViewById(R.id.homeb).setVisibility(View.GONE);
        findViewById(R.id.kolendarb).setVisibility(View.GONE);
        findViewById(R.id.textView3).setVisibility(View.GONE);
        findViewById(R.id.settingsb).setVisibility(View.GONE);
        findViewById(R.id.urab).setVisibility(View.GONE);
    }

    private void showUIButtons() {
        findViewById(R.id.homeb).setVisibility(View.VISIBLE);
        findViewById(R.id.kolendarb).setVisibility(View.VISIBLE);
        findViewById(R.id.textView3).setVisibility(View.VISIBLE);
        findViewById(R.id.settingsb).setVisibility(View.VISIBLE);
        findViewById(R.id.urab).setVisibility(View.VISIBLE);
    }

    private void saveTimerState() {
        getSharedPreferences("timerPrefs", MODE_PRIVATE)
                .edit()
                .putInt("seconds", seconds)
                .putBoolean("running", startstop)
                .putLong("startTimeMillis", startTimeMillis)
                .apply();
    }

    public void nastavitve(View v) {
        startActivity(new Intent(this, Nastavitve.class));
    }

    public void kuci(View v) {
        startActivity(new Intent(this, Home.class));
    }

    public void cas(View v) {
        startActivity(new Intent(this, Timer.class));
    }

    public void infotocka(View v) {
        startActivity(new Intent(this, Info.class));
    }
}