package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Nastavitve extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nastavitve);

        dbHelper = new DatabaseHelper(this);
    }

    private void sendFocusNotification(){

        String channelId = "focus_channel";

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Focus Mode",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Focus mode notifications");

            manager.createNotificationChannel(channel);
        }
        Notification notification;
        if(FocusManager.focusEnable){
            notification = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.beeicon)
                    .setContentTitle("Focus Mode Activated")
                    .setContentText("When you will turn on the timer you wont be able to leave the app.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
        }else{
            notification = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.beeicon)
                    .setContentTitle("Focus Mode Deactivated")
                    .setContentText("When you will turn on the timer you will be able to leave the app.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build();
        }



        manager.notify(1, notification);
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
        dbHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_SESSIONS, null, null);
        dbHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_USER, null, null);
        System.out.println("Vsa baza izbrisana");

    }

    public void fokus(View v){


        if(FocusManager.focusEnable){
            FocusManager.focusEnable = false;
        } else {
            FocusManager.focusEnable = true;
        }

        dbHelper.setFocusMode(FocusManager.focusEnable);

        sendFocusNotification();

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