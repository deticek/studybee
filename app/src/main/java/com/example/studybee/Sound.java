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

public class Sound extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SoundPlayer sp = new SoundPlayer();

    TextView m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.soundsettings);

        m = findViewById(R.id.soundinfo);

        dbHelper = new DatabaseHelper(this);

        getInfo();

    }

    private void getInfo(){
        String t = "Current sound effect: Sound ";
        m.setText(t+dbHelper.getSound());
    }

    public void goback(View v){
        sp.turnOffPlyer();
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

    public void change(View v){
        int sund = Integer.parseInt(v.getTag().toString());
        dbHelper.setSound(sund);
        getInfo();
        sp.playSound(this,sund);
    }


}