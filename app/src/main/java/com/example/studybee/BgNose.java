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

public class BgNose extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SoundPlayer sp = new SoundPlayer();

    TextView m;

    Button b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sessionsound);

        m = findViewById(R.id.soundinfo);
        b = findViewById(R.id.onoffbutton);

        dbHelper = new DatabaseHelper(this);

        getInfo();

    }

    public void goback(View v){
        sp.turnOffPlyer();
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

    private void getInfo(){
        String t = "Current background sound: ";

        int num = dbHelper.getBgSound();

        switch (num) {
            case 1:
                t += "Fire place";
                break;
            case 2:
                t += "Rain";
                break;
            case 3:
                t += "Ocean";
                break;
            case 4:
                t += "Coffe shop";
                break;
            case 5:
                t += "Gray noise";
                break;
            default:
                break;
        }

        m.setText(t);

    }

    public void changesound(View v){
        int sund = Integer.parseInt(v.getTag().toString());
        dbHelper.setBgSound(sund);
        getInfo();

        int num = dbHelper.getBgSound();

        switch (num) {
            case 1:
                sp.playSound(this,sund);
                break;
            case 2:
                sp.playSound(this,sund);
                break;
            case 3:
                sp.playSound(this,sund);
                break;
            case 4:
                sp.playSound(this,sund);
                break;
            case 5:
                sp.playSound(this,sund);
                break;
            default:
                break;
        }

        sp.playBgSound(this,dbHelper.getBgSound());

    }

    public void change(View v){
        //int sund = Integer.parseInt(v.getTag().toString());
        dbHelper.setBgMode();
        String t = "BG SOUND: ";
        t += dbHelper.getBgMode()? "TRUE":"FALSE";
        b.setText(t);
    }


}