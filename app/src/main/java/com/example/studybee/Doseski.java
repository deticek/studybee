package com.example.studybee;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Doseski extends AppCompatActivity {

    private TextView v, q;
    private DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    ArrayList<Dosezek> doseski = new ArrayList<>();
    Doeski_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.doseski);

        v = findViewById(R.id.miniinfo);
        q = findViewById(R.id.numof);
        recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializiraj dbHelper preden ga uporabiš
        dbHelper = new DatabaseHelper(this);

       hoursofstudy();
       getNumOfAch();
       loadAch();


        adapter = new Doeski_Adapter(doseski, dbHelper);
        recyclerView.setAdapter(adapter);

    }

    private void hoursofstudy() {
        try {
            String totalTime = dbHelper.getTotalStudyTime();
            v.setText("Hours of study: " + totalTime);
        } catch (Exception e) {
            e.printStackTrace();
            v.setText("Hours of study: 00:00:00");
            Toast.makeText(this, "Napaka pri branju baze", Toast.LENGTH_SHORT).show();
        }
    }

    private  void getNumOfAch(){
        int num = dbHelper.getNumOfAchievements();
        q.setText("Numbers of achievements: "+num);
    }

    private void loadAch(){
        Cursor c = dbHelper.getAllAchivement();

        if (c != null && c.moveToFirst()) { // premakni na prvo vrstico samo, če obstaja
            do {
                int id = c.getInt(c.getColumnIndexOrThrow("id"));
                String name = c.getString(c.getColumnIndexOrThrow("name"));
                String des = c.getString(c.getColumnIndexOrThrow("description"));
                String date = c.getString(c.getColumnIndexOrThrow("date"));
                int un = c.getInt(c.getColumnIndexOrThrow("unlock"));
                int hid = c.getInt(c.getColumnIndexOrThrow("hidden"));

                boolean u = (un == 0) ? false : true;
                boolean h = (hid == 0) ? true : false;

                doseski.add(new Dosezek(id, name, des, u, date, h));

            } while (c.moveToNext());
        }
            c.close();
    }

    public void goback(View v){
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

}