package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    TextView v;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        v = findViewById(R.id.asdf);
        b = findViewById(R.id.button);

        // Preveri in ustvari datoteko, če ne obstaja
        createFileIfNotExists();

        myname();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void createFileIfNotExists() {
        File file = new File(getFilesDir(), "name.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write("".getBytes()); // Prazna vsebina
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void domov(View v){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void myname() {
        File namee = new File(getFilesDir(), "name.txt");

        if (!namee.exists()) {
            v.setText("No File");
            return;
        }

        try {
            Scanner reader = new Scanner(namee);
            if (reader.hasNextLine()) {
                String name = reader.nextLine().trim();
                if (name.isEmpty()) {
                    Intent intent = new Intent(this, GetName.class);
                    startActivity(intent);
                } else {
                    v.setText("Welcome "+name);
                }
            } else {
                Intent intent = new Intent(this, GetName.class);
                startActivity(intent);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
