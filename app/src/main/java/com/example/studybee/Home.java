package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    private TextView v, q;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);

        v = findViewById(R.id.miniinfo);
        q = findViewById(R.id.qout);

        // Inicializiraj dbHelper preden ga uporabiš
        dbHelper = new DatabaseHelper(this);

        hoursofstudy();   // preberi vsebino iz baze in izpiši
        getquotes();
    }

    private void getquotes(){
        String qoutt = "A little progress each day adds up to big results";
        q.setText(qoutt);
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