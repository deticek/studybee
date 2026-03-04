package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Home extends AppCompatActivity {

    private TextView v, q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);

        v = findViewById(R.id.miniinfo);
        q = findViewById(R.id.qout);

        createFileIfNotExists();  // ustvari file če ne obstaja
        hoursofstudy();           // preberi vsebino in izpiši
        getquotes();
    }

    private void getquotes(){
        String qoutt = "A little progress each day adds up to big results";
        q.setText(qoutt);
    }

    private void createFileIfNotExists() {
        File file = new File(getFilesDir(), "hfs.txt");

        if (!file.exists()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write("0".getBytes()); // začetna vrednost
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Napaka pri ustvarjanju fila", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void hoursofstudy() {
        File file = new File(getFilesDir(), "hfs.txt");

        try (Scanner reader = new Scanner(file)) {
            if (reader.hasNextLine()) {
                String content = reader.nextLine().trim();
                v.setText("Hours of study: " + content);
            } else {
                v.setText("Hours of study: 0");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File ne obstaja", Toast.LENGTH_SHORT).show();
        }
    }

    public void nastavitve(View view){
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

    public void kuci(View view){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    public void cas(View view){
        Intent i = new Intent(this, Timer.class);
        startActivity(i);
    }

    public void infotocka(View view){
        Intent i = new Intent(this, Info.class);
        startActivity(i);
    }
}