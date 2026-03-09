package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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