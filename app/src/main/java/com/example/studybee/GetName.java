package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class GetName extends AppCompatActivity {

    Button b;
    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.getname);

        b=findViewById(R.id.submit);
        e=findViewById(R.id.getnametext);

    }

    public void goBack(View v){

        String name = e.getText().toString().trim();
        if (name.isEmpty()) {
            e.setError("Vnesi ime!");
            return;
        }

        File file = new File(getFilesDir(), "name.txt");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(name.getBytes());
            e.setText("");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    }

