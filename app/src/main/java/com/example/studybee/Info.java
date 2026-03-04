package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Info extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.info);


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
