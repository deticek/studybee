package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Nastavitve extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nastavitve);


    }

    public void izbrisidata(View v){
        File file = new File(getFilesDir(), "name.txt");

        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write("".getBytes()); // Zapiše prazno vsebino, kar efektivno izbriše podatke
                fos.close();
                System.out.println("Podatki izbrisani"); // Posodobi UI, če je potrebno
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Datoteka ne obstaja");
        }

        file = new File(getFilesDir(), "hfs.txt");

        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write("0".getBytes()); // Zapiše prazno vsebino, kar efektivno izbriše podatke
                fos.close();
                System.out.println("Podatki izbrisani"); // Posodobi UI, če je potrebno
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Datoteka ne obstaja");
        }
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
