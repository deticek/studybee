package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Doseski extends AppCompatActivity {

    private TextView v, q;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.doseski);



        // Inicializiraj dbHelper preden ga uporabiš
        dbHelper = new DatabaseHelper(this);

       // hoursofstudy();
    }

    private void hoursofstudy() {
        try {
            // Preberi skupni čas učenja iz baze
            String totalTime = dbHelper.getTotalStudyTime();
            v.setText("Hours of study: " + totalTime);
        } catch (Exception e) {
            e.printStackTrace();
            v.setText("Hours of study: 00:00:00");
            Toast.makeText(this, "Napaka pri branju baze", Toast.LENGTH_SHORT).show();
        }
    }

    public void goback(View v){
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

}