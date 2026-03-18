package com.example.studybee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Nastavitve extends AppCompatActivity {

    DatabaseHelper dbHelper;

    Button fm;

    TextView m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nastavitve);

        fm = findViewById(R.id.focusbutton);
        m = findViewById(R.id.miniinfo);

        dbHelper = new DatabaseHelper(this);

        setTexts();
    }

    public void change(View v){
        dbHelper.setTimerMode();
        writeTimerMode();
        System.out.println(dbHelper.getTimerMode());
    }

    private void setTexts(){
        nastaviNapisFocus();
        writeTimerMode();

    }

    public void writeTimerMode(){
        String text = "Timer mode: ";
        text+= dbHelper.getTimerMode()? "Countdown" : "Stopwatch";
        m.setText(text);
    }

    public void nastaviNapisFocus(){
        String osnova = "FOCUSE MODE: ";
        String text = (FocusManager.focusEnable) ? osnova+"TRUE" : osnova+"FALSE";
        fm.setText(text);
    }

    public void izbrisidata(View v){
        // 1. Izbriši datoteke (če obstajajo)
        File nameFile = new File(getFilesDir(), "name.txt");
        File hfsFile = new File(getFilesDir(), "hfs.txt");

        try {
            if (nameFile.exists()) {
                FileOutputStream fos = new FileOutputStream(nameFile);
                fos.write("".getBytes());
                fos.close();
                System.out.println("name.txt izbrisan");
            }
            if (hfsFile.exists()) {
                FileOutputStream fos = new FileOutputStream(hfsFile);
                fos.write("0".getBytes());
                fos.close();
                System.out.println("hfs.txt izbrisan");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Izbriši vse podatke iz baze
       dbHelper.resetDb();
        System.out.println("Vsa baza izbrisana");

    }

    public void fokus(View v){


        String title;
        String text;

        if(FocusManager.focusEnable){
            FocusManager.focusEnable = false;
            title="Focus Mode Deactivated";
            text="When you will turn on the timer you will be able to leave the app.";

        } else {

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!nm.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                    // uporabnik mora ročno omogočit dostop
                } else {
                    // vklopi DND (samo alarma tvoj timer in tvoj push notifikacije lahko preglasijo)
                    //nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
                }
            }

            FocusManager.focusEnable = true;
                    title=("Focus Mode Activated");
                    text=("When you will turn on the timer you wont be able to leave the app.");
        }

        dbHelper.setFocusMode(FocusManager.focusEnable);
        nastaviNapisFocus();
        Notifications.pushNotification(this, title,text, Nastavitve.class);

    }

    public void soundsettings(View v){
        Intent i = new Intent(this, Sound.class);
        startActivity(i);
    }

    public void letsi(View v){
        Intent i = new Intent(this, Doseski.class);
        startActivity(i);
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