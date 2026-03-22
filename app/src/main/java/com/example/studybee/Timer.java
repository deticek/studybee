/*
 * Author: [Aleks Detiček]
 * Project: StudyBee
 * Year: 2026
 *
 * This source code is proprietary and confidential.
 * Unauthorized copying, distribution or use is strictly prohibited.
 */
package com.example.studybee;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Timer extends AppCompatActivity{

    TextView t;
    Button s;

    boolean startstop = false;
    int seconds = 0;
    long startTimeMillis = 0;

    int defultTime=0;

    DatabaseHelper dbHelper;
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;

    SoundPlayer sp = new SoundPlayer();
    Handler lockHandler = new Handler(Looper.getMainLooper());
    Runnable lockChecker;

    boolean firstCheck =false;
    int graceCounter = 0; // milliseconds
    int ssecends=0;

    boolean isCountdown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        dbHelper = new DatabaseHelper(this);
        isCountdown = dbHelper.getTimerMode();
        defultTime = dbHelper.getDefaultTime();

        if(!isCountdown){
            setContentView(R.layout.timer);
        }else{
            setContentView(R.layout.countdown);
        }

        t = findViewById(R.id.timer);
        s = findViewById(R.id.startbutton);

        SharedPreferences prefs = getSharedPreferences("timerPrefs", MODE_PRIVATE);
        int savedSeconds = prefs.getInt("seconds", -1); // -1 pomeni, da še ni nič shranjeno

        if(savedSeconds == -1) {
            // prvi zagon
            seconds = isCountdown ? defultTime*60 : 0;
        } else {
            // če je countdown in je shranjeno 0 → reset na default
            if(isCountdown && savedSeconds <= 0){
                seconds = defultTime*60;
            } else {
                seconds = savedSeconds;
            }
        }

        startstop = prefs.getBoolean("running", false);
        startTimeMillis = prefs.getLong("startTimeMillis", 0);

        t.setText(formatTime(seconds));
        s.setText(startstop ? "Stop" : "Start");

        runnable = new Runnable() {
            @Override
            public void run() {
                if (!startstop) return;

                ssecends++;

                if (isCountdown) {
                    if (seconds > 0) {
                        seconds--;
                    } else {
                        koncajtimer();
                        return;
                    }
                } else {
                    seconds++;
                }

                t.setText(formatTime(seconds));
                saveTimerState();
                handler.postDelayed(this, 1000);
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

        resetText();
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
        SharedPreferences.Editor editor = getSharedPreferences("timerPrefs", MODE_PRIVATE).edit();

        if (isCountdown) {
            editor.putInt("seconds", defultTime * 60); // countdown default
        } else {
            editor.putInt("seconds", 0); // stopwatch default
        }

        editor.putBoolean("running", false);
        editor.putLong("startTimeMillis", 0);
        editor.apply();
        resetText();
    }

    public void addtime(View v){
        dbHelper.setDefultTime(defultTime+5);
        defultTime= dbHelper.getDefaultTime();
        resetText();
    }

    public void removetime(View v){
        if(defultTime-5 >=5){
            dbHelper.setDefultTime(defultTime-5);
            defultTime= dbHelper.getDefaultTime();
        }
        resetText();
    }

    public void resettodefault(View v){
        dbHelper.setDefultTime(45);
        defultTime = dbHelper.getDefaultTime();
        resetText();
    }

    private void resetText(){
        seconds = !isCountdown?0:defultTime*60;
        t.setText(formatTime(seconds));
    }

    public void startstop(View v) {
        if (!startstop) {
            startstop = true;
            FocusManager.timerRunning = true;
            s.setText("Stop");

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            checkAchivements(2);

            if (FocusManager.focusEnable)
            {
                findViewById(R.id.obvestilo).setVisibility(View.VISIBLE);
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!nm.isNotificationPolicyAccessGranted()) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);
                        // uporabnik mora ročno omogočit dostop
                    } else {
                        // vklopi DND (samo alarma tvoj timer in tvoj push notifikacije lahko preglasijo)
                        nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                    }
                }
            }
            hideUIButtons();

            if (startTimeMillis == 0) startTimeMillis = System.currentTimeMillis();

            handler.post(runnable);
            if (FocusManager.focusEnable) lockHandler.post(lockChecker);

            if(dbHelper.getBgMode()) sp.playBgSound(this, dbHelper.getBgSound());

        } else {
            sp.turnOffPlyer();
            pauseTimer();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (nm.isNotificationPolicyAccessGranted()) {
                    nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                }
            }

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        saveTimerState();
    }

    private void pauseTimer() {
        startstop = false;
        FocusManager.timerRunning = false;
        s.setText("Start");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (nm.isNotificationPolicyAccessGranted()) {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }
        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findViewById(R.id.obvestilo).setVisibility(View.GONE);
        showUIButtons();

        handler.removeCallbacks(runnable);
        lockHandler.removeCallbacks(lockChecker);
        graceCounter = 0;
    }

    private void stopTimerDueToExit() {
        sp.turnOffPlyer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (nm.isNotificationPolicyAccessGranted()) {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }
        }

        Toast.makeText(this, "You left the app! Timer stopped.", Toast.LENGTH_LONG).show();
        Notifications.pushNotification(this, "Timer Stopped", "You left the app!", Timer.class);
        checkAchivements(25);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        startstop = false;



        FocusManager.timerRunning = false;
        handler.removeCallbacks(runnable);
        lockHandler.removeCallbacks(lockChecker);
        graceCounter = 0;

        findViewById(R.id.obvestilo).setVisibility(View.GONE);
        showUIButtons();


        seconds = isCountdown?0:defultTime*60;

        ssecends=0;
        startTimeMillis = 0;
        t.setText(formatTime(seconds));
        s.setText("Start");

        resetSavedTimer();

    }

    public void fertik(View v){
        sp.turnOffPlyer();
        koncajtimer();
    }

    public void koncajtimer() {

        startstop = false;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        FocusManager.timerRunning = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (nm.isNotificationPolicyAccessGranted()) {
                nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }
        }

        SoundPlayer sp = new SoundPlayer();
        sp.playSound(this, dbHelper.getSound());

        if(FocusManager.focusEnable) checkAchivements(12);

        handler.removeCallbacks(runnable);
        lockHandler.removeCallbacks(lockChecker);
        graceCounter = 0;

        findViewById(R.id.obvestilo).setVisibility(View.GONE);
        showUIButtons();

        if(isCountdown){
            findViewById(R.id.addless).setVisibility(View.VISIBLE);
            findViewById(R.id.addmore).setVisibility(View.VISIBLE);
            findViewById(R.id.defaultt).setVisibility(View.VISIBLE);
        }

        checkAchivements(3);

        long duration = ssecends;
        checkMilestones(ssecends);

        if (startTimeMillis != 0) {
            long endTimeMillis = System.currentTimeMillis();
            String startTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(startTimeMillis));
            String endTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(endTimeMillis));

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());
            dbHelper.insertSession(startTime, endTime, duration, date);
        }

        seconds = isCountdown ? defultTime*60 : 0;
        ssecends=0;

        sp.turnOffPlyer();

        startTimeMillis = 0;
        t.setText(formatTime(seconds));
        s.setText("Start");
        saveTimerState();
        resetSavedTimer();

        checkAchivements(3);

        int n = dbHelper.getNumOfSessions();

        switch (n){
            case 5:
                checkAchivements(9);
                break;
            case 10: checkAchivements(10);
                break;
            case 50: checkAchivements(11);
                break;
            case 100: checkAchivements(17);
                break;
            default:
                break;
        }

    }

    private void checkMilestones(int sec) {

        if(sec == 1) checkAchivements(20);
        if(sec < 10 && sec > 5) checkAchivements(23);
        if(sec < 60 && sec > 10) checkAchivements(24);
        if(sec < 5 && FocusManager.focusEnable && sec > 1) checkAchivements(19);
        if(sec >= 10800) checkAchivements(22);

        if (sec == 300) {
            if (FocusManager.focusEnable) {
                checkAchivements(13);
            } else {
                checkAchivements(4);
            }
        }

        if (sec == 600) {
            if (FocusManager.focusEnable) {
                checkAchivements(14);
            } else {
                checkAchivements(5);
            }
        }

        if (sec == 1500) {
            if (!FocusManager.focusEnable) {
                checkAchivements(6);
            }
        }

        if (sec == 1800) {
            if (FocusManager.focusEnable) {
                checkAchivements(15);
            } else {
                checkAchivements(7);
            }
        }

        if (sec == 3600) {
            if (FocusManager.focusEnable) {
                checkAchivements(16);
            } else {
                checkAchivements(8);
            }
        }

        if (sec == 7200) {
            if (FocusManager.focusEnable) {
                checkAchivements(18);
            }else{
                checkAchivements(21);
            }
        }
    }

    private void checkAchivements(int achId){

        Cursor c = dbHelper.getAchivement(achId);

        if (c != null && c.moveToFirst()) {
            int unlockValue = c.getInt(c.getColumnIndexOrThrow("unlock"));
            String name = c.getString(c.getColumnIndexOrThrow("name"));

            if (unlockValue == 0) {
                dbHelper.unlockAchievement(achId);
                Notifications.pushNotification(this, "New trophy unlocked!",name, Doseski.class);
            }
        }
    }

    private void hideUIButtons() {
        findViewById(R.id.homeb).setVisibility(View.GONE);
        findViewById(R.id.kolendarb).setVisibility(View.GONE);
        findViewById(R.id.textView3).setVisibility(View.GONE);
        findViewById(R.id.settingsb).setVisibility(View.GONE);
        findViewById(R.id.urab).setVisibility(View.GONE);

        if(isCountdown){
            findViewById(R.id.addless).setVisibility(View.GONE);
            findViewById(R.id.addmore).setVisibility(View.GONE);
            findViewById(R.id.defaultt).setVisibility(View.GONE);
        }

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