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
import java.util.Arrays;
import java.util.Scanner;

public class GetName extends AppCompatActivity {

    Button b;
    EditText e;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.getname);

        dbHelper = new DatabaseHelper(this);

        b = findViewById(R.id.submit);
        e = findViewById(R.id.getnametext);

    }

    public void goBack(View v) {

        String name = e.getText().toString().trim();
        if (name.isEmpty()) {
            e.setError("Vnesi ime!");
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        if (dbHelper.setUsername(name.toString())) {
            startActivity(intent);
        } else {
            System.out.println("error");
        }
    }
}

